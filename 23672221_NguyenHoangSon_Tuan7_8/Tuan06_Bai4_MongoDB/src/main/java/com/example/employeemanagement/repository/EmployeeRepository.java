package com.example.employeemanagement.repository;

import com.example.employeemanagement.dto.DepartmentCount;
import com.example.employeemanagement.dto.DepartmentSalary;
import com.example.employeemanagement.dto.EmployeeSearchCriteria;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class EmployeeRepository {

    private static final String COLLECTION = "department";
    private static final Document SORT_BY_ID = new Document("employees.employeeId", 1);

    private final MongoTemplate mongo;
    private final DepartmentRepository departmentRepository;

    public EmployeeRepository(MongoTemplate mongo, DepartmentRepository departmentRepository) {
        this.mongo = mongo;
        this.departmentRepository = departmentRepository;
    }

    public List<Employee> findAll() {
        return query(null, null, SORT_BY_ID, null);
    }

    public Optional<Employee> findById(Integer id) {
        return query(null, where("employees.employeeId").is(id), SORT_BY_ID, 1).stream().findFirst();
    }

    public Employee save(Employee e) {
        if (e.getId() == null) {
            e.setId(nextId());
            push(e);
            return e;
        }
        Employee old = findById(e.getId()).orElse(null);
        if (old == null) {
            push(e);
        } else if (Objects.equals(old.getDepartmentId(), e.getDepartmentId())) {
            // cùng phòng ban: sửa tại chỗ phần tử trong mảng bằng toán tử vị trí "$"
            Update u = new Update()
                    .set("employees.$.name", e.getName())
                    .set("employees.$.role", e.getRole())
                    .set("employees.$.age", e.getAge())
                    .set("employees.$.salary", e.getSalary())
                    .set("employees.$.positionId", e.getPositionId());
            mongo.updateFirst(
                    Query.query(where("_id").is(e.getDepartmentId()).and("employees.employeeId").is(e.getId())),
                    u, COLLECTION);
        } else {
            // đổi phòng ban: gỡ khỏi phòng cũ rồi đưa vào phòng mới (giữ nguyên id)
            deleteById(e.getId());
            push(e);
        }
        return e;
    }

    public void deleteById(Integer id) {
        mongo.updateMulti(new Query(),
                new Update().pull("employees", new Document("employeeId", id)),
                COLLECTION);
    }

    public List<Employee> findByNameContainingIgnoreCase(String name) {
        return query(null, nameLike(name), SORT_BY_ID, null);
    }

    public List<Employee> findByAgeBetween(int from, int to) {
        return query(null, where("employees.age").gte(from).lte(to), SORT_BY_ID, null);
    }

    public List<Employee> findBySalaryGreaterThan(double salary) {
        return query(null, where("employees.salary").gt(salary), SORT_BY_ID, null);
    }

    /** Tìm kết hợp nhiều tiêu chí: chỉ thêm điều kiện khi người dùng có nhập. */
    public List<Employee> search(EmployeeSearchCriteria c) {
        List<Criteria> list = new ArrayList<>();
        if (c.getId() != null) {
            list.add(where("employees.employeeId").is(c.getId()));
        }
        if (c.normalizedName() != null) {
            list.add(nameLike(c.normalizedName()));
        }
        if (c.getMinAge() != null) {
            list.add(where("employees.age").gte(c.getMinAge()));
        }
        if (c.getMaxAge() != null) {
            list.add(where("employees.age").lte(c.getMaxAge()));
        }
        if (c.getMinSalary() != null) {
            list.add(where("employees.salary").gte(c.getMinSalary()));
        }
        if (c.getPositionId() != null) {
            list.add(where("employees.positionId").is(c.getPositionId()));
        }
        Criteria emp = list.isEmpty() ? null : new Criteria().andOperator(list.toArray(new Criteria[0]));
        return query(null, emp, SORT_BY_ID, null);
    }

    public List<Employee> findByPositionId(Integer positionId) {
        return query(null, where("employees.positionId").is(positionId), SORT_BY_ID, null);
    }

    public List<Employee> findByDepartmentIdAndPositionId(Integer departmentId, Integer positionId) {
        return query(where("_id").is(departmentId),
                where("employees.positionId").is(positionId), SORT_BY_ID, null);
    }

    public List<Employee> findTopByDepartmentId(Integer departmentId, int n) {
        Document sort = new Document("employees.salary", -1).append("employees.employeeId", 1);
        return query(where("_id").is(departmentId), null, sort, n);
    }

    public List<DepartmentCount> countByDepartment() {
        Map<Integer, Document> stats = statsByDepartment();
        List<DepartmentCount> result = new ArrayList<>();
        for (Department d : departmentRepository.findAll(Sort.by("id"))) {
            DepartmentCount r = new DepartmentCount();
            r.setDepartmentId(d.getId());
            r.setDepartmentName(d.getName());
            Document s = stats.get(d.getId());
            r.setEmployeeCount(s == null ? 0 : number(s.get("employeeCount")).longValue());
            result.add(r);
        }
        return result;
    }

    public List<DepartmentSalary> salaryStatsByDepartment() {
        Map<Integer, Document> stats = statsByDepartment();
        List<DepartmentSalary> result = new ArrayList<>();
        for (Department d : departmentRepository.findAll(Sort.by("id"))) {
            DepartmentSalary r = new DepartmentSalary();
            r.setDepartmentId(d.getId());
            r.setDepartmentName(d.getName());
            Document s = stats.get(d.getId());
            if (s != null) {
                r.setEmployeeCount(number(s.get("employeeCount")).longValue());
                r.setAvgSalary(number(s.get("avgSalary")).doubleValue());
                r.setTotalSalary(number(s.get("totalSalary")).doubleValue());
            }
            result.add(r);
        }
        return result;
    }

    public List<Department> findDepartmentsWithoutEmployees() {
        Map<Integer, Document> stats = statsByDepartment();
        List<Department> result = new ArrayList<>();
        for (Department d : departmentRepository.findAll(Sort.by("id"))) {
            if (!stats.containsKey(d.getId())) {
                result.add(d);
            }
        }
        return result;
    }

    private MongoCollection<Document> departments() {
        return mongo.getCollection(COLLECTION);
    }

    private List<Employee> query(Criteria deptFilter, Criteria empFilter, Document sort, Integer limit) {
        List<Document> pipeline = new ArrayList<>();
        if (deptFilter != null) {
            pipeline.add(new Document("$match", deptFilter.getCriteriaObject()));
        }
        pipeline.add(new Document("$unwind", "$employees"));
        if (empFilter != null) {
            pipeline.add(new Document("$match", empFilter.getCriteriaObject()));
        }
        pipeline.add(new Document("$sort", sort));
        if (limit != null) {
            pipeline.add(new Document("$limit", limit));
        }
        List<Document> rows = departments().aggregate(pipeline).into(new ArrayList<>());
        List<Employee> result = new ArrayList<>();
        for (Document row : rows) {
            result.add(toEmployee(row));
        }
        return result;
    }

    private Map<Integer, Document> statsByDepartment() {
        List<Document> pipeline = List.of(
                new Document("$unwind", "$employees"),
                new Document("$group", new Document("_id", "$_id")
                        .append("employeeCount", new Document("$sum", 1))
                        .append("avgSalary", new Document("$avg", "$employees.salary"))
                        .append("totalSalary", new Document("$sum", "$employees.salary"))));
        List<Document> rows = departments().aggregate(pipeline).into(new ArrayList<>());
        Map<Integer, Document> map = new HashMap<>();
        for (Document r : rows) {
            map.put(number(r.get("_id")).intValue(), r);
        }
        return map;
    }

    private int nextId() {
        List<Document> pipeline = List.of(
                new Document("$unwind", "$employees"),
                new Document("$group", new Document("_id", null)
                        .append("max", new Document("$max", "$employees.employeeId"))));
        List<Document> rows = departments().aggregate(pipeline).into(new ArrayList<>());
        if (rows.isEmpty() || rows.get(0).get("max") == null) {
            return 1;
        }
        return number(rows.get(0).get("max")).intValue() + 1;
    }

    private void push(Employee e) {
        Document doc = new Document("employeeId", e.getId())
                .append("name", e.getName())
                .append("role", e.getRole())
                .append("age", e.getAge())
                .append("salary", e.getSalary())
                .append("positionId", e.getPositionId());
        UpdateResult r = mongo.updateFirst(
                Query.query(where("_id").is(e.getDepartmentId())),
                new Update().push("employees", doc), COLLECTION);
        if (r.getMatchedCount() == 0) {
            throw new IllegalArgumentException("Không tìm thấy phòng ban có ID " + e.getDepartmentId());
        }
    }

    private Criteria nameLike(String name) {
        return where("employees.name").regex(escapeRegex(name), "i");
    }

    private static String escapeRegex(String s) {
        StringBuilder sb = new StringBuilder();
        for (char ch : s.toCharArray()) {
            if (".\\+*?[^]$(){}=!<>|:-".indexOf(ch) >= 0) {
                sb.append('\\');
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    private static Number number(Object o) {
        return o instanceof Number n ? n : 0;
    }

    private Employee toEmployee(Document row) {
        Document d = row.get("employees", Document.class);
        Employee e = new Employee();
        e.setId(number(d.get("employeeId")).intValue());
        e.setName(d.getString("name"));
        e.setRole(d.getString("role"));
        e.setAge(d.get("age") instanceof Number a ? Integer.valueOf(a.intValue()) : null);
        e.setSalary(d.get("salary") instanceof Number s ? Double.valueOf(s.doubleValue()) : null);
        e.setDepartmentId(number(row.get("_id")).intValue());
        e.setPositionId(d.get("positionId") instanceof Number p ? Integer.valueOf(p.intValue()) : null);
        return e;
    }
}

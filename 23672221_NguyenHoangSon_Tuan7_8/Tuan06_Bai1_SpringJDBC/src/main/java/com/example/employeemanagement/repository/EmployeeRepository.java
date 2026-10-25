package com.example.employeemanagement.repository;

import com.example.employeemanagement.dto.DepartmentCount;
import com.example.employeemanagement.dto.DepartmentSalary;
import com.example.employeemanagement.dto.EmployeeSearchCriteria;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.mapper.DepartmentCountRowMapper;
import com.example.employeemanagement.mapper.DepartmentSalaryRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository {

    private static final String SELECT =
            "SELECT id, name, role, age, salary, department_id, position_id FROM employees";

    private static final RowMapper<Employee> ROW_MAPPER = (rs, rowNum) -> {
        Employee e = new Employee();
        e.setId(rs.getInt("id"));
        e.setName(rs.getString("name"));
        e.setRole(rs.getString("role"));
        e.setAge(rs.getObject("age", Integer.class));
        e.setSalary(rs.getObject("salary", Double.class));
        e.setDepartmentId(rs.getObject("department_id", Integer.class));
        e.setPositionId(rs.getObject("position_id", Integer.class));
        return e;
    };

    private final JdbcTemplate jdbc;

    public EmployeeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Employee> findAll() {
        return jdbc.query(SELECT + " ORDER BY id", ROW_MAPPER);
    }

    public Optional<Employee> findById(Integer id) {
        return jdbc.query(SELECT + " WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }

    public Employee save(Employee e) {
        if (e.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO employees (name, role, age, salary, department_id, position_id) VALUES (?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, e.getName());
                ps.setString(2, e.getRole());
                ps.setObject(3, e.getAge());
                ps.setObject(4, e.getSalary());
                ps.setObject(5, e.getDepartmentId());
                ps.setObject(6, e.getPositionId());
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key != null) e.setId(key.intValue());
        } else {
            jdbc.update("UPDATE employees SET name=?, role=?, age=?, salary=?, department_id=?, position_id=? WHERE id=?",
                    e.getName(), e.getRole(), e.getAge(), e.getSalary(),
                    e.getDepartmentId(), e.getPositionId(), e.getId());
        }
        return e;
    }

    public void deleteById(Integer id) {
        jdbc.update("DELETE FROM employees WHERE id = ?", id);
    }

    public List<Employee> findByNameContainingIgnoreCase(String name) {
        return jdbc.query(SELECT + " WHERE LOWER(name) LIKE ? ORDER BY id", ROW_MAPPER,
                "%" + name.toLowerCase() + "%");
    }

    public List<Employee> findByAgeBetween(int from, int to) {
        return jdbc.query(SELECT + " WHERE age BETWEEN ? AND ? ORDER BY id", ROW_MAPPER, from, to);
    }

    public List<Employee> findBySalaryGreaterThan(double salary) {
        return jdbc.query(SELECT + " WHERE salary > ? ORDER BY id", ROW_MAPPER, salary);
    }

    public List<Employee> search(EmployeeSearchCriteria c) {
        StringBuilder sql = new StringBuilder(SELECT + " WHERE 1=1");
        List<Object> args = new ArrayList<>();
        if (c.getId() != null) {
            sql.append(" AND id = ?");
            args.add(c.getId());
        }
        if (c.normalizedName() != null) {
            sql.append(" AND LOWER(name) LIKE ?");
            args.add("%" + c.normalizedName().toLowerCase() + "%");
        }
        if (c.getMinAge() != null) {
            sql.append(" AND age >= ?");
            args.add(c.getMinAge());
        }
        if (c.getMaxAge() != null) {
            sql.append(" AND age <= ?");
            args.add(c.getMaxAge());
        }
        if (c.getMinSalary() != null) {
            sql.append(" AND salary >= ?");
            args.add(c.getMinSalary());
        }
        if (c.getPositionId() != null) {
            sql.append(" AND position_id = ?");
            args.add(c.getPositionId());
        }
        sql.append(" ORDER BY id");
        return jdbc.query(sql.toString(), ROW_MAPPER, args.toArray());
    }

    public List<Employee> findByPositionId(Integer positionId) {
        return jdbc.query(SELECT + " WHERE position_id = ? ORDER BY id", ROW_MAPPER, positionId);
    }

    public List<Employee> findByDepartmentIdAndPositionId(Integer departmentId, Integer positionId) {
        return jdbc.query(SELECT + " WHERE department_id = ? AND position_id = ? ORDER BY id",
                ROW_MAPPER, departmentId, positionId);
    }

    public List<Employee> findTopByDepartmentId(Integer departmentId, int n) {
        return jdbc.query(SELECT + " WHERE department_id = ? ORDER BY salary DESC LIMIT ?",
                ROW_MAPPER, departmentId, n);
    }

    public List<DepartmentCount> countByDepartment() {
        return jdbc.query("""
                SELECT d.id AS department_id, d.name AS department_name, COUNT(e.id) AS employee_count
                FROM departments d LEFT JOIN employees e ON e.department_id = d.id
                GROUP BY d.id, d.name
                ORDER BY d.id
                """, new DepartmentCountRowMapper());
    }

    public List<DepartmentSalary> salaryStatsByDepartment() {
        return jdbc.query("""
                SELECT d.id AS department_id, d.name AS department_name,
                       COUNT(e.id) AS employee_count,
                       COALESCE(AVG(e.salary), 0) AS avg_salary,
                       COALESCE(SUM(e.salary), 0) AS total_salary
                FROM departments d LEFT JOIN employees e ON e.department_id = d.id
                GROUP BY d.id, d.name
                ORDER BY d.id
                """, new DepartmentSalaryRowMapper());
    }
}

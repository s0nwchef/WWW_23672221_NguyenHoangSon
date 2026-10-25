package com.example.employeemanagement.repository;

import com.example.employeemanagement.dto.DepartmentCount;
import com.example.employeemanagement.dto.DepartmentSalary;
import com.example.employeemanagement.dto.EmployeeSearchCriteria;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.mapper.DepartmentCountRowMapper;
import com.example.employeemanagement.mapper.DepartmentSalaryRowMapper;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JDBC: chỉ cần khai báo interface.
 * CRUD (save, findById, findAll, deleteById...) có sẵn từ ListCrudRepository.
 * Các method findByXxx được Spring tự sinh SQL từ tên method (derived query).
 */
public interface EmployeeRepository extends ListCrudRepository<Employee, Integer> {

    // ================= Tìm kiếm (derived query) =================
    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByAgeBetween(Integer from, Integer to);

    List<Employee> findBySalaryGreaterThan(Double salary);

    // ================= Quan hệ 1-n =================
    List<Employee> findByPositionId(Integer positionId);

    List<Employee> findByDepartmentIdAndPositionId(Integer departmentId, Integer positionId);

    /** Dạng derived query cố định N = 5 (đúng như gợi ý trong đề). */
    List<Employee> findTop5ByDepartmentIdOrderBySalaryDesc(Integer departmentId);

    /** Dạng N tùy chọn: phải viết @Query. */
    @Query("SELECT * FROM employees WHERE department_id = :departmentId ORDER BY salary DESC LIMIT :n")
    List<Employee> findTopByDepartmentId(@Param("departmentId") Integer departmentId, @Param("n") int n);

    // ================= Tìm kết hợp nhiều tiêu chí =================
    @Query("""
            SELECT * FROM employees
            WHERE (:id IS NULL OR id = :id)
              AND (:name IS NULL OR LOWER(name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:minAge IS NULL OR age >= :minAge)
              AND (:maxAge IS NULL OR age <= :maxAge)
              AND (:minSalary IS NULL OR salary >= :minSalary)
              AND (:positionId IS NULL OR position_id = :positionId)
            ORDER BY id
            """)
    List<Employee> searchAll(@Param("id") Integer id,
                             @Param("name") String name,
                             @Param("minAge") Integer minAge,
                             @Param("maxAge") Integer maxAge,
                             @Param("minSalary") Double minSalary,
                             @Param("positionId") Integer positionId);

    /** default method: Service gọi search(criteria) giống hệt project Spring JDBC. */
    default List<Employee> search(EmployeeSearchCriteria c) {
        return searchAll(c.getId(), c.normalizedName(), c.getMinAge(), c.getMaxAge(),
                c.getMinSalary(), c.getPositionId());
    }

    // ================= Thống kê (GROUP BY) - map sang DTO bằng RowMapper =================
    @Query(value = """
            SELECT d.id AS department_id, d.name AS department_name, COUNT(e.id) AS employee_count
            FROM departments d LEFT JOIN employees e ON e.department_id = d.id
            GROUP BY d.id, d.name
            ORDER BY d.id
            """, rowMapperClass = DepartmentCountRowMapper.class)
    List<DepartmentCount> countByDepartment();

    @Query(value = """
            SELECT d.id AS department_id, d.name AS department_name,
                   COUNT(e.id) AS employee_count,
                   COALESCE(AVG(e.salary), 0) AS avg_salary,
                   COALESCE(SUM(e.salary), 0) AS total_salary
            FROM departments d LEFT JOIN employees e ON e.department_id = d.id
            GROUP BY d.id, d.name
            ORDER BY d.id
            """, rowMapperClass = DepartmentSalaryRowMapper.class)
    List<DepartmentSalary> salaryStatsByDepartment();
}

package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Department;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartmentRepository {

    private static final RowMapper<Department> ROW_MAPPER = (rs, rowNum) -> {
        Department d = new Department();
        d.setId(rs.getInt("id"));
        d.setName(rs.getString("name"));
        return d;
    };

    private final JdbcTemplate jdbc;

    public DepartmentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Department> findAll() {
        return jdbc.query("SELECT id, name FROM departments ORDER BY id", ROW_MAPPER);
    }

    public List<Department> findWithoutEmployees() {
        return jdbc.query("""
                SELECT d.id, d.name
                FROM departments d LEFT JOIN employees e ON e.department_id = d.id
                WHERE e.id IS NULL
                ORDER BY d.id
                """, ROW_MAPPER);
    }
}

package com.example.employeemanagement.mapper;

import com.example.employeemanagement.dto.DepartmentSalary;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentSalaryRowMapper implements RowMapper<DepartmentSalary> {
    @Override
    public DepartmentSalary mapRow(ResultSet rs, int rowNum) throws SQLException {
        DepartmentSalary d = new DepartmentSalary();
        d.setDepartmentId(rs.getInt("department_id"));
        d.setDepartmentName(rs.getString("department_name"));
        d.setEmployeeCount(rs.getLong("employee_count"));
        d.setAvgSalary(rs.getDouble("avg_salary"));
        d.setTotalSalary(rs.getDouble("total_salary"));
        return d;
    }
}

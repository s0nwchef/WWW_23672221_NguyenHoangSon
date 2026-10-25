package com.example.employeemanagement.mapper;

import com.example.employeemanagement.dto.DepartmentCount;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentCountRowMapper implements RowMapper<DepartmentCount> {
    @Override
    public DepartmentCount mapRow(ResultSet rs, int rowNum) throws SQLException {
        DepartmentCount d = new DepartmentCount();
        d.setDepartmentId(rs.getInt("department_id"));
        d.setDepartmentName(rs.getString("department_name"));
        d.setEmployeeCount(rs.getLong("employee_count"));
        return d;
    }
}

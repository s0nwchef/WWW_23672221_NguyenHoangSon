package com.se.demorestdb.repo;

import com.se.demorestdb.model.Employee;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EmployeeRepoImpl implements EmployeeRepo {

    private volatile DataSource dataSource;

    private DataSource getDataSource() {
        if (dataSource == null) {
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                dataSource = (DataSource) env.lookup("jdbc/hrdb");
            } catch (NamingException e) {
                throw new RuntimeException("Không tìm thấy DataSource jdbc/hrdb", e);
            }
        }
        return dataSource;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (Connection con = getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setRole(rs.getString("role"));
                emp.setSalary(rs.getDouble("salary"));
                emp.setDepartmentId(rs.getInt("department_id"));
                list.add(emp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Employee getEmployeeById(int id) {
        Employee emp = null;
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection con = getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    emp = new Employee();
                    emp.setId(rs.getInt("id"));
                    emp.setName(rs.getString("name"));
                    emp.setRole(rs.getString("role"));
                    emp.setSalary(rs.getDouble("salary"));
                    emp.setDepartmentId(rs.getInt("department_id"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return emp;
    }

    public Employee addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (name,role, salary, department_id) VALUES (?, ?, ?, ?)";
        try (Connection con = getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getRole());

            ps.setDouble(3, employee.getSalary());
            ps.setInt(4, employee.getDepartmentId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    employee.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return employee;
    }

    @Override
    public Employee updateEmployee(int id, Employee employee) {
        String sql = "UPDATE employees SET name = ?, role = ?, salary = ?, department_id = ? WHERE id = ?";
        try (Connection con = getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getRole());
            ps.setDouble(3, employee.getSalary());
            ps.setInt(4, employee.getDepartmentId());
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return employee;
    }

}

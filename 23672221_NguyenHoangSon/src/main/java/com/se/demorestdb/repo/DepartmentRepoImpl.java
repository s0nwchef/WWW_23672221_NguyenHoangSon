package com.se.demorestdb.repo;

import com.se.demorestdb.model.Department;
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
public class DepartmentRepoImpl implements DepartmentRepo {

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
    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM departments";

        try (Connection con = getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Department dep = new Department();
                dep.setId(rs.getInt("id"));
                dep.setName(rs.getString("name"));
                list.add(dep);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Department getDepartmentById(int id) {
        Department dep = null;
        String sql = "SELECT * FROM departments WHERE id = ?";
        try (Connection con = getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dep = new Department();
                    dep.setId(rs.getInt("id"));
                    dep.setName(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dep;
    }

    @Override
    public Department addDepartment(Department department) {
        String sql = "INSERT INTO departments (name) VALUES (?)";
        try (Connection con = getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, department.getName());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    department.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return department;
    }

    @Override
    public Department updateDepartment(int id, Department department) {
        String sql = "UPDATE departments SET name = ? WHERE id = ?";
        try (Connection con = getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, department.getName());
            ps.setInt(2, id);
            if (ps.executeUpdate() == 0) {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        department.setId(id);
        return department;
    }

    @Override
    public boolean deleteDepartment(int id) {
        String sql = "DELETE FROM departments WHERE id = ?";
        try (Connection con = getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

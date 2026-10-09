package com.se.demorestdb.service;

import com.se.demorestdb.model.Department;
import com.se.demorestdb.repo.DepartmentRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class DepartmentService {
    @Inject
    private DepartmentRepo departmentRepo;

    public DepartmentService() {
    }

    public List<Department> getAllDepartments() {
        return departmentRepo.getAllDepartments();
    }

    public Department getDepartmentById(int id) {
        return departmentRepo.getDepartmentById(id);
    }

    public Department addDepartment(Department department) {
        return departmentRepo.addDepartment(department);
    }

    public Department updateDepartment(int id, Department department) {
        return departmentRepo.updateDepartment(id, department);
    }

    public boolean deleteDepartment(int id) {
        return departmentRepo.deleteDepartment(id);
    }
}

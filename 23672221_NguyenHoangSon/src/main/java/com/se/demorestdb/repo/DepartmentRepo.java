package com.se.demorestdb.repo;

import com.se.demorestdb.model.Department;

import java.util.List;

public interface DepartmentRepo {
    List<Department> getAllDepartments();

    // get Department by id
    Department getDepartmentById(int id);

    // add new Department
    Department addDepartment(Department department);

    // update Department (trả về null nếu không tồn tại)
    Department updateDepartment(int id, Department department);

    // delete Department
    boolean deleteDepartment(int id);
}

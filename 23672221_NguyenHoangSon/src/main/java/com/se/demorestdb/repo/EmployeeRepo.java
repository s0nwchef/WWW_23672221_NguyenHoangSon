package com.se.demorestdb.repo;

import com.se.demorestdb.model.Employee;

import java.util.List;

public interface EmployeeRepo {
    List<Employee> getAllEmployees();

    //get Employee by id
    Employee getEmployeeById(int id);

    // add new Employee
    Employee addEmployee(Employee employee);

    // update Employee
    Employee updateEmployee(int id, Employee employee);

    // delete Employee
    //boolean deleteEmployee(int id);

    // get Employee by department_id
    //List<Employee> getEmployeesByDepartmentId(int departmentId);

}

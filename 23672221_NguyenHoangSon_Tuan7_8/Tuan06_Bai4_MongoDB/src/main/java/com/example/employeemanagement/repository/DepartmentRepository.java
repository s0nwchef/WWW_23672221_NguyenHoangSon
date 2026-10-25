package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmentRepository extends MongoRepository<Department, Integer> {
}

package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByAgeBetween(Integer from, Integer to);

    List<Employee> findBySalaryGreaterThan(Double salary);

    List<Employee> findByPositionId(Integer positionId);

    List<Employee> findByDepartmentIdAndPositionId(Integer departmentId, Integer positionId);

    List<Employee> findTop5ByDepartmentIdOrderBySalaryDesc(Integer departmentId);

    List<Employee> findByDepartmentId(Integer departmentId, Pageable pageable);
}

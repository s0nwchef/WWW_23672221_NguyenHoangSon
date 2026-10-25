package com.example.employeemanagement.repository;

import com.example.employeemanagement.dto.DepartmentCount;
import com.example.employeemanagement.dto.DepartmentSalary;
import com.example.employeemanagement.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    @Query("""
            select new com.example.employeemanagement.dto.DepartmentCount(d.id, d.name, count(e.id))
            from Department d left join Employee e on e.departmentId = d.id
            group by d.id, d.name
            order by d.id
            """)
    List<DepartmentCount> countEmployeesByDepartment();

    @Query("""
            select new com.example.employeemanagement.dto.DepartmentSalary(
                   d.id, d.name, count(e.id), avg(e.salary), sum(e.salary))
            from Department d left join Employee e on e.departmentId = d.id
            group by d.id, d.name
            order by d.id
            """)
    List<DepartmentSalary> salaryStatsByDepartment();

    @Query("""
            select d from Department d
            where not exists (select 1 from Employee e where e.departmentId = d.id)
            order by d.id
            """)
    List<Department> findWithoutEmployees();
}

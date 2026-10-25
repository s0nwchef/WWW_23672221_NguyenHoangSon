package com.example.employeemanagement.repository;

import com.example.employeemanagement.dto.EmployeeSearchCriteria;
import com.example.employeemanagement.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

public final class EmployeeSpecifications {

    private EmployeeSpecifications() { }

    public static Specification<Employee> fromCriteria(EmployeeSearchCriteria c) {
        Specification<Employee> spec = Specification.where((Specification<Employee>) null);
        if (c.getId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("id"), c.getId()));
        }
        if (c.normalizedName() != null) {
            String pattern = "%" + c.normalizedName().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), pattern));
        }
        if (c.getMinAge() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.<Integer>get("age"), c.getMinAge()));
        }
        if (c.getMaxAge() != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.<Integer>get("age"), c.getMaxAge()));
        }
        if (c.getMinSalary() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.<Double>get("salary"), c.getMinSalary()));
        }
        if (c.getPositionId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("positionId"), c.getPositionId()));
        }
        return spec;
    }
}

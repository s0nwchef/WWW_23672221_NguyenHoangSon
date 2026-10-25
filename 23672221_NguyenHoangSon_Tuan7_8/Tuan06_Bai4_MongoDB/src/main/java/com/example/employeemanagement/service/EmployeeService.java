package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.*;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.Position;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.PositionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           PositionRepository positionRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Integer id) {
        return employeeRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên có ID " + id));
    }

    public EmployeeForm getForm(Integer id) {
        Employee e = findById(id);
        EmployeeForm f = new EmployeeForm();
        f.setId(e.getId());
        f.setName(e.getName());
        f.setRole(e.getRole());
        f.setAge(e.getAge());
        f.setSalary(e.getSalary());
        f.setDepartmentId(e.getDepartmentId());
        f.setPositionId(e.getPositionId());
        return f;
    }

    public Employee save(Integer id, EmployeeForm form) {
        Employee e = (id == null) ? new Employee() : findById(id);
        e.setName(form.getName().trim());
        e.setRole(form.getRole());
        e.setAge(form.getAge());
        e.setSalary(form.getSalary());
        e.setDepartmentId(form.getDepartmentId());
        e.setPositionId(form.getPositionId());
        return employeeRepository.save(e);
    }

    public void delete(Integer id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> search(EmployeeSearchCriteria criteria) {
        return employeeRepository.search(criteria);
    }

    public List<Employee> findByPosition(Integer positionId) {
        return employeeRepository.findByPositionId(positionId);
    }

    public List<Employee> findByDepartmentAndPosition(Integer departmentId, Integer positionId) {
        return employeeRepository.findByDepartmentIdAndPositionId(departmentId, positionId);
    }

    public List<Employee> topByDepartment(Integer departmentId, int n) {
        return employeeRepository.findTopByDepartmentId(departmentId, n);
    }

    public List<DepartmentCount> countByDepartment() {
        return employeeRepository.countByDepartment();
    }

    public List<DepartmentSalary> salaryByDepartment() {
        return employeeRepository.salaryStatsByDepartment();
    }

    public List<Department> departmentsWithoutEmployees() {
        return employeeRepository.findDepartmentsWithoutEmployees();
    }

    public List<Department> departments() { return departmentRepository.findAll(Sort.by("id")); }
    public List<Position> positions() { return positionRepository.findAll(Sort.by("id")); }

    public Map<Integer, String> departmentNames() {
        Map<Integer, String> m = new LinkedHashMap<>();
        departments().forEach(d -> m.put(d.getId(), d.getName()));
        return m;
    }

    public Map<Integer, String> positionNames() {
        Map<Integer, String> m = new LinkedHashMap<>();
        positions().forEach(p -> m.put(p.getId(), p.getTitle()));
        return m;
    }
}

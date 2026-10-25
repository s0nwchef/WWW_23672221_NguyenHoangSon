package com.example.employeemanagement.dto;

import jakarta.validation.constraints.*;

public class EmployeeForm {

    private Integer id;
    @NotBlank(message = "Tên không được để trống")
    @Size(max = 255)
    private String name;
    private String role;
    @NotNull(message = "Tuổi không được để trống")
    @Min(value = 18, message = "Tuổi tối thiểu là 18")
    @Max(value = 65, message = "Tuổi tối đa là 65")
    private Integer age;
    @NotNull(message = "Lương không được để trống")
    @PositiveOrZero(message = "Lương phải >= 0")
    private Double salary;
    @NotNull(message = "Hãy chọn phòng ban")
    private Integer departmentId;
    @NotNull(message = "Hãy chọn vị trí")
    private Integer positionId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public Integer getPositionId() { return positionId; }
    public void setPositionId(Integer positionId) { this.positionId = positionId; }

}

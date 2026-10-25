package com.example.employeemanagement.dto;

public class DepartmentCount {

    private Integer departmentId;
    private String departmentName;
    private long employeeCount;

    public DepartmentCount() { }

    public DepartmentCount(Integer departmentId, String departmentName, Long employeeCount) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.employeeCount = employeeCount == null ? 0 : employeeCount;
    }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public long getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(long employeeCount) { this.employeeCount = employeeCount; }

}

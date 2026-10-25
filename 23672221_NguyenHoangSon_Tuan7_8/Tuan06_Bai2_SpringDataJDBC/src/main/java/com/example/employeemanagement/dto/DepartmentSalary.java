package com.example.employeemanagement.dto;

public class DepartmentSalary {

    private Integer departmentId;
    private String departmentName;
    private long employeeCount;
    private double avgSalary;
    private double totalSalary;

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public long getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(long employeeCount) { this.employeeCount = employeeCount; }

    public double getAvgSalary() { return avgSalary; }
    public void setAvgSalary(double avgSalary) { this.avgSalary = avgSalary; }

    public double getTotalSalary() { return totalSalary; }
    public void setTotalSalary(double totalSalary) { this.totalSalary = totalSalary; }

}

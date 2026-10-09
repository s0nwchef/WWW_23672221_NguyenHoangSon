package com.se.demorestdb.model;

public class Employee {
    private int id;
    private String name;
    private String role;
    private double salary;
    private int departmentId;

    public Employee() {
    }

    public Employee(int id, String name, String role, double salary, int departmentId) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.departmentId = departmentId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
}

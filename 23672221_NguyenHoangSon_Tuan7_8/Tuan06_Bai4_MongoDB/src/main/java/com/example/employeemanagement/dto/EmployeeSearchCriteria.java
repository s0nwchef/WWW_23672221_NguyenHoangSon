package com.example.employeemanagement.dto;

public class EmployeeSearchCriteria {

    private Integer id;
    private String name;
    private Integer minAge;
    private Integer maxAge;
    private Double minSalary;
    private Integer positionId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }

    public Integer getMaxAge() { return maxAge; }
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }

    public Double getMinSalary() { return minSalary; }
    public void setMinSalary(Double minSalary) { this.minSalary = minSalary; }

    public Integer getPositionId() { return positionId; }
    public void setPositionId(Integer positionId) { this.positionId = positionId; }

    /** Có ít nhất một tiêu chí được nhập hay không. */
    public boolean hasAny() {
        return id != null || normalizedName() != null || minAge != null
                || maxAge != null || minSalary != null || positionId != null;
    }

    /** Trả về name đã trim, hoặc null nếu để trống. */
    public String normalizedName() {
        return (name == null || name.isBlank()) ? null : name.trim();
    }

}

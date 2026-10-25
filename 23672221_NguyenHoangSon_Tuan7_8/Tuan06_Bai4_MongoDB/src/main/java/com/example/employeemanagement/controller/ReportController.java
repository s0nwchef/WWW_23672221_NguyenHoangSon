package com.example.employeemanagement.controller;

import com.example.employeemanagement.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final EmployeeService service;

    public ReportController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("departments", service.departments());
        model.addAttribute("positions", service.positions());
        model.addAttribute("deptCounts", service.countByDepartment());
        model.addAttribute("deptSalaries", service.salaryByDepartment());
        model.addAttribute("emptyDepartments", service.departmentsWithoutEmployees());
        return "reports/index";
    }

    @GetMapping("/by-position")
    public String byPosition(@RequestParam Integer positionId, Model model) {
        prepare(model, "Nhân viên theo vị trí: " + service.positionNames().get(positionId));
        model.addAttribute("employees", service.findByPosition(positionId));
        return "reports/employees";
    }

    @GetMapping("/by-dept-position")
    public String byDeptAndPosition(@RequestParam Integer departmentId,
                                    @RequestParam Integer positionId, Model model) {
        prepare(model, "Nhân viên phòng " + service.departmentNames().get(departmentId)
                + " - vị trí " + service.positionNames().get(positionId));
        model.addAttribute("employees", service.findByDepartmentAndPosition(departmentId, positionId));
        return "reports/employees";
    }

    @GetMapping("/top")
    public String top(@RequestParam Integer departmentId,
                      @RequestParam(defaultValue = "5") int n, Model model) {
        prepare(model, "Top " + n + " lương cao nhất - phòng " + service.departmentNames().get(departmentId));
        model.addAttribute("employees", service.topByDepartment(departmentId, n));
        return "reports/employees";
    }

    private void prepare(Model model, String title) {
        model.addAttribute("title", title);
        model.addAttribute("departmentNames", service.departmentNames());
        model.addAttribute("positionNames", service.positionNames());
    }
}

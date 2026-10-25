package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeForm;
import com.example.employeemanagement.dto.EmployeeSearchCriteria;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.Position;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ModelAttribute("departments")
    public List<Department> departments() { return employeeService.departments(); }

    @ModelAttribute("positions")
    public List<Position> positions() { return employeeService.positions(); }

    @ModelAttribute("departmentNames")
    public Map<Integer, String> departmentNames() { return employeeService.departmentNames(); }

    @ModelAttribute("positionNames")
    public Map<Integer, String> positionNames() { return employeeService.positionNames(); }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "employees/list";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("criteria") EmployeeSearchCriteria criteria, Model model) {
        if (criteria.hasAny()) {
            model.addAttribute("employees", employeeService.search(criteria));
        }
        return "employees/search";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("employeeForm", new EmployeeForm());
        return "employees/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("employeeForm") EmployeeForm form,
                         BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "employees/form";
        }
        Employee saved = employeeService.save(null, form);
        redirect.addFlashAttribute("message", "Đã thêm nhân viên " + saved.getName() + ".");
        return "redirect:/employees";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        return "employees/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("employeeForm", employeeService.getForm(id));
        return "employees/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("employeeForm") EmployeeForm form,
                         BindingResult result, RedirectAttributes redirect) {
        form.setId(id);
        if (result.hasErrors()) {
            return "employees/form";
        }
        Employee saved = employeeService.save(id, form);
        redirect.addFlashAttribute("message", "Đã lưu thay đổi của nhân viên " + saved.getName() + ".");
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirect) {
        employeeService.delete(id);
        redirect.addFlashAttribute("message", "Đã xóa nhân viên có ID " + id + ".");
        return "redirect:/employees";
    }
}

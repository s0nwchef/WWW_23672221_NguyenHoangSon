package com.example.shopping.controller;
import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.web.bind.annotation.*; import java.time.LocalDate;
@Controller @RequestMapping({"/", "/home"}) public class HomeController { @GetMapping public String homePage(Model model) { model.addAttribute("message", "Shopping Management System"); model.addAttribute("date", LocalDate.now()); return "home"; } }

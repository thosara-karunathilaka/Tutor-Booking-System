package com.tbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/admin")
    public String adminDashboard(HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("role"))) return "redirect:/login";
        return "dashboard/admin";
    }

    @GetMapping("/tutor")
    public String tutorDashboard(HttpSession session) {
        if (!"TUTOR".equals(session.getAttribute("role"))) return "redirect:/login";
        return "dashboard/tutor";
    }

    @GetMapping("/student")
    public String studentDashboard(HttpSession session) {
        if (!"STUDENT".equals(session.getAttribute("role"))) return "redirect:/login";
        return "dashboard/student";
    }
}

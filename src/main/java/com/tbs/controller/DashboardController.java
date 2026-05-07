package com.tbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/admin")
    public String adminDashboard() {
        return "dashboard/admin";
    }

    @GetMapping("/tutor")
    public String tutorDashboard() {
        return "dashboard/tutor";
    }

    @GetMapping("/student")
    public String studentDashboard() {
        return "dashboard/student";
    }
}

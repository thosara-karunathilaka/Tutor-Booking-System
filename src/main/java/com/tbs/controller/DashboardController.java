package com.tbs.controller;

import com.tbs.model.Course;
import com.tbs.model.Enrollment;
import com.tbs.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private CourseService courseService;

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
    public String studentDashboard(HttpSession session, Model model) {
        if (!"STUDENT".equals(session.getAttribute("role"))) return "redirect:/login";
        String studentId = (String) session.getAttribute("userId");
        // Get all enrolled course IDs for this student
        List<String> enrolledCourseIds = courseService.getEnrollmentsByStudent(studentId)
                .stream().map(Enrollment::getCourseId).collect(Collectors.toList());
        // Fetch the full course details for each enrolled course
        List<Course> enrolledCourses = courseService.getAllCourses().stream()
                .filter(c -> enrolledCourseIds.contains(c.getCourseId()))
                .collect(Collectors.toList());
        model.addAttribute("enrolledCourses", enrolledCourses);
        return "dashboard/student";
    }
}

package com.tbs.controller;

import com.tbs.model.Course;
import com.tbs.model.Enrollment;
import com.tbs.service.CourseService;
import com.tbs.service.StudentService;
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

    @Autowired
    private StudentService studentService;

    @GetMapping("/admin")
    public String adminDashboard(HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("role"))) return "redirect:/login";
        return "dashboard/admin";
    }

    @GetMapping("/tutor")
    public String tutorDashboard(HttpSession session, Model model) {
        if (!"TUTOR".equals(session.getAttribute("role"))) return "redirect:/login";
        String tutorId = (String) session.getAttribute("userId");
        // My courses
        List<Course> myCourses = courseService.getCoursesByTutor(tutorId);
        model.addAttribute("myCourses", myCourses);
        // Total unique enrolled students across all my courses
        long totalStudents = myCourses.stream()
                .flatMap(c -> courseService.getEnrollmentsByCourse(c.getCourseId()).stream())
                .map(Enrollment::getStudentId)
                .distinct()
                .count();
        model.addAttribute("totalStudents", totalStudents);
        return "dashboard/tutor";
    }

    @GetMapping("/student")
    public String studentDashboard(HttpSession session, Model model) {
        if (!"STUDENT".equals(session.getAttribute("role"))) return "redirect:/login";
        String studentId = (String) session.getAttribute("userId");
        // Enrolled courses
        List<String> enrolledCourseIds = courseService.getEnrollmentsByStudent(studentId)
                .stream().map(Enrollment::getCourseId).collect(Collectors.toList());
        List<Course> enrolledCourses = courseService.getAllCourses().stream()
                .filter(c -> enrolledCourseIds.contains(c.getCourseId()))
                .collect(Collectors.toList());
        model.addAttribute("enrolledCourses", enrolledCourses);
        // Total available courses count for stat card
        model.addAttribute("totalCourses", courseService.getAllCourses().size());
        return "dashboard/student";
    }
}

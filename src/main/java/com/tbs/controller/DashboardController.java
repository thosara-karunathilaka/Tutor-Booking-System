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
        // Get all enrollments with their status
        List<com.tbs.model.Enrollment> enrollments = courseService.getEnrollmentsByStudent(studentId);
        List<String> enrolledCourseIds = enrollments.stream()
                .map(com.tbs.model.Enrollment::getCourseId).collect(Collectors.toList());
        List<Course> enrolledCourses = courseService.getAllCourses().stream()
                .filter(c -> enrolledCourseIds.contains(c.getCourseId()))
                .collect(Collectors.toList());
        // Build courseId -> status map
        java.util.Map<String, String> enrollmentStatuses = new java.util.HashMap<>();
        // Build courseId -> classLink map
        java.util.Map<String, String> classLinkMap = new java.util.HashMap<>();
        for (com.tbs.model.Enrollment e : enrollments) {
            enrollmentStatuses.put(e.getCourseId(), e.getStatus() != null ? e.getStatus() : "ACTIVE");
            if (e.getClassLink() != null && !e.getClassLink().isBlank()) {
                classLinkMap.put(e.getCourseId(), e.getClassLink());
            }
        }
        long completedCount = enrollmentStatuses.values().stream().filter("COMPLETED"::equals).count();
        long activeCount = enrollmentStatuses.values().stream().filter("ACTIVE"::equals).count();
        model.addAttribute("enrolledCourses", enrolledCourses);
        model.addAttribute("enrollmentStatuses", enrollmentStatuses);
        model.addAttribute("classLinkMap", classLinkMap);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("totalCourses", courseService.getAllCourses().size());
        return "dashboard/student";
    }
}

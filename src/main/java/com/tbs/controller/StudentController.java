package com.tbs.controller;

import com.tbs.model.Student;
import com.tbs.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;
import com.tbs.service.CourseService;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;

    public StudentController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping("/list")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "student/list";
    }

    @GetMapping("/add")
    public String addStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "student/add";
    }

    @PostMapping("/add")
    public String addStudentSubmit(@ModelAttribute Student student) {
        student.setRole("STUDENT");
        studentService.saveStudent(student);
        return "redirect:/student/list";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null || !"STUDENT".equals(session.getAttribute("role"))) return "redirect:/login";
        model.addAttribute("student", studentService.getStudentById(userId));
        return "student/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute Student updatedStudent, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null || !"STUDENT".equals(session.getAttribute("role"))) return "redirect:/login";
        updatedStudent.setUserId(userId);
        updatedStudent.setRole("STUDENT");
        studentService.updateStudent(updatedStudent);
        session.setAttribute("user", updatedStudent);
        return "redirect:/dashboard/student";
    }

    @GetMapping("/courses")
    public String browseCourses(HttpSession session, Model model) {
        if (!"STUDENT".equals(session.getAttribute("role"))) return "redirect:/login";
        model.addAttribute("courses", courseService.getAllCourses());
        return "student/courses";
    }

    @GetMapping("/enroll/{courseId}")
    public String enrollInCourse(@PathVariable String courseId, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null || !"STUDENT".equals(session.getAttribute("role"))) return "redirect:/login";
        courseService.enrollStudent(userId, courseId);
        return "redirect:/student/courses?enrolled=true";
    }

    @GetMapping("/tutor/{tutorId}/courses")
    public String browseTutorCourses(@PathVariable String tutorId, HttpSession session, Model model) {
        if (!"STUDENT".equals(session.getAttribute("role"))) return "redirect:/login";
        model.addAttribute("courses", courseService.getCoursesByTutor(tutorId));
        return "student/courses";
    }
}

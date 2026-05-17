package com.tbs.controller;

import com.tbs.model.Tutor;
import com.tbs.service.TutorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.tbs.service.CourseService;
import com.tbs.model.Course;
import com.tbs.model.Enrollment;
import com.tbs.model.Student;
import com.tbs.service.StudentService;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tutor")
public class TutorController {

    private final TutorService tutorService;
    private final CourseService courseService;
    private final StudentService studentService;

    public TutorController(TutorService tutorService, CourseService courseService, StudentService studentService) {
        this.tutorService = tutorService;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @GetMapping("/list")
    public String listTutors(Model model, HttpSession session) {
        model.addAttribute("tutors", tutorService.getAllTutors());
        String role = (String) session.getAttribute("role");
        if (role == null) {
            role = "ADMIN"; // Default fallback
        }
        model.addAttribute("userRole", role);
        return "tutor/list";
    }

    @GetMapping("/add")
    public String addTutorForm(Model model) {
        model.addAttribute("tutor", new Tutor());
        return "tutor/add";
    }

    @PostMapping("/add")
    public String addTutorSubmit(@ModelAttribute Tutor tutor) {
        tutor.setRole("TUTOR");
        tutorService.saveTutor(tutor);
        return "redirect:/tutor/list";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null || !"TUTOR".equals(session.getAttribute("role"))) return "redirect:/login";
        model.addAttribute("tutor", tutorService.getTutorById(userId));
        return "tutor/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute Tutor updatedTutor, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null || !"TUTOR".equals(session.getAttribute("role"))) return "redirect:/login";
        updatedTutor.setUserId(userId);
        updatedTutor.setRole("TUTOR");
        tutorService.updateTutor(updatedTutor);
        session.setAttribute("user", updatedTutor);
        return "redirect:/dashboard/tutor";
    }

    @GetMapping("/courses")
    public String myCourses(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null || !"TUTOR".equals(session.getAttribute("role"))) return "redirect:/login";
        model.addAttribute("courses", courseService.getCoursesByTutor(userId));
        return "tutor/courses";
    }

    @GetMapping("/course/add")
    public String addCourseForm(Model model, HttpSession session) {
        if (!"TUTOR".equals(session.getAttribute("role"))) return "redirect:/login";
        model.addAttribute("course", new Course());
        return "tutor/add-course";
    }

    @PostMapping("/course/add")
    public String addCourseSubmit(@ModelAttribute Course course, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null || !"TUTOR".equals(session.getAttribute("role"))) return "redirect:/login";
        course.setTutorId(userId);
        courseService.saveCourse(course);
        return "redirect:/tutor/courses";
    }

    @GetMapping("/course/{courseId}/students")
    public String viewEnrolledStudents(@PathVariable String courseId, HttpSession session, Model model) {
        if (!"TUTOR".equals(session.getAttribute("role"))) return "redirect:/login";
        List<Enrollment> enrollments = courseService.getEnrollmentsByCourse(courseId);
        List<Student> students = enrollments.stream()
                .map(e -> studentService.getStudentById(e.getStudentId()))
                .collect(Collectors.toList());
        model.addAttribute("students", students);
        return "tutor/enrolled-students";
    }

    @PostMapping("/course/{courseId}/delete")
    public String deleteCourse(@PathVariable String courseId, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null || !"TUTOR".equals(session.getAttribute("role"))) return "redirect:/login";
        // Safety check: only delete if this tutor owns the course
        Course course = courseService.getCourseById(courseId);
        if (course != null && userId.equals(course.getTutorId())) {
            courseService.deleteCourse(courseId);
        }
        return "redirect:/tutor/courses";
    }
}

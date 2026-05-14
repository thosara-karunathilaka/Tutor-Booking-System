package com.tbs.controller;

import com.tbs.model.Student;
import com.tbs.model.Tutor;
import com.tbs.model.User;
import com.tbs.service.StudentService;
import com.tbs.service.TutorService;
import com.tbs.util.FileHandler;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final StudentService studentService;
    private final TutorService tutorService;

    public AdminController(StudentService studentService, TutorService tutorService) {
        this.studentService = studentService;
        this.tutorService = tutorService;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        model.addAttribute("user", user);
        return "admin/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User updatedUser, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        
        updatedUser.setUserId(user.getUserId());
        updatedUser.setRole("ADMIN");
        FileHandler.updateUser(updatedUser);
        session.setAttribute("user", updatedUser); // update session
        return "redirect:/dashboard/admin";
    }

    @GetMapping("/student/edit/{id}")
    public String editStudent(@PathVariable String id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "admin/edit-student";
    }

    @PostMapping("/student/edit")
    public String updateStudentSubmit(@ModelAttribute Student student, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        student.setRole("STUDENT");
        studentService.updateStudent(student);
        return "redirect:/dashboard/admin";
    }

    @GetMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable String id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        studentService.deleteStudent(id);
        return "redirect:/dashboard/admin";
    }

    @GetMapping("/tutor/edit/{id}")
    public String editTutor(@PathVariable String id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        Tutor tutor = tutorService.getTutorById(id);
        model.addAttribute("tutor", tutor);
        return "admin/edit-tutor";
    }

    @PostMapping("/tutor/edit")
    public String updateTutorSubmit(@ModelAttribute Tutor tutor, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        tutor.setRole("TUTOR");
        tutorService.updateTutor(tutor);
        return "redirect:/dashboard/admin";
    }

    @GetMapping("/tutor/delete/{id}")
    public String deleteTutor(@PathVariable String id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        tutorService.deleteTutor(id);
        return "redirect:/dashboard/admin";
    }

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "ADMIN".equals(role);
    }
}

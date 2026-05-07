package com.tbs.controller;

import com.tbs.model.Tutor;
import com.tbs.service.TutorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tutor")
public class TutorController {

    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
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
}

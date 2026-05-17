package com.tbs.controller;

import com.tbs.service.CourseService;
import com.tbs.service.TutorService;
import com.tbs.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("tutorCount", tutorService.getAllTutors().size());
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        return "index";
    }
}

package com.tbs.controller;

import com.tbs.model.User;
import com.tbs.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(String username, String password, Model model, HttpSession session) {
        User user = authService.login(username, password);
        if (user != null) {
            String role = user.getRole();
            session.setAttribute("role", role);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("user", user);
            if ("ADMIN".equalsIgnoreCase(role)) return "redirect:/dashboard/admin";
            if ("TUTOR".equalsIgnoreCase(role)) return "redirect:/dashboard/tutor";
            if ("STUDENT".equalsIgnoreCase(role)) return "redirect:/dashboard/student";
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute User user) {
        authService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

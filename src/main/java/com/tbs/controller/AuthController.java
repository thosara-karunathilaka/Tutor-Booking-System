package com.tbs.controller;

import com.tbs.model.User;
import com.tbs.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ── Helper: redirect already-logged-in users to their dashboard ──────────
    private String getDashboardForRole(String role) {
        if ("ADMIN".equalsIgnoreCase(role))   return "redirect:/dashboard/admin";
        if ("TUTOR".equalsIgnoreCase(role))   return "redirect:/dashboard/tutor";
        if ("STUDENT".equalsIgnoreCase(role)) return "redirect:/dashboard/student";
        return null;
    }

    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        // If already logged in, skip the login page entirely
        String role = (String) session.getAttribute("role");
        String redirect = getDashboardForRole(role);
        if (redirect != null) return redirect;
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(String username, String password, Model model,
                              HttpSession session, RedirectAttributes redirectAttrs) {
        User user = authService.login(username, password);
        if (user != null) {
            String role = user.getRole();
            session.setAttribute("role", role);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("user", user);
            // Flash welcome message shown on dashboard after login
            redirectAttrs.addFlashAttribute("flashSuccess", "Welcome back, " + user.getName() + "! 👋");
            String redirect = getDashboardForRole(role);
            if (redirect != null) return redirect;
        }
        model.addAttribute("error", "Invalid username or password. Please try again.");
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(HttpSession session, Model model) {
        // If already logged in, skip the register page
        String role = (String) session.getAttribute("role");
        String redirect = getDashboardForRole(role);
        if (redirect != null) return redirect;
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute User user, RedirectAttributes redirectAttrs) {
        authService.registerUser(user);
        // Flash message shown on the login page after successful registration
        redirectAttrs.addFlashAttribute("flashSuccess", "Account created! Please log in.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttrs) {
        session.invalidate();
        redirectAttrs.addFlashAttribute("flashSuccess", "You have been logged out successfully.");
        return "redirect:/login";
    }
}

package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.LoginDto;
import com.project_agh.payrollmanagementsystem.entities.User;
import com.project_agh.payrollmanagementsystem.service.AuthService;
import jakarta.servlet.http.HttpSession; // Used to manually manage the session
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

// Controller for handling authentication-related requests (like login)
@Controller
public class AuthController {

    private final AuthService authService; // Service component handling the login logic

    // Constructor for dependency injection of AuthService
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles POST requests to the /login URL, processing user login attempts.
     * * NOTE: This method implements **manual session management** by storing
     * the user ID directly in the HttpSession, which is generally **discouraged** * when using Spring Security for proper session and context management.
     * * @param dto LoginDto containing the submitted email and password.
     * @param session The current HTTP session to store user details.
     * @return Redirects to /dashboard on success, or back to /login with an error.
     */
    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto dto, HttpSession session) {

        // Call the AuthService to validate credentials (email and password)
        User user = authService.login(dto.getEmail(), dto.getPassword());

        // Check if the login was unsuccessful (service returned null)
        if (user == null) {
            // Redirect back to the login page, adding an 'error' query parameter
            return "redirect:/login?error";
        }

        // --- MANUAL SESSION MANAGEMENT (Security Risk) ---
        // Store the user's ID in the HTTP session. This approach bypasses
        // standard Spring Security context setup.
        session.setAttribute("userId", user.getId());
        // ------------------------------------------------

        // Redirect the successfully authenticated user to the dashboard
        return "redirect:/dashboard";
    }
}
package com.project_agh.payrollmanagementsystem.service.impl;

import com.project_agh.payrollmanagementsystem.entities.User;
import com.project_agh.payrollmanagementsystem.repositories.UserRepository;
import com.project_agh.payrollmanagementsystem.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Implementation of the AuthService interface
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository; // Repository for accessing User data
    private final PasswordEncoder encoder;      // Utility for encoding and verifying passwords

    // Constructor for dependency injection of required components
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    /**
     * Attempts to authenticate a user by checking their email and password.
     * * This method uses the injected PasswordEncoder to securely compare
     * the provided raw password with the stored hashed password.
     * * @param email The user's email address.
     * @param password The raw password provided by the user during login.
     * @return The authenticated User object if credentials are valid,
     * or null if the user is not found or the password does not match.
     */
    @Override
    public User login(String email, String password) {

        // 1. Find the user in the database by their email.
        // findByEmail returns an Optional<User>.
        return userRepository.findByEmail(email)
                // 2. If the user is found, use the filter to check the password.
                // The encoder.matches() method securely compares the raw password
                // with the hashed password stored in the User entity.
                .filter(u -> encoder.matches(password, u.getPassword()))
                // 3. If the filter passes (password matches), return the User object.
                // Otherwise (user not found or password mismatch), return null.
                .orElse(null);
    }
}
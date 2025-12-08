package com.project_agh.payrollmanagementsystem.service.impl;

import com.project_agh.payrollmanagementsystem.entities.User;
import com.project_agh.payrollmanagementsystem.repositories.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// This service is required by Spring Security to load user-specific data during authentication.
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // Repository to fetch User entities from the database

    // Constructor for dependency injection of UserRepository
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the username (which is the email in this application).
     * This method is called by Spring Security's Authentication Provider
     * when a user attempts to log in.
     * * @param email The username (email) provided by the user.
     * @return A UserDetails object that Spring Security uses for authentication and authorization.
     * @throws UsernameNotFoundException If the user with the given email is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Fetch the application's User entity from the database using the email.
        // The email is used as the unique identifier (username) for login.
        User user = userRepository.findByEmail(email)
                // 2. If the user is not found, throw the standard Spring Security exception.
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String userRole = user.getRole().getName();
        System.out.println("userRole: " + userRole);
        // 3. Convert the application's 'User' entity into a Spring Security 'UserDetails' object.
        return org.springframework.security.core.userdetails.User
                // Set the principal (username/email)
                .withUsername(user.getEmail())
                // Set the hashed password from the database.
                // Spring Security will automatically compare this with the provided password.
                .password(user.getPassword())
                // Assign roles/authorities. Roles should typically come from the database,
                .roles(userRole)
                .build();
    }

}
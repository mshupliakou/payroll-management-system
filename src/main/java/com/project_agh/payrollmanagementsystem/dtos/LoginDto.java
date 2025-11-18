package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

/**
 * Data Transfer Object (DTO) used for user authentication requests (login).
 * <p>
 * This class encapsulates the necessary credentials—email and password—sent
 * by the client to the server during the login process.
 * The {@code @Data} annotation from Lombok automatically provides getters, setters,
 * toString, equals, and hashCode methods.
 */
@Data
public class LoginDto {
    /**
     * The email address provided by the user, which serves as the unique login identifier.
     */
    private String email;

    /**
     * The plain-text password provided by the user.
     * This password should be verified against the stored hash in the database.
     */
    private String password;
}
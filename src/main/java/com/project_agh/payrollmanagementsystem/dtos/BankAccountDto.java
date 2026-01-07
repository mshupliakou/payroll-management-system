package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

/**
 * Data Transfer Object (DTO) responsible for handling bank account information updates.
 * <p>
 * This class encapsulates the data required when a user (employee) requests to change
 * or update their registered bank account number within the system. It is typically
 * used in the user profile management sections.
 * </p>
 */
@Data
public class BankAccountDto {

    /**
     * The new bank account number (e.g., IBAN or local account format) provided by the user.
     */
    private String account;
}
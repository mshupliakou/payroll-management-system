package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO used for creating a new user within the payroll management system.
 * <p>
 * Contains personal information, employment details,
 * salary data, and references to related entities such as role,
 * position, and department.
 * </p>
 */
@Data
public class CreateUserDto {
    Long id;

    /**
     * User's first name.
     */
    private String name;

    /**
     * User's last name.
     */
    private String lastname;

    /**
     * User's email address used for login and communication.
     */
    private String email;

    /**
     * Raw (unencoded) password entered during user creation.
     * Will be encoded before storing in the database.
     */
    private String password;

    /**
     * Gross salary of the user, expressed in PLN.
     */
    private BigDecimal wynagrodzenie_pln_g;

    /**
     * Date when the user was hired.
     */
    private LocalDate data_zatrudnienia;

    /**
     * Determines whether the user account is active.
     * <p>
     * Uses {@link Boolean} (not primitive) to handle form binding correctly.
     * </p>
     */
    private Boolean aktywny;

    /**
     * Identifier of the user's role.
     * Must correspond to an existing role record.
     */
    private Long id_rola;

    /**
     * Identifier of the user's job position.
     */
    private Long id_stanowisko;

    /**
     * Identifier of the department the user belongs to.
     */
    private Long id_dzial;

    private  String account;
}

package com.project_agh.payrollmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents an employee (User) in the Payroll Management System.
 * <p>
 * This JPA entity maps to the "pracownik" table and stores personal,
 * organizational, and access details for each staff member. It establishes
 * relationships with the Position, Role, and Department entities.
 */
@Entity
@Table(name = "pracownik")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * The unique identifier for the employee.
     * Mapped to the "id_pracownik" column and serves as the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pracownik")
    private Long id;

    /**
     * The first name of the employee. Mapped to the "imie" column.
     */
    @Column(name = "imie", nullable = false)
    private String name;

    /**
     * The last name (surname) of the employee. Mapped to the "nazwisko" column.
     */
    @Column(name = "nazwisko", nullable = false)
    private String lastname;

    /**
     * The job position held by the employee.
     * Established as a Many-to-One relationship via the "id_stanowisko" foreign key.
     */
    @ManyToOne
    @JoinColumn(name = "id_stanowisko", nullable = false)
    private Position position;

    /**
     * The security role assigned to the employee (for system access/permissions).
     * Established as a Many-to-One relationship via the "id_rola" foreign key.
     */
    @ManyToOne
    @JoinColumn(name = "id_rola", nullable = false)
    private Role role;

    /**
     * The department to which the employee belongs.
     * Established as a Many-to-One relationship via the "id_dzial" foreign key.
     */
    @ManyToOne
    @JoinColumn(name = "id_dzial", nullable = false)
    private Department department;

    /**
     * The gross hourly salary of the employee in PLN (Polish złoty).
     * Mapped to the "wynagrodzenie_pln_g" column.
     */
    @Column(name = "wynagrodzenie_pln_g", nullable = false)
    private BigDecimal salary_pln_h;

    /**
     * The unique email address used for contact and login. Mapped to the "email" column.
     * Must be unique across all employees.
     */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * The employee's phone number. Mapped to the "telefon" column.
     * This field is unique but optional (nullable = true).
     */
    @Column(name = "telefon", unique = true)
    private String phone_number;

    /**
     * The hashed password used for system authentication. Mapped to the "haslo_hash" column.
     * Stored as a hash for security purposes.
     */
    @Column(name = "haslo_hash", nullable = false)
    private String password;

    /**
     * The official date the employee started their employment. Mapped to the "data_zatrudnienia" column.
     */
    @Column(name = "data_zatrudnienia", nullable = false)
    private LocalDate hireDate;

    /**
     * The official date the employee's employment ended (if applicable).
     * Mapped to the "data_zwolnienia" column and is optional (nullable = true).
     */
    @Column(name = "data_zwolnienia")
    private LocalDate retirementDate;

    /**
     * Flag indicating the current employment status. True if the employee is currently active/employed.
     * Mapped to the "aktywny" column.
     */
    @Column(name = "aktywny", nullable = false)
    private boolean active;

}
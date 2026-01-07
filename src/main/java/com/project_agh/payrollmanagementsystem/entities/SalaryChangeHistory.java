package com.project_agh.payrollmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing a historical record of salary adjustments for an employee.
 * <p>
 * This class maps to the database table {@code historia_zmian_wynagrodzen}. It serves as
 * an audit trail, preserving data regarding previous compensation levels, the new amounts,
 * and the reasons for the changes.
 * </p>
 */
@Entity
@Table(name = "historia_zmian_wynagrodzen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryChangeHistory {

    /**
     * The unique identifier for this salary change record.
     * <p>
     * Generated automatically using the identity strategy. Maps to the {@code id_zmiany_wynagrodzenia} column.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zmiany_wynagrodzenia")
    private Long id;

    /**
     * The employee (User) associated with this salary change.
     * <p>
     * Establishes a Many-to-One relationship with the {@link User} entity.
     * Maps to the {@code id_pracownik} foreign key column.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "id_pracownik")
    private User user;

    /**
     * The monetary value of the employee's salary *before* this adjustment.
     * <p>
     * Maps to the {@code stare_wynagr} column.
     * </p>
     */
    @Column(name = "stare_wynagr")
    private BigDecimal oldSalary;

    /**
     * The monetary value of the employee's salary *after* this adjustment.
     * <p>
     * Maps to the {@code nowe_wynagr} column.
     * </p>
     */
    @Column(name = "nowe_wynagr")
    private BigDecimal newSalary;

    /**
     * The date on which the salary change was recorded or became effective.
     * <p>
     * Maps to the {@code data} column.
     * </p>
     */
    @Column(name = "data")
    private LocalDate date;

    /**
     * A textual description or reason for the salary change (e.g., "Annual Raise", "Promotion").
     * <p>
     * Maps to the {@code opis} column.
     * </p>
     */
    @Column(name = "opis")
    private String description;

}
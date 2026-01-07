package com.project_agh.payrollmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing a financial payment record within the system.
 * <p>
 * This class maps to the database table {@code historia_wyplat} (Payout History).
 * It serves as a historical log of all financial transfers made to employees,
 * including base salaries, bonuses, and other forms of compensation.
 * </p>
 */
@Entity
@Table(name = "historia_wyplat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    /**
     * The unique identifier for this payment record.
     * <p>
     * Generated automatically using the identity strategy. Maps to the {@code id_wyplata} column.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_wyplata")
    private Long id;

    /**
     * The employee (User) who is the recipient of this payment.
     * <p>
     * Establishes a Many-to-One relationship with the {@link User} entity.
     * Maps to the {@code id_pracownik} foreign key column.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "id_pracownik", nullable = false)
    private User user;

    /**
     * The date on which the payment was processed or is scheduled to be processed.
     * Maps to the {@code data} column.
     */
    @Column(name = "data", nullable = false)
    private LocalDate date;

    /**
     * The monetary value of the payment.
     * <p>
     * Uses {@link BigDecimal} to ensure arithmetic precision for financial calculations.
     * Maps to the {@code wyplata} column.
     * </p>
     */
    @Column(name = "wyplata")
    private BigDecimal amount;

    /**
     * The current status of the payment (e.g., Pending, Approved, Paid).
     * <p>
     * Maps to the {@code id_status_wyplaty} foreign key column.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "id_status_wyplaty")
    private PaymentStatus paymentStatus;

    /**
     * The classification of the payment (e.g., Base Salary, Overtime, Bonus).
     * <p>
     * Maps to the {@code id_typ_wyplaty} foreign key column.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "id_typ_wyplaty")
    private PaymentType paymentType;

    /**
     * An optional text description or note regarding the payment details.
     * Maps to the {@code opis} column.
     */
    @Column(name = "opis")
    private String description;

}
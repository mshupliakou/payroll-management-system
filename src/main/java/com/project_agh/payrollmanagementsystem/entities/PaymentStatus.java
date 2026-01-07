package com.project_agh.payrollmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing the status lifecycle of a payment transaction.
 * <p>
 * This class maps to the database table {@code status_wyplaty}. It functions as a dictionary
 * entity defining the various states a payment can be in (e.g., "Pending", "Approved", "Rejected").
 * </p>
 */
@Entity
@Table(name = "status_wyplaty")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatus {

    /**
     * The unique identifier of the payment status record.
     * <p>
     * Generated automatically using the identity strategy. Maps to the {@code id_status_wyplaty} column.
     * </p>
     */
    @Id
    @Column(name = "id_status_wyplaty", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique display name of the status (e.g., "Pending", "Completed").
     * <p>
     * Maps to the {@code nazwa} column.
     * </p>
     */
    @Column(name = "nazwa", unique = true)
    String name;

    /**
     * A brief description explaining the meaning and implication of this status.
     * <p>
     * Maps to the {@code opis} column.
     * </p>
     */
    @Column(name = "opis")
    String description;

}
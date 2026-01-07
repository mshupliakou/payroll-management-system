package com.project_agh.payrollmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a specific category or classification of financial payment.
 * <p>
 * This class maps to the database table {@code typ_wyplaty}. It functions as a dictionary
 * entity used to categorize payments (e.g., "Regular Salary", "Bonus", "Severance Pay").
 * </p>
 */
@Entity
@Table(name = "typ_wyplaty")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentType {

    /**
     * The unique identifier of the payment type record.
     * <p>
     * Generated automatically using the identity strategy. Maps to the {@code id_typ_wyplaty} column.
     * </p>
     */
    @Id
    @Column(name = "id_typ_wyplaty", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique display name of the payment type (e.g., "Bonus", "Salary").
     * <p>
     * Maps to the {@code nazwa} column.
     * </p>
     */
    @Column(name = "nazwa", unique = true)
    String name;

    /**
     * A brief description explaining the nature or purpose of this payment type.
     * <p>
     * Maps to the {@code opis} column.
     * </p>
     */
    @Column(name = "opis")
    String description;

}
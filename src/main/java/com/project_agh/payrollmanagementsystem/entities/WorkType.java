package com.project_agh.payrollmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a classification or category of work performed by an employee.
 * <p>
 * This class maps to the database table {@code typ_pracy}. It functions as a dictionary
 * entity used to define the nature of the work log (e.g., "Remote Work", "Office",
 * "Business Trip"). These types are referenced when users register their work hours.
 * </p>
 */
@Entity
@Table(name = "typ_pracy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkType {

    /**
     * The unique identifier of the work type record.
     * <p>
     * Generated automatically using the identity strategy. Maps to the {@code id_typ_pracy} column.
     * </p>
     */
    @Id
    @Column(name = "id_typ_pracy", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique display name of the work type (e.g., "Remote", "Office").
     * <p>
     * Maps to the {@code nazwa} column.
     * </p>
     */
    @Column(name = "nazwa", unique = true)
    private String name;

    /**
     * A brief description explaining the rules or context of this work type.
     * <p>
     * Maps to the {@code opis} column.
     * </p>
     */
    @Column(name = "opis")
    private String description;
}
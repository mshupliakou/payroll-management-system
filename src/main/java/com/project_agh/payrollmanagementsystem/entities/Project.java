package com.project_agh.payrollmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a business project within the organization.
 * <p>
 * This class maps to the database table {@code projekt}. It encapsulates the lifecycle details
 * of a project (start and end dates) and serves as a container for assigning employees
 * and tracking work hours.
 * </p>
 */
@Entity
@Table(name = "projekt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    /**
     * The unique identifier of the project.
     * <p>
     * Generated automatically using the identity strategy. Maps to the {@code id_projekt} column.
     * </p>
     */
    @Id
    @Column(name = "id_projekt", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The official name or title of the project.
     * <p>
     * Maps to the {@code nazwa} column.
     * </p>
     */
    @Column(name = "nazwa")
    private String name;

    /**
     * A brief description outlining the goals, scope, or details of the project.
     * <p>
     * Maps to the {@code opis} column.
     * </p>
     */
    @Column(name = "opis")
    private String description;

    /**
     * The date on which the project officially begins.
     * <p>
     * Maps to the {@code data_rozpoczecia} column.
     * </p>
     */
    @Column(name = "data_rozpoczecia")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectBeginDate;

    /**
     * The expected or actual date of project completion.
     * <p>
     * Maps to the {@code data_zakonczenia} column.
     * </p>
     */
    @Column(name = "data_zakonczenia")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEndDate;

    /**
     * A temporary list of members assigned to this project.
     * <p>
     * Marked as {@link Transient}, meaning this field is not directly mapped to a database column
     * by JPA in this entity. It is typically populated manually by the service layer or
     * a custom query when detailed member information is required for the view.
     * </p>
     */
    @Transient
    private List<ProjectMember> members = new ArrayList<>();

    /**
     * Checks if a specific user is currently assigned to this project.
     * <p>
     * This utility method iterates through the {@link #members} list to find a match
     * based on the user's ID.
     * </p>
     *
     * @param userId the unique identifier of the user to check
     * @return {@code true} if the user is found in the members list; {@code false} otherwise
     */
    public boolean hasMember(Long userId) {
        return members.stream().anyMatch(m -> m.getUser().getId().equals(userId));
    }

}
package com.project_agh.payrollmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity representing a record of work hours logged by an employee.
 * <p>
 * This class maps to the database table {@code rejestracja_godzin_pracy}. It tracks
 * the duration of work performed by a user on a specific date, optionally linked to a project,
 * along with the type of work and approval status.
 * </p>
 */
@Entity
@Table(name = "rejestracja_godzin_pracy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkHours {

    /**
     * The unique identifier for this work registration record.
     * <p>
     * Generated automatically using the identity strategy. Maps to the {@code id_rejestracji} column.
     * </p>
     */
    @Id
    @Column(name = "id_rejestracji", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The employee (User) who performed the work.
     * <p>
     * Establishes a Many-to-One relationship with the {@link User} entity.
     * Maps to the {@code id_pracownik} foreign key column.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "id_pracownik")
    private User user;

    /**
     * The calendar date on which the work was performed.
     * <p>
     * Maps to the {@code data} column.
     * </p>
     */
    @Column(name = "data")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * The specific project associated with this work session.
     * <p>
     * This may be null if the work performed was general and not tied to a specific project.
     * Maps to the {@code id_projekt} foreign key column.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "id_projekt")
    private Project project;

    /**
     * The category of work performed (e.g., Remote, Office, Business Trip).
     * <p>
     * Maps to the {@code id_typ_pracy} foreign key column.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "id_typ_pracy", nullable = false)
    private WorkType workType;

    /**
     * The time of day the work session began.
     * <p>
     * Maps to the {@code godzina_rozpoczecia} column.
     * </p>
     */
    @Column(name = "godzina_rozpoczecia")
    private LocalTime startTime;

    /**
     * The time of day the work session ended.
     * <p>
     * Maps to the {@code godzina_zakonczenia} column.
     * </p>
     */
    @Column(name = "godzina_zakonczenia")
    private LocalTime endTime;

    /**
     * Optional notes or description regarding the tasks performed.
     * <p>
     * Maps to the {@code komentarz} column.
     * </p>
     */
    @Column(name = "komentarz")
    private String comment;

    /**
     * Indicates whether this work record has been approved by a manager or administrator.
     * <p>
     * Typically used to lock the record before payroll processing.
     * Maps to the {@code zatwierdzenie} column.
     * </p>
     */
    @Column(name = "zatwierdzenie")
    private boolean approved;

    // --- Transient Fields (Not persisted in the database) ---

    /**
     * The name of the day of the week (e.g., "Monday") corresponding to the {@code date}.
     * <p>
     * Marked as {@link Transient}, meaning it is calculated for display purposes and not stored in the database.
     * </p>
     */
    @Transient
    private String dayOfWeek;

    /**
     * The calculated duration of work (e.g., "8h 00m").
     * <p>
     * Marked as {@link Transient}. This value is derived from the difference between
     * {@code startTime} and {@code endTime}.
     * </p>
     */
    @Transient
    private String workedTime;

    /**
     * A string representation of the week range this date falls into (e.g., "Oct 1 - Oct 7").
     * <p>
     * Marked as {@link Transient}. Used for grouping records in the user interface.
     * </p>
     */
    @Transient
    private String weekRange;

}
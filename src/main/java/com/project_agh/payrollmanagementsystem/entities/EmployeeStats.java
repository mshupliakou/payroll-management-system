package com.project_agh.payrollmanagementsystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

/**
 * Entity representing a read-only view of aggregated employee work statistics.
 * <p>
 * This class maps to the database view {@code widok_statystyki_pracownika}.
 * It provides pre-calculated metrics such as total hours worked and weekly averages,
 * allowing for efficient data retrieval without complex application-side logic.
 * As this entity represents a view, it is marked as {@link Immutable}.
 * </p>
 */
@Entity
@Immutable
@Table(name = "widok_statystyki_pracownika")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeStats {

    /**
     * The unique identifier of the employee to whom these statistics belong.
     * Maps to the {@code id_pracownik} column.
     */
    @Id
    @Column(name = "id_pracownik")
    private Long id;

    /**
     * The total accumulated work hours for the employee across all projects.
     * Maps to the {@code suma_calkowita} column.
     */
    @Column(name = "suma_calkowita")
    private Double totalHours;

    /**
     * The count of distinct weeks in which the employee has recorded work activity.
     * Maps to the {@code liczba_tygodni} column.
     */
    @Column(name = "liczba_tygodni")
    private Integer weeksCount;

    /**
     * The average number of work hours per active week.
     * Maps to the {@code srednia_tygodniowa} column.
     */
    @Column(name = "srednia_tygodniowa")
    private Double averageWeeklyHours;
}
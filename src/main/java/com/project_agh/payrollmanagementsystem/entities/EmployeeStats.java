package com.project_agh.payrollmanagementsystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "widok_statystyki_pracownika")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeStats {

    @Id
    @Column(name = "id_pracownik")
    private Long id;

    @Column(name = "suma_calkowita")
    private Double totalHours;

    @Column(name = "liczba_tygodni")
    private Integer weeksCount;

    @Column(name = "srednia_tygodniowa")
    private Double averageWeeklyHours;
}
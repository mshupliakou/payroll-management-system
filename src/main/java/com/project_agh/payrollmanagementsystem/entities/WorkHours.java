package com.project_agh.payrollmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "rejestracja_godzin_pracy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkHours {
    @Id
    @Column(name = "id_rejestracji", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pracownik")
    private User user;

    @Column(name = "data")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "id_projekt")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "id_typ_pracy", nullable = false)
    private WorkType workType;

    @Column(name = "godzina_rozpoczecia")
    private LocalTime startTime;    // godzina_rozpoczecia

    @Column(name = "godzina_zakonczenia")
    private LocalTime endTime;      // godzina_zakonczenia

    @Column(name = "komentarz")
    private String comment;         // komentarz

    @Column(name = "zatwierdzenie")
    private boolean approved;       // zatwierdzenie

    @Transient
    private String dayOfWeek;

    @Transient
    private String workedTime;

    @Transient
    private String weekRange;

}

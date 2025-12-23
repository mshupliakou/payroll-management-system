package com.project_agh.payrollmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "historia_wyplat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_wyplata")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pracownik", nullable = false)
    private User user;

    @Column(name = "data", nullable = false)
    private LocalDate date;

    @Column(name = "wyplata")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "id_status_wyplaty")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "id_typ_wyplaty")
    private PaymentType paymentType;

    @Column(name = "opis")
    private String description;

}

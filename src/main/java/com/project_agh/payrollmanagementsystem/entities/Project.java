package com.project_agh.payrollmanagementsystem.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "projekt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @Column(name = "id_projekt", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nazwa")
    private String name;

    @Column(name = "opis")
    private String description;

    @Column(name = "data_rozpoczecia")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectBeginDate;

    @Column(name = "data_zakonczenia")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEndDate;

    @Transient
    private List<ProjectMember> members = new ArrayList<>();
    public boolean hasMember(Long userId) {
        return members.stream().anyMatch(m -> m.getUser().getId().equals(userId));
    }

}

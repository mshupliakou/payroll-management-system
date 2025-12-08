package com.project_agh.payrollmanagementsystem.dtos;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ProjectDto {
    private Long id;
    private String name;
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectBeginDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEndDate;

}

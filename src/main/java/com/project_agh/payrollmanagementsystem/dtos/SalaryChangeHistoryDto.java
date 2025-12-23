package com.project_agh.payrollmanagementsystem.dtos;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SalaryChangeHistoryDto {
    private Long id;
    private Long userId;
    private BigDecimal oldSalary;
    private BigDecimal newSalary;
    private LocalDate date;
    private String description;
}

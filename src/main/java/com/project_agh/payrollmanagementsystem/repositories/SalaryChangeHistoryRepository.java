package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.SalaryChangeHistory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalaryChangeHistoryRepository {
    void changeSalary(Long userId, BigDecimal oldSalary, BigDecimal newSalary, LocalDate date);
    List<SalaryChangeHistory> findAll();
}

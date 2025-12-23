package com.project_agh.payrollmanagementsystem.service.impl;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
public class AutoPayoutService {

    private final JdbcTemplate jdbcTemplate;

    public AutoPayoutService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    @Transactional
    public void triggerMonthlyPayoutProcedure() {
        System.out.println("Java Scheduler: Liczą się wypłaty...");

        LocalDate today = LocalDate.now();
        LocalDate startOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        try {
            String sql = "CALL generuj_wyplaty_za_miesiac(?, ?)";

            jdbcTemplate.update(sql, startOfLastMonth, endOfLastMonth);

            System.out.println("Java Scheduler: Success");

        } catch (Exception e) {
            System.err.println("Java Scheduler: Błąd " + e.getMessage());
            e.printStackTrace();
        }
    }


    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup() {
        triggerMonthlyPayoutProcedure();
    }
}
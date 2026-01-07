package com.project_agh.payrollmanagementsystem.service.impl;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * Service responsible for automating the payroll generation process.
 * <p>
 * This service triggers the database-level stored procedure that calculates salaries
 * for all employees. It is configured to run automatically on a monthly schedule
 * and can also be triggered upon application startup for testing or initialization purposes.
 * </p>
 */
@Service
public class AutoPayoutService {

    private final JdbcTemplate jdbcTemplate;

    public AutoPayoutService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Triggers the monthly payroll calculation procedure.
     * <p>
     * This method is scheduled to run at <b>01:00 AM on the 1st day of every month</b>.
     * It determines the date range for the <i>previous month</i> (first day to last day)
     * and executes the SQL stored procedure {@code generuj_wyplaty_za_miesiac}.
     * </p>
     * <p>
     * <b>Note:</b> The actual salary logic (taxes, bonuses, deductions) is encapsulated
     * within the database procedure to ensure transactional integrity and performance.
     * </p>
     */
    @Scheduled(cron = "0 0 1 1 * ?")
    @Transactional
    public void triggerMonthlyPayoutProcedure() {
        System.out.println("Java Scheduler: Calculating monthly payouts...");

        LocalDate today = LocalDate.now();
        // Calculate the range for the previous month
        LocalDate startOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        try {
            // Call the database stored procedure
            String sql = "CALL generuj_wyplaty_za_miesiac(?, ?)";

            jdbcTemplate.update(sql, startOfLastMonth, endOfLastMonth);

            System.out.println("Java Scheduler: Success - Payouts generated.");

        } catch (Exception e) {
            System.err.println("Java Scheduler: Error generating payouts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Event listener that triggers the payout procedure immediately upon application startup.
     * <p>
     * This is useful for development environments or ensuring pending calculations are processed
     * if the server was down during the scheduled cron execution.
     * </p>
     */
    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup() {
        triggerMonthlyPayoutProcedure();
    }
}
package com.project_agh.payrollmanagementsystem.repositories;
import com.project_agh.payrollmanagementsystem.entities.WorkHours;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface WorkHoursRepository {
    void createWorkHoursWithProject(Long currentUserId, LocalDate date, Long workType, Long projectId, LocalTime startTime, LocalTime endTime, String comment);
    void createWorkHours(Long currentUserId, LocalDate date, Long workType, LocalTime startTime, LocalTime endTime, String comment);
    List<WorkHours> findByUserId(Long userId);
    List<WorkHours> findAll();
    void deleteWorkHours(Long id);
    void editWorkHours(Long id,Long currentUserId, LocalDate date, Long workType, LocalTime startTime, LocalTime endTime, String comment);

    List<WorkHours> findByUserIdAndDateRange(Long id, LocalDate startOfWeek, LocalDate endOfWeek);
    List<WorkHours> findByDateRange( LocalDate startOfWeek, LocalDate endOfWeek);

    void approveWorkHours(Long id);
}

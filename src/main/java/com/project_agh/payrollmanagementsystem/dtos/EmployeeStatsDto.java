package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

@Data
public class EmployeeStatsDto {
    Long employeeId;
    int sumAll;
    int weekAmount;
    int weekHoursAverage;

}

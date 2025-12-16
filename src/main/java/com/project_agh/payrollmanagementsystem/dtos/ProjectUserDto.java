package com.project_agh.payrollmanagementsystem.dtos;

import com.project_agh.payrollmanagementsystem.entities.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectUserDto {
    Long user_id;
    Long project_id;
    String project_role;
    LocalDate project_start_date;
    private User user;
}

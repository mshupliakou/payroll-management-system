package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

/**
 * DTO used for creating a new department in the system.
 * <p>
 * Contains basic information required to define a department:
 * its name and a textual description.
 * </p>
 */
@Data
public class CreateDepartmentDto {
    private Long id;

    /**s
     * The name of the department.
     * Should be unique and clearly identify the department.
     */
    private String department_name;

    /**
     * A short textual description of the department.
     * Used to give more context about the department's purpose or responsibilities.
     */
    private String department_desc;
}

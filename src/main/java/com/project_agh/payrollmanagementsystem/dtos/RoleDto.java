package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a user role within the system's security context.
 * <p>
 * This class encapsulates the data required to define a security role (e.g., "ROLE_ADMIN",
 * "ROLE_USER"). It is used primarily for transferring role information between the
 * client-side administration panels and the server-side persistence layer.
 * </p>
 */
@Data
public class RoleDto {

    /**
     * The unique identifier of the role record in the database.
     */
    private Long id;

    /**
     * The name of the role (e.g., "ADMIN", "ACCOUNTANT").
     * <p>
     * This string typically corresponds to the authority granted to users holding this role.
     * </p>
     */
    private String name;

}
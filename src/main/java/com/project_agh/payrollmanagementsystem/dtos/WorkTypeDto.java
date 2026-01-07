package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a specific category or classification of work.
 * <p>
 * This class encapsulates the definition of a work type (e.g., "Remote", "Office",
 * "Business Trip"). It is primarily used for data transport between client-side
 * administration forms and the server-side persistence layer.
 * </p>
 */
@Data
public class WorkTypeDto {

    /**
     * The unique identifier of the work type record.
     */
    private Long id;

    /**
     * The display name of the work type (e.g., "Remote", "Office").
     */
    private String name;

    /**
     * A brief description explaining the nature or rules associated with this specific work type.
     */
    private String description;

}
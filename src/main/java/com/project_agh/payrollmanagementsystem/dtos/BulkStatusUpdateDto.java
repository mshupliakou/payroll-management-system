package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) designed to facilitate bulk status update operations.
 * <p>
 * This class is used to transfer a list of entity identifiers (e.g., payment IDs)
 * along with a single target status ID. It allows the system to update the status
 * of multiple records in a single transaction (batch processing).
 * </p>
 */
@Data
public class BulkStatusUpdateDto {

    /**
     * A list of unique identifiers for the entities (e.g., payments) that require a status update.
     */
    private List<Long> ids;

    /**
     * The unique identifier of the target status to be applied to all the entities
     * specified in the {@code ids} list.
     */
    private Long statusId;

    // Getters and Setters

    /**
     * Retrieves the list of IDs to be updated.
     *
     * @return a list of {@code Long} identifiers
     */
    public List<Long> getIds() { return ids; }

    /**
     * Sets the list of IDs to be updated.
     *
     * @param ids a list of {@code Long} identifiers
     */
    public void setIds(List<Long> ids) { this.ids = ids; }

    /**
     * Retrieves the target status ID.
     *
     * @return the ID of the new status
     */
    public Long getStatusId() { return statusId; }

    /**
     * Sets the target status ID.
     *
     * @param statusId the ID of the new status to apply
     */
    public void setStatusId(Long statusId) { this.statusId = statusId; }
}
package com.project_agh.payrollmanagementsystem.dtos;
import lombok.Data;

import java.util.List;

@Data
public class BulkStatusUpdateDto {
    private List<Long> ids;
    private Long statusId;

    // Getters and Setters
    public List<Long> getIds() { return ids; }
    public void setIds(List<Long> ids) { this.ids = ids; }
    public Long getStatusId() { return statusId; }
    public void setStatusId(Long statusId) { this.statusId = statusId; }
}
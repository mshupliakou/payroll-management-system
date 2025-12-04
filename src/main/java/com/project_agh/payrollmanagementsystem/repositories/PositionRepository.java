package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.Position;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing operations on {@link Position} entities.
 * <p>
 * Provides methods to retrieve job positions within the payroll management system.
 * </p>
 */
@Repository
public interface PositionRepository {

    /**
     * Retrieves all positions available in the system.
     *
     * @return a {@link List} containing all {@link Position} entities
     */
    List<Position> findAll();

    void createPosition(String position_name, String position_desc);
}

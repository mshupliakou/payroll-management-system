package com.project_agh.payrollmanagementsystem.repositories;


import com.project_agh.payrollmanagementsystem.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Position} entities.
 * <p>
 * This interface extends Spring Data JPA's {@link JpaRepository}, providing
 * standard CRUD (Create, Read, Update, Delete) operations.
 */
public interface PositionRepository extends JpaRepository<Position, Long> {

    /**
     * Retrieves an Optional container of a Position entity based on its name.
     * <p>
     * This method automatically generates the appropriate query to find a Position
     * where the 'name' field matches the given 'nazwa' parameter.
     *
     * @param nazwa The name (nazwa) of the Position to look for.
     * @return An {@link Optional} containing the matching Position, or empty if no position is found.
     */
    Optional<Position> findByName(String nazwa);
}
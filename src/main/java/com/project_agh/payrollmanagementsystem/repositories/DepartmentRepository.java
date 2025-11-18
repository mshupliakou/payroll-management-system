package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Department} entities.
 * <p>
 * This interface extends Spring Data JPA's {@link JpaRepository}, providing
 * standard CRUD (Create, Read, Update, Delete) operations and basic querying
 * capabilities for the Department entity, using {@code Long} as the type
 * of the primary key (ID).
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // Custom query methods can be defined here, following Spring Data JPA naming conventions.
    // Example:
    // Optional<Department> findByName(String name);
}
package com.project_agh.payrollmanagementsystem.repositories.jdbc;
import com.project_agh.payrollmanagementsystem.entities.Role;
import com.project_agh.payrollmanagementsystem.repositories.RoleRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcRoleRepository implements RoleRepository {

    private static final String FIND_ALL_FULL_SQL =
            "SELECT id_rola, nazwa FROM rola \n";

    private static final String CREATE_NEW_ROLE =
            "INSERT INTO rola (nazwa) VALUES (?)";

    private static final String EDIT_ROLE=
            "UPDATE rola SET " +
                    "nazwa = ? " +

                    "WHERE id_rola = ?";
    private static final String DELETE_ROLE =
            "DELETE FROM  rola \n" +
                    "WHERE id_rola = ?\n";

    private final JdbcTemplate jdbcTemplate;

    public JdbcRoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Role> roleRowMapper = (rs, rowNum) -> {
        Role role = new Role();


        role.setId(rs.getLong("id_rola"));
        role.setName(rs.getString("nazwa"));

        return role;
    };

    @Override
    public List<Role> findAll() {
        return jdbcTemplate.query(FIND_ALL_FULL_SQL, roleRowMapper);
    }

    public void createRole(String role_name ) {
        int rowsAffected = jdbcTemplate.update(CREATE_NEW_ROLE, role_name);
    }

    @Override
    public void deleteRole(Long id) {
        int rowsAffected = jdbcTemplate.update(
                DELETE_ROLE,
                id
        );

        if (rowsAffected != 1) {
        }
    }

    @Override
    public void editRole(Long id, String name) {
        int rowsAffected = jdbcTemplate.update(
                EDIT_ROLE,
                name,
                id
        );

    }
}

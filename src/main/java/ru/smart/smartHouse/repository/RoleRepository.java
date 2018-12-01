package ru.smart.smartHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.smart.smartHouse.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}

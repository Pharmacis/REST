package com.example.repository;

import com.example.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Long countRoleByName(String name);
    Role getRoleByName(String name);
    Role getRoleById(Long Id);
}

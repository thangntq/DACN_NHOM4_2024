package com.ManShirtShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ManShirtShop.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByStatus(int status);
}

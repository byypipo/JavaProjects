package com.sebatmedikal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sebatmedikal.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}

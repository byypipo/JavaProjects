package com.sebatmedikal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sebatmedikal.domain.Role;
import com.sebatmedikal.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

	User findByUsernameAndPassword(String username, String password);

	List<User> findByRole(Role role);
}

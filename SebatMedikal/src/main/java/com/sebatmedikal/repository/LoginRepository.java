package com.sebatmedikal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sebatmedikal.domain.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {
	Login findByUsernameAndEndDateIsNull(String username);
	
	Login findLastByUsername(String username);

	List<Login> readAllByEndDateNull();
}

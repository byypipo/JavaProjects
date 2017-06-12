package com.sebatmedikal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sebatmedikal.domain.OperationType;

public interface OperationTypeRepository extends JpaRepository<OperationType, Long> {
	OperationType findByOperationTypeName(String operationTypeName);

	OperationType findBySale(boolean sale);
}

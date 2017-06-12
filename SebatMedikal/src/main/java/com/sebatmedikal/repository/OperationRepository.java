package com.sebatmedikal.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sebatmedikal.domain.Operation;
import com.sebatmedikal.domain.OperationType;
import com.sebatmedikal.domain.Product;

public interface OperationRepository extends JpaRepository<Operation, Long> {
	List<Operation> findByOperationType(OperationType operationType);

	List<Operation> findByProduct(Product product);
	
	List<Operation> queryByOrderByCreatedDate(Pageable pageable);
	
	List<Operation> queryByOrderByCreatedDateDesc(Pageable pageable);
}

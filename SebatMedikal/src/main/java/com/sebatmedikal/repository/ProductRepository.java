package com.sebatmedikal.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sebatmedikal.domain.Brand;
import com.sebatmedikal.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByProductName(String productName);

	Product findByBarcod(String barcod);

	List<Product> findByBrand(Brand brand);
	
	long count();
	
	boolean exists(long id);
	
	List<Product> queryByOrderByCreatedDate(Pageable pageable);
}

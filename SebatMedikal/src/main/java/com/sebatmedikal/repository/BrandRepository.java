package com.sebatmedikal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sebatmedikal.domain.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
	Brand findByBrandName(String brandName);
}

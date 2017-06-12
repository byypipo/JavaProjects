package com.sebatmedikal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sebatmedikal.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
}

package com.sebatmedikal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sebatmedikal.domain.Product;
import com.sebatmedikal.domain.Stock;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.repository.StockRepository;

@Service
public class StockService {
	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private UserService userService;

	public void save(Stock stock) {
		stockRepository.save(stock);
	}

	public Stock createAndSaveForMe(Product product) {
		Stock stock = new Stock(product.getCreatedBy());
		stock.setCount(0);
		save(stock);
		return stock;
	}

	public Stock findById(long id) {
		return stockRepository.findOne(id);
	}

	public User getCreatedBy(Stock stock) {
		return userService.findByUsername(stock.getCreatedBy());
	}
}

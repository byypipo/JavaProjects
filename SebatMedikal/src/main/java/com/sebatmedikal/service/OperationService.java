package com.sebatmedikal.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.sebatmedikal.configuration.ErrorCodes;
import com.sebatmedikal.domain.Operation;
import com.sebatmedikal.domain.OperationType;
import com.sebatmedikal.domain.Product;
import com.sebatmedikal.domain.Stock;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.repository.OperationRepository;
import com.sebatmedikal.util.LogUtil;

@Service
public class OperationService {
	@Autowired
	private OperationRepository operationRepository;

	@Autowired
	private OperationTypeService operationTypeService;

	@Autowired
	private ProductService productService;

	@Autowired
	private StockService stockService;

	@Autowired
	private UserService userService;

	public int save(Operation operation) {
		OperationType operationType = operation.getOperationType();
		int operationCount = operation.getCount();
		if (operationType.isSale()) {
			operationCount *= -1;
		}

		Stock stock = operation.getProduct().getStock();
		int stockCount = stock.getCount();

		int afterCount = operationCount + stockCount;
		if (afterCount < 0) {
			LogUtil.logMessage(getClass(), "YETERSIZ");
			return ErrorCodes.INSUFFICIENT_STOCK;
		}

		stock.setCount(afterCount);
		stockService.save(stock);

		String totalPriceString = (operation.getCount() * operation.getProduct().getPrice().doubleValue()) + "";
		operation.setTotalPrice(new BigDecimal(totalPriceString));
		operationRepository.save(operation);
		return ErrorCodes.SUCCESSFULLY;
	}

	public void save(Iterable<Operation> operations) {
		operationRepository.save(operations);
	}

	public Operation findById(long id) {
		return operationRepository.findOne(id);
	}

	public List<Operation> getOperationPage(int begin, int size) {
		return operationRepository.queryByOrderByCreatedDateDesc(new PageRequest(begin, size));
	}

	public List<Operation> findByProduct(Product product) {
		return operationRepository.findByProduct(product);
	}

	public List<Operation> findByProductId(long id) {
		Product product = productService.findById(id);
		return operationRepository.findByProduct(product);
	}

	public List<Operation> findByOperationType(OperationType operationType) {
		return operationRepository.findByOperationType(operationType);
	}

	public List<Operation> findByOperationTypeId(long id) {
		OperationType operationType = operationTypeService.findById(id);
		return operationRepository.findByOperationType(operationType);
	}

	public List<Operation> findAll() {
		return (List<Operation>) operationRepository.findAll();
	}

	public void delete(long id) {
		operationRepository.delete(id);
	}

	public User getCreatedBy(Operation operation) {
		return userService.findByUsername(operation.getCreatedBy());
	}
}

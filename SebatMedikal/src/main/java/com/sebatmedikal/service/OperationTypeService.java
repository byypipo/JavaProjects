package com.sebatmedikal.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sebatmedikal.domain.OperationType;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.repository.OperationTypeRepository;

@Service
public class OperationTypeService {
	@Autowired
	private OperationTypeRepository operationTypeRepository;

	@Autowired
	private UserService userService;

	public void save(OperationType operationType) {
		operationTypeRepository.save(operationType);
	}

	public void save(Iterable<OperationType> operationTypes) {
		operationTypeRepository.save(operationTypes);
	}

	public OperationType findByName(String name) {
		return operationTypeRepository.findByOperationTypeName(name);
	}

	public OperationType findById(long id) {
		return operationTypeRepository.findOne(id);
	}

	public OperationType findByIsSale(Boolean sale) {
		return operationTypeRepository.findBySale(sale);
	}

	public List<OperationType> findAll() {
		return (List<OperationType>) operationTypeRepository.findAll();
	}

	public void delete(long id) {
		operationTypeRepository.delete(id);
	}

	public List<String> getOperationTypeNames() {
		List<String> operationTypeNames = new ArrayList<String>();
		Iterator<OperationType> iterator = findAll().iterator();

		while (iterator.hasNext()) {
			OperationType operationType = (OperationType) iterator.next();
			operationTypeNames.add(operationType.getOperationTypeName());
		}

		return operationTypeNames;
	}

	public User getCreatedBy(OperationType operationType) {
		return userService.findByUsername(operationType.getCreatedBy());
	}
}

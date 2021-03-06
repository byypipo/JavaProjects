package com.sebatmedikal.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sebatmedikal.UserSession;
import com.sebatmedikal.configuration.ErrorCodes;
import com.sebatmedikal.domain.Operation;
import com.sebatmedikal.domain.OperationType;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.model.request.RequestModel;
import com.sebatmedikal.model.response.ResponseModel;
import com.sebatmedikal.model.response.ResponseModelError;
import com.sebatmedikal.model.response.ResponseModelSuccess;
import com.sebatmedikal.service.OperationService;
import com.sebatmedikal.service.OperationTypeService;
import com.sebatmedikal.util.NullUtil;

@RestController
@RequestMapping("/operationTypesRest")
public class OperationTypeRestController {
	@Autowired
	private OperationService operationService;

	@Autowired
	private OperationTypeService operationTypeService;

	@Autowired
	private UserSession userSession;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseModel processGetRequest() throws IOException {
		return new ResponseModelError().setErrorCode(ErrorCodes.GET_METHOD_NOT_SUPPORTED);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseModel processPostRequest(@RequestBody RequestModel rm) {
		try {
			if (!RequestModel.isValid(rm, userSession)) {
				return new ResponseModelError().setErrorCode(ErrorCodes.BAD_REQUEST);
			}

			switch (rm.getOperation()) {
			case "findAll":
				return findAll();
			case "findOne":
				return findOne(Integer.parseInt(rm.getParameter01()));
			case "operations":
				return operations(Integer.parseInt(rm.getParameter01()));
			case "createdBy":
				return createdBy(Integer.parseInt(rm.getParameter01()));
			default:
				return new ResponseModelError().setErrorCode(ErrorCodes.UNKNOWN_OPERATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseModelError().setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
		}
	}

	public ResponseModel findAll() {
		List<OperationType> operationTypes = operationTypeService.findAll();
		return new ResponseModelSuccess().setContent(operationTypes);
	}

	public ResponseModel findOne(long id) {
		OperationType operationType = operationTypeService.findById(id);
		if (NullUtil.isNull(operationType)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.OPERATIONTYPE_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(operationType);
	}

	public ResponseModel operations(long id) {
		List<Operation> operations = operationService.findByOperationTypeId(id);
		if (NullUtil.isNull(operations)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.OPERATIONS_NOT_FOUND);
		}

		if (operations.isEmpty()) {
			return new ResponseModelError().setErrorCode(ErrorCodes.OPERATIONS_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(operations);
	}

	public ResponseModel createdBy(long id) {
		OperationType operationType = operationTypeService.findById(id);
		if (NullUtil.isNull(operationType)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.OPERATIONTYPE_NOT_FOUND);
		}

		User user = operationTypeService.getCreatedBy(operationType);
		if (NullUtil.isNull(user)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.CREATED_USER_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(user);
	}
}

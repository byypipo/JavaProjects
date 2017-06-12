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
import com.sebatmedikal.configuration.GeneralConfiguration;
import com.sebatmedikal.domain.Operation;
import com.sebatmedikal.domain.Product;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.model.request.RequestModel;
import com.sebatmedikal.model.response.ResponseModel;
import com.sebatmedikal.model.response.ResponseModelError;
import com.sebatmedikal.model.response.ResponseModelSuccess;
import com.sebatmedikal.service.OperationService;
import com.sebatmedikal.util.NullUtil;

@RestController
@RequestMapping("/operationsRest")
public class OperationRestController {
	@Autowired
	private OperationService operationService;

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
			case "page":
				return page(Integer.parseInt(rm.getParameter01()));
			case "product":
				return product(Integer.parseInt(rm.getParameter01()));
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

	private ResponseModel findAll() {
		List<Operation> operations = operationService.findAll();
		return new ResponseModelSuccess().setContent(operations);
	}

	private ResponseModel findOne(long id) {
		Operation operation = operationService.findById(id);
		if (NullUtil.isNull(operation)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.OPERATION_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(operation);
	}

	private ResponseModel page(int pageIndex) {
		List<Operation> products = operationService.getOperationPage(pageIndex, GeneralConfiguration.PAGESIZE);
		return new ResponseModelSuccess().setContent(products);
	}

	private ResponseModel product(long id) {
		Operation operation = operationService.findById(id);
		if (NullUtil.isNull(operation)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.OPERATION_NOT_FOUND);
		}

		Product product = operation.getProduct();
		if (NullUtil.isNull(product)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRODUCT_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(product);
	}

	private ResponseModel createdBy(long id) {
		Operation operation = operationService.findById(id);
		if (NullUtil.isNull(operation)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.OPERATION_NOT_FOUND);
		}

		User user = operationService.getCreatedBy(operation);
		if (NullUtil.isNull(user)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.CREATED_USER_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(user);
	}
}

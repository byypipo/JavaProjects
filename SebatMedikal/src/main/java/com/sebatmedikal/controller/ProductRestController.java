package com.sebatmedikal.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sebatmedikal.UserSession;
import com.sebatmedikal.configuration.ErrorCodes;
import com.sebatmedikal.configuration.SecurityConfiguration;
import com.sebatmedikal.domain.Operation;
import com.sebatmedikal.domain.OperationType;
import com.sebatmedikal.domain.Product;
import com.sebatmedikal.domain.Stock;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.gcm.NotificationSender;
import com.sebatmedikal.gcm.gcm.GcmPushImpl;
import com.sebatmedikal.model.request.RequestModel;
import com.sebatmedikal.model.request.RequestModelProduct;
import com.sebatmedikal.model.response.ResponseModel;
import com.sebatmedikal.model.response.ResponseModelError;
import com.sebatmedikal.model.response.ResponseModelSuccess;
import com.sebatmedikal.service.OperationService;
import com.sebatmedikal.service.OperationTypeService;
import com.sebatmedikal.service.ProductService;
import com.sebatmedikal.service.UserService;
import com.sebatmedikal.util.CompareUtil;
import com.sebatmedikal.util.LogUtil;
import com.sebatmedikal.util.NullUtil;

@RestController
@RequestMapping("/productsRest")
public class ProductRestController {
	@Autowired
	private ProductService productService;

	@Autowired
	private OperationService operationService;

	@Autowired
	private OperationTypeService operationTypeService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserSession userSession;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseModel processGetRequest() throws IOException {
		return new ResponseModelError().setErrorCode(ErrorCodes.GET_METHOD_NOT_SUPPORTED);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseModel processPostRequest(@RequestBody RequestModelProduct rmp) {
		try {
			if (!RequestModel.isValid(rmp, userSession)) {
				return new ResponseModelError().setErrorCode(ErrorCodes.BAD_REQUEST);
			}

			switch (rmp.getOperation()) {
			case "findAll":
				return findAll();
			case "findOne":
				return findOne(Integer.parseInt(rmp.getParameter01()));
			case "price":
				return price(Integer.parseInt(rmp.getParameter01()), rmp.getParameter02());
			case "count":
				return count();
			case "page":
				return page(Integer.parseInt(rmp.getParameter01()));
			case "operations":
				return operations(Integer.parseInt(rmp.getParameter01()));
			case "operation":
				return operation(Integer.parseInt(rmp.getParameter01()), Integer.parseInt(rmp.getParameter02()), rmp.getParameter03());
			case "stock":
				return stock(Integer.parseInt(rmp.getParameter01()));
			case "createdBy":
				return createdBy(Integer.parseInt(rmp.getParameter01()));
			case "create":
				return create(rmp.getProduct());
			case "delete":
				return delete(Integer.parseInt(rmp.getParameter01()));
			default:
				return new ResponseModelError().setErrorCode(ErrorCodes.UNKNOWN_OPERATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseModelError().setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
		}
	}

	public ResponseModel findAll() {
		List<Product> products = productService.findAll();
		return new ResponseModelSuccess().setContent(products);
	}

	public ResponseModel findOne(long id) {
		Product product = productService.findById(id);
		if (NullUtil.isNull(product)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRODUCT_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(product);
	}

	public ResponseModel price(long id, String priceString) {
		if (NullUtil.isNull(priceString)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRICE_CANNOT_BE_NULL);
		}

		Product product = productService.findById(id);
		if (NullUtil.isNull(product)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRODUCT_NOT_FOUND);
		}

		BigDecimal newPrice = new BigDecimal(priceString);

		if (CompareUtil.equal(newPrice, product.getPrice())) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRICE_NOT_CHANGED);
		}

		if (newPrice.longValue() <= 0) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRICE_BT_0);
		}

		product.setPrice(newPrice);
		productService.save(product);

		return new ResponseModelSuccess().setContent(product);
	}

	public ResponseModel count() {
		long count = productService.count();
		return new ResponseModelSuccess().setContent(count);
	}

	public ResponseModel page(int pageIndex) {
		List<Product> products = productService.getProductPage(pageIndex);
		return new ResponseModelSuccess().setContent(products);
	}

	public ResponseModel operations(long id) {
		List<Operation> operations = operationService.findByProductId(id);
		if (NullUtil.isNull(operations)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.OPERATIONS_NOT_FOUND);
		}

		if (operations.isEmpty()) {
			return new ResponseModelError().setErrorCode(ErrorCodes.OPERATIONS_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(operations);
	}

	public ResponseModel operation(long id, int count, String note) {
		Product product = productService.findById(id);
		if (NullUtil.isNull(product)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRODUCT_NOT_FOUND);
		}

		if (count == 0) {
			return new ResponseModelError().setErrorCode(ErrorCodes.OPERATION_COUNT_0);
		}

		if (NullUtil.isNull(product.getPrice())) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRODUCT_PRICE_NOT_SETTED);
		}

		boolean isSale = false;
		if (count < 0) {
			isSale = true;
			count *= -1;
		}

		OperationType operationType = operationTypeService.findByIsSale(isSale);
		if (NullUtil.isNull(operationType)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.OPERATIONTYPE_NOT_FOUND);
		}

		Operation operation = new Operation();
		operation.setOperationType(operationType);
		operation.setProduct(product);
		operation.setCount(count);
		operation.setCreatedBy(userSession.getUser().getUsername());
		operation.setNote(note);

		int responseCode = operationService.save(operation);

		if (CompareUtil.equal(responseCode, ErrorCodes.SUCCESSFULLY)) {
			GcmPushImpl gcmPushImpl = new GcmPushImpl(SecurityConfiguration.FCM_SERVER_KEY);
			NotificationSender.operation(gcmPushImpl, userService.findAllFcmRegistrationIds(), operation);

			return new ResponseModelSuccess().setContent(operation);
		}

		return new ResponseModelError().setErrorCode(responseCode);
	}

	public ResponseModel stock(long id) {
		Product product = productService.findById(id);
		if (NullUtil.isNull(product)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRODUCT_NOT_FOUND);
		}

		Stock stock = product.getStock();
		return new ResponseModelSuccess().setContent(stock);
	}

	@RequestMapping(value = { "{id}/image" }, method = RequestMethod.GET)
	public void getImageAsByteArray(@PathVariable long id, HttpServletResponse response) throws IOException {
		LogUtil.logMessage(this.getClass(), "getImageAsByteArray called");
		Product product = productService.findById(id);

		byte[] productImage = product.getImage();
		if (productImage == null) {
			LogUtil.logMessage(this.getClass(), "userMedia not found for findByName");
			// TODO: FIX ME
			return;
		}

		InputStream in = new ByteArrayInputStream(productImage);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		IOUtils.copy(in, response.getOutputStream());
	}

	public ResponseModel createdBy(long id) {
		Product product = productService.findById(id);
		if (NullUtil.isNull(product)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRODUCT_NOT_FOUND);
		}

		User user = productService.getCreatedBy(product);
		if (NullUtil.isNull(user)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.CREATED_USER_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(user);
	}

	public ResponseModel create(Product product) {
		if (productService.isProductExist(product)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRODUCT_IS_EXIST);
		}

		product.setCreatedBy(userSession.getUser().getUsername());
		productService.save(product);

		List<Product> products = productService.findAll();
		return new ResponseModelSuccess().setContent(products);
	}

	public ResponseModel delete(long id) {
		productService.delete(id);

		List<Product> products = productService.findAll();
		return new ResponseModelSuccess().setContent(products);
	}
}

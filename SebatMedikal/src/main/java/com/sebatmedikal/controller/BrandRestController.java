package com.sebatmedikal.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
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
import com.sebatmedikal.domain.Brand;
import com.sebatmedikal.domain.Product;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.model.request.RequestModel;
import com.sebatmedikal.model.request.RequestModelBrand;
import com.sebatmedikal.model.response.ResponseModel;
import com.sebatmedikal.model.response.ResponseModelError;
import com.sebatmedikal.model.response.ResponseModelSuccess;
import com.sebatmedikal.service.BrandService;
import com.sebatmedikal.service.ProductService;
import com.sebatmedikal.service.UserService;
import com.sebatmedikal.util.LogUtil;
import com.sebatmedikal.util.NullUtil;

@RestController
@RequestMapping("/brandsRest")
public class BrandRestController {
	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserSession userSession;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseModel processGetRequest() throws IOException {
		return new ResponseModelError().setErrorCode(ErrorCodes.GET_METHOD_NOT_SUPPORTED);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseModel processPostRequest(@RequestBody RequestModelBrand rmb) {
		try {
			if (!RequestModel.isValid(rmb, userSession)) {
				return new ResponseModelError().setErrorCode(ErrorCodes.BAD_REQUEST);
			}

			switch (rmb.getOperation()) {
			case "findAll":
				return findAll();
			case "findAllOnlyName":
				return findAllOnlyName();
			case "findOne":
				return findOne(Integer.parseInt(rmb.getParameter01()));
			case "products":
				return getProducts(Integer.parseInt(rmb.getParameter01()));
			case "createdBy":
				return createdBy(Integer.parseInt(rmb.getParameter01()));
			case "create":
				return create(rmb.getBrand());
			case "update":
				return update(rmb.getBrand());
			case "delete":
				return delete(Integer.parseInt(rmb.getParameter01()));
			default:
				return new ResponseModelError().setErrorCode(ErrorCodes.UNKNOWN_OPERATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseModelError().setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
		}
	}

	public ResponseModel findAll() {
		List<Brand> brands = brandService.findAll();

		User currentUser = userSession.getUser();
		currentUser.setReadedBrandsDate(new Date());
		userService.save(currentUser);

		return new ResponseModelSuccess().setContent(brands);
	}

	public ResponseModel findAllOnlyName() {
		List<String> brandNames = brandService.findAllOnlyName();
		return new ResponseModelSuccess().setContent(brandNames);
	}

	public ResponseModel findOne(long id) {
		Brand brand = brandService.findById(id);
		if (NullUtil.isNull(brand)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.BRAND_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(brand);
	}

	public ResponseModel getProducts(long id) {
		List<Product> products = productService.findByBrandId(id);
		if (NullUtil.isNull(products)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRODUCT_NOT_FOUND);
		}

		if (products.isEmpty()) {
			return new ResponseModelError().setErrorCode(ErrorCodes.PRODUCT_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(products);
	}

	@RequestMapping(value = { "/{id}/image" }, method = RequestMethod.GET)
	public void getImageAsByteArray(@PathVariable long id, HttpServletResponse response) throws IOException {
		LogUtil.logMessage(this.getClass(), "getImageAsByteArray called");
		Brand brand = brandService.findById(id);

		byte[] brandImage = brand.getImage();
		if (NullUtil.isNull(brandImage)) {
			LogUtil.logMessage(this.getClass(), "userMedia not found for findByName");
			// TODO: FIX ME
			return;
		}

		InputStream in = new ByteArrayInputStream(brandImage);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		IOUtils.copy(in, response.getOutputStream());
	}

	public ResponseModel createdBy(long id) {
		Brand brand = brandService.findById(id);
		if (NullUtil.isNull(brand)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.BRAND_NOT_FOUND);
		}

		User user = brandService.getCreatedBy(brand);
		if (NullUtil.isNull(user)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.USER_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(user);
	}

	public ResponseModel create(Brand brand) {
		if (brandService.isBrandExist(brand)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.BRAND_IS_EXIST);
		}

		brand.setCreatedBy(userSession.getUser().getUsername());
		brandService.save(brand);

		return new ResponseModelSuccess().setContent(brand);
	}

	public ResponseModel update(Brand brand) {
		if (NullUtil.isNull(brand)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.BRAND_CANNOT_BE_NULL);
		}

		brandService.save(brand);

		return new ResponseModelSuccess().setContent(brand);
	}

	public ResponseModel delete(long id) {
		brandService.delete(id);

		List<Brand> brands = brandService.findAll();
		return new ResponseModelSuccess().setContent(brands);
	}
}

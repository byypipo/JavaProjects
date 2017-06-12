package com.sebatmedikal.model.request;

import com.sebatmedikal.domain.Product;

public class RequestModelProduct extends RequestModel {
	private Product product;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}

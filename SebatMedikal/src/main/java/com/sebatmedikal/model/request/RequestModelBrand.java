package com.sebatmedikal.model.request;

import com.sebatmedikal.domain.Brand;

public class RequestModelBrand extends RequestModel {
	private Brand brand;

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
}

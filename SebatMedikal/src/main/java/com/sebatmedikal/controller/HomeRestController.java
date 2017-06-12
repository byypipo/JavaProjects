package com.sebatmedikal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sebatmedikal.model.response.ResponseModel;
import com.sebatmedikal.model.response.ResponseModelSuccess;	

@RestController
public class HomeRestController {
	@RequestMapping(value = { "/hello" }, method = RequestMethod.GET)
	public ResponseModel all() {
		return new ResponseModelSuccess().setContent("Hello");
	}
}

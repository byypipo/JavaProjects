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
import com.sebatmedikal.domain.Role;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.model.request.RequestModel;
import com.sebatmedikal.model.response.ResponseModel;
import com.sebatmedikal.model.response.ResponseModelError;
import com.sebatmedikal.model.response.ResponseModelSuccess;
import com.sebatmedikal.service.RoleService;
import com.sebatmedikal.service.UserService;
import com.sebatmedikal.util.NullUtil;

@RestController
@RequestMapping("/rolesRest")
public class RoleRestController {
	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

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
			case "users":
				return users(Integer.parseInt(rm.getParameter01()));
			default:
				return new ResponseModelError().setErrorCode(ErrorCodes.UNKNOWN_OPERATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseModelError().setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
		}
	}

	public ResponseModel findAll() {
		List<Role> roles = roleService.findAll();
		return new ResponseModelSuccess().setContent(roles);
	}

	public ResponseModel findOne(long id) {
		Role role = roleService.findById(id);
		if (NullUtil.isNull(role)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.ROLE_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(role);
	}

	public ResponseModel users(long id) {
		List<User> users = userService.findByRoleId(id);
		if (NullUtil.isNull(users)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.USERS_NOT_FOUND);
		}

		if (users.isEmpty()) {
			return new ResponseModelError().setErrorCode(ErrorCodes.USERS_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(users);
	}
}

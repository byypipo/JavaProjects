package com.sebatmedikal.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.sebatmedikal.domain.Login;
import com.sebatmedikal.domain.Role;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.gcm.NotificationSender;
import com.sebatmedikal.gcm.gcm.GcmPushImpl;
import com.sebatmedikal.model.request.RequestModel;
import com.sebatmedikal.model.request.RequestModelUser;
import com.sebatmedikal.model.response.ResponseModel;
import com.sebatmedikal.model.response.ResponseModelError;
import com.sebatmedikal.model.response.ResponseModelLogin;
import com.sebatmedikal.model.response.ResponseModelSuccess;
import com.sebatmedikal.service.RoleService;
import com.sebatmedikal.service.UserService;
import com.sebatmedikal.util.CompareUtil;
import com.sebatmedikal.util.LogUtil;
import com.sebatmedikal.util.NullUtil;

@RestController
@RequestMapping("/usersRest")
public class UserRestController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserSession userSession;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseModel processGetRequest() throws IOException {
		return new ResponseModelError().setErrorCode(ErrorCodes.GET_METHOD_NOT_SUPPORTED);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseModel processPostRequest(@RequestBody RequestModelUser rmu) {
		try {
			if (!RequestModel.isValid(rmu, userSession)) {
				return new ResponseModelError().setErrorCode(ErrorCodes.BAD_REQUEST);
			}

			switch (rmu.getOperation()) {
			case "findAll":
				return findAll();
			case "findOne":
				return findOne(Integer.parseInt(rmu.getParameter01()));
			case "createdBy":
				return createdBy(Integer.parseInt(rmu.getParameter01()));
			case "update":
				return update(rmu.getUser());
			case "create":
				return create(rmu.getUser());
			case "login":
				return login(rmu.getParameter01(), rmu.getParameter02(), rmu.getParameter03());
			case "logout":
				return logout(rmu.getParameter01());
			case "changePassword":
				return changePassword(rmu.getParameter01(), rmu.getParameter02(), rmu.getParameter03());
			case "loginUsers":
				return loginUsers();
			case "delete":
				return delete(Integer.parseInt(rmu.getParameter01()));
			default:
				return new ResponseModelError().setErrorCode(ErrorCodes.UNKNOWN_OPERATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseModelError().setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
		}
	}

	public ResponseModel findAll() {
		List<User> users = userService.findAll();
		return new ResponseModelSuccess().setContent(users);
	}

	public ResponseModel findOne(long id) {
		User user = userService.findById(id);
		if (NullUtil.isNull(user)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.USER_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(user);
	}

	@RequestMapping(value = { "/{id}/image" }, method = RequestMethod.GET)
	public void getImageAsByteArray(@PathVariable long id, HttpServletResponse response) throws IOException {
		LogUtil.logMessage(this.getClass(), "getImageAsByteArray called");
		User user = userService.findById(id);

		byte[] userImage = user.getImage();
		if (userImage == null) {
			LogUtil.logMessage(this.getClass(), "userMedia not found for findByUsername");
		}

		InputStream in = new ByteArrayInputStream(userImage);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		IOUtils.copy(in, response.getOutputStream());
	}

	public ResponseModel createdBy(long id) {
		User user = userService.findById(id);
		if (NullUtil.isNull(user)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.USER_NOT_FOUND);
		}

		User createdUser = userService.getCreatedBy(user);
		if (NullUtil.isNull(createdUser)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.CREATED_USER_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(createdUser);
	}

	public ResponseModel update(User user) {
		if (NullUtil.isNull(user)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.USER_CANNOT_BE_NULL);
		}

		if (!CompareUtil.equal(userSession.getUser().getId(), user.getId())) {
			return new ResponseModelError().setErrorCode(ErrorCodes.CANNOT_FOR_ANOTHER_USER);
		}

		userService.save(user);

		return new ResponseModelSuccess().setContent(user);
	}

	public ResponseModel create(User user) {
		if (userService.isUserExist(user)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.USER_IS_EXIST);
		}

		long roleId = user.getRole().getId();
		Role originalRole = roleService.findById(roleId);
		if (NullUtil.isNull(originalRole)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.ROLE_NOT_FOUND);
		}

		user.setRole(originalRole);
		user.setCreatedBy(userSession.getUser().getUsername());
		userService.save(user);

		return new ResponseModelSuccess().setContent(user);
	}

	public ResponseModel login(String username, String password, String fcmRegistrationId) {
		if (NullUtil.isAnyNull(username, password)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.USER_NOT_FOUND);
		}

		User user = userService.login(username, password, fcmRegistrationId);

		if (NullUtil.isNull(user)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.USER_NOT_FOUND);
		}

		userSession.setUser(user);

		GcmPushImpl gcmPushImpl = new GcmPushImpl(SecurityConfiguration.FCM_SERVER_KEY);
		NotificationSender.login(gcmPushImpl, userService.findAllFcmRegistrationIds(), user);
		return new ResponseModelLogin().setUser(user).setAccessToken(userSession.getAccessToken());
	}

	public ResponseModel logout(String username) {
		if (NullUtil.isNull(username)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.USER_NOT_FOUND);
		}

		if (!CompareUtil.equal(userSession.getUser().getUsername(), username)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.LOGOUT_REQ_ANOTHER_USER);
		}

		boolean successful = userService.logout(username);

		if (successful) {
			GcmPushImpl gcmPushImpl = new GcmPushImpl(SecurityConfiguration.FCM_SERVER_KEY);
			NotificationSender.login(gcmPushImpl, userService.findAllFcmRegistrationIds(), userSession.getUser());
			userSession.clear();
			return new ResponseModelSuccess();
		} else {
			return new ResponseModelError().setErrorCode(ErrorCodes.USER_ALREADY_LOGOUT);
		}
	}

	public ResponseModel changePassword(String username, String password, String newPassword) {
		if (NullUtil.isAnyNull(username, password)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.USER_NOT_FOUND);
		}

		boolean success = userService.changePassword(username, password, newPassword);

		if (success) {
			return new ResponseModelSuccess();
		} else {
			return new ResponseModelError().setErrorCode(ErrorCodes.UNKNOWN_ERROR);
		}
	}

	public ResponseModel loginUsers() {
		List<Login> logins = userService.getLoginUsers();

		if (NullUtil.isNull(logins)) {
			return new ResponseModelError().setErrorCode(ErrorCodes.USERS_NOT_FOUND);
		}

		return new ResponseModelSuccess().setContent(logins);
	}

	public ResponseModel delete(long id) {
		userService.delete(id);
		List<User> users = userService.findAll();
		return new ResponseModelSuccess().setContent(users);
	}
}

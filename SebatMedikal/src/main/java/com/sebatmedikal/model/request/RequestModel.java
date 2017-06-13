package com.sebatmedikal.model.request;

import java.util.logging.Level;

import com.sebatmedikal.UserSession;
import com.sebatmedikal.util.CompareUtil;
import com.sebatmedikal.util.LogUtil;
import com.sebatmedikal.util.NullUtil;

public class RequestModel {
	private String accessToken;
	private String operation;
	private String parameter01;
	private String parameter02;
	private String parameter03;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getParameter01() {
		return parameter01;
	}

	public void setParameter01(String parameter01) {
		this.parameter01 = parameter01;
	}

	public String getParameter02() {
		return parameter02;
	}

	public void setParameter02(String parameter02) {
		this.parameter02 = parameter02;
	}

	public String getParameter03() {
		return parameter03;
	}

	public void setParameter03(String parameter03) {
		this.parameter03 = parameter03;
	}

	public static boolean isValid(RequestModel rm, UserSession userSession) {
		if (NullUtil.isNull(rm)) {
			LogUtil.logMessage(RequestModel.class, Level.WARNING, "RequestModel is null");
			return false;
		}

		if (NullUtil.isNull(rm.getOperation())) {
			LogUtil.logMessage(RequestModel.class, Level.WARNING, "Operation is null");
			return false;
		}

		LogUtil.logMessage(RequestModel.class, rm.getOperation() + " called");

		if (CompareUtil.equal(rm.getOperation(), "login")) {
			if (NullUtil.isAnyNull(rm.getParameter01(), rm.getParameter02())) {
				LogUtil.logMessage(RequestModel.class, Level.WARNING, "Login parameters are invalid");
				return false;
			}
		} else {
			LogUtil.logMessage(RequestModel.class, Level.WARNING, "userSession.getUser(): " + userSession.getUser());
			LogUtil.logMessage(RequestModel.class, Level.WARNING, "userSession.getAccessToken(): " + userSession.getAccessToken());

			if (NullUtil.isAnyNull(userSession.getUser(), userSession.getAccessToken())) {
				LogUtil.logMessage(RequestModel.class, Level.WARNING, "User is not login requested operation: " + rm.getOperation());
				return false;
			}

			if (NullUtil.isNull(rm.getAccessToken())) {
				LogUtil.logMessage(RequestModel.class, Level.WARNING, "AccessToken is null");
				return false;
			}

			if (!CompareUtil.equal(rm.getAccessToken(), userSession.getAccessToken())) {
				LogUtil.logMessage(RequestModel.class, Level.WARNING, "AccessToken is not matched");
				return false;
			}
		}

		LogUtil.logMessage(RequestModel.class, "UserSessionAccessToken: " + userSession.getAccessToken());
		LogUtil.logMessage(RequestModel.class, "RequestModelAccessToken: " + rm.getAccessToken());

		return true;
	}
}

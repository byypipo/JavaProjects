package com.sebatmedikal.model.response;

import com.sebatmedikal.configuration.ErrorCodes;

public class ResponseModelError extends ResponseModel {
	private int errorCode;
	private String error;

	public ResponseModelError() {
		super(false);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public ResponseModelError setErrorCode(int errorCode) {
		this.errorCode = errorCode;
		this.error=ErrorCodes.getErrorMessage(errorCode);
		return this;
	}

	public String getError() {
		return error;
	}
}

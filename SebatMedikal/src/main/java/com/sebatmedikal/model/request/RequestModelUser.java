package com.sebatmedikal.model.request;

import com.sebatmedikal.domain.User;

public class RequestModelUser extends RequestModel {
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

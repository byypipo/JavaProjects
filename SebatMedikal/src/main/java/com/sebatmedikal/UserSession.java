package com.sebatmedikal;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.sebatmedikal.domain.User;
import com.sebatmedikal.util.EncryptUtil;

@SessionScope
@Component
public class UserSession {
	private String accessToken;
	private User user;

	public String getAccessToken() {
		return accessToken;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		try {
			this.accessToken = EncryptUtil.encodeAES(EncryptUtil.encodeAES(System.currentTimeMillis() + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		this.accessToken = null;
		this.user = null;
	}
}

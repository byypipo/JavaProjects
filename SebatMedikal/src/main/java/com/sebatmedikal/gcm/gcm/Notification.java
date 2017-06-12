package com.sebatmedikal.gcm.gcm;

import org.json.simple.JSONObject;

/**
 * 
 * @author orhan
 *
 */
public class Notification extends JSONObject {
	private static final long serialVersionUID = 1L;

	public String getTitle() {
		return (String) get("title");
	}

	@SuppressWarnings("unchecked")
	public Notification setTitle(String title) {
		put("title", title);
		return this;
	}

	public String getBody() {
		return (String) get("body");
	}

	@SuppressWarnings("unchecked")
	public Notification setBody(String body) {
		put("body", body);
		return this;
	}
}

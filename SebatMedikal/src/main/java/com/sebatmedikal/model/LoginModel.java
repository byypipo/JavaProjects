package com.sebatmedikal.model;

public class LoginModel {
	private String username;
	private String password;

	public LoginModel() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		stringBuilder.append("\"username\":\""+username+"\",");
		stringBuilder.append("\"password\":\""+password+"\"");
		stringBuilder.append("}");

		return stringBuilder.toString();
	}
}

package com.sebatmedikal.configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.sebatmedikal.util.LogUtil;

@Component
@PropertySource("classpath:secure.properties")
public class SecurityConfiguration {
	public static String FCM_SERVER_KEY;
	public static String FCM_DB_URL;
	public static String FCM_SERVICE_ACCOUNT_KEY_NAME;
	public static String FCM_USER_UID;
	public static int FCM_MULTICAST_LIMIT;

	public static String myIp;

	@Value("${fcm.server_key}")
	public void setFCM_SERVER_KEY(String value) {
		FCM_SERVER_KEY = value;
	}

	@Value("${fcm.db_url}")
	public void setFCM_DB_URL(String value) {
		FCM_DB_URL = value;
	}

	@Value("${fcm.service_account_key_name}")
	public void setFCM_SERVICE_ACCOUNT_KEY_NAME(String value) {
		FCM_SERVICE_ACCOUNT_KEY_NAME = value;
	}

	@Value("${fcm.user_id}")
	public void setFCM_USER_UID(String value) {
		FCM_USER_UID = value;
	}

	@Value("${fcm.multicast_limit}")
	public void setFCM_MULTICAST_LIMIT(int value) {
		FCM_MULTICAST_LIMIT = value;
	}

	@Autowired
	public void setMyIp() throws Exception {
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

		myIp = in.readLine();
		LogUtil.logMessage(getClass(), "MY IP: " + myIp);
	}

	public static String SERVER_ONLINE = "SERVER_ONLINE";
	public static String NEW_OPERATION = "NEW_OPERATION";
	public static String NEW_MESSAGE = "NEW_MESSAGE";
	public static String NEW_PRODUCT = "NEW_PRODUCT";
	public static String LOGIN = "LOGIN";
	public static String LOGOUT = "LOGOUT";
}

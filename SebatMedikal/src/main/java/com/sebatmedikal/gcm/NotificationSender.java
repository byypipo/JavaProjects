package com.sebatmedikal.gcm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.sebatmedikal.configuration.SecurityConfiguration;
import com.sebatmedikal.domain.Operation;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.gcm.gcm.GcmPushImpl;
import com.sebatmedikal.gcm.gcm.GcmPushInfo;
import com.sebatmedikal.gcm.gcm.Notification;
import com.sebatmedikal.mapper.Mapper;
import com.sebatmedikal.util.LogUtil;
import com.sebatmedikal.util.NullUtil;

public class NotificationSender {
	@Deprecated
	private static String regId;

	public static void serverOnline(GcmPushImpl gcmPushImpl, ArrayList<String> usersFcmRegIds) {
		Notification notification = generateNotification(SecurityConfiguration.SERVER_ONLINE);

		GcmPushInfo gcmPushInfo = new GcmPushInfo(usersFcmRegIds, "{\"serverip\":\"" + SecurityConfiguration.myIp + "\"}", notification);
		send(gcmPushImpl, gcmPushInfo);
	}

	public static void operation(GcmPushImpl gcmPushImpl, ArrayList<String> usersFcmRegIds, Operation operation) {
		Notification notification = new Notification().setTitle(SecurityConfiguration.NEW_OPERATION).setBody(new Date().toString());
		String data = Mapper.writeValueAsString(operation);

		LogUtil.logMessage(NotificationSender.class, "Operation: " + data);

		GcmPushInfo gcmPushInfo = new GcmPushInfo(usersFcmRegIds, data, notification);
		send(gcmPushImpl, gcmPushInfo);
	}

	public static void login(GcmPushImpl gcmPushImpl, ArrayList<String> usersFcmRegIds, User user) {
		Notification notification = new Notification().setTitle(SecurityConfiguration.LOGIN).setBody(new Date().toString());
		String data = Mapper.writeValueAsString(user);

		LogUtil.logMessage(NotificationSender.class, "LOGIN User: " + data);

		GcmPushInfo gcmPushInfo = new GcmPushInfo(usersFcmRegIds, data, notification);
		send(gcmPushImpl, gcmPushInfo);
	}

	public static void logout(GcmPushImpl gcmPushImpl, ArrayList<String> usersFcmRegIds, User user) {
		Notification notification = new Notification().setTitle(SecurityConfiguration.LOGOUT).setBody(new Date().toString());
		String data = Mapper.writeValueAsString(user);

		LogUtil.logMessage(NotificationSender.class, "LOGOUT User: " + data);

		GcmPushInfo gcmPushInfo = new GcmPushInfo(usersFcmRegIds, data, notification);
		send(gcmPushImpl, gcmPushInfo);
	}

	@SuppressWarnings("unused")
	@Deprecated
	private static String getRegistrationId() {
		if (NullUtil.isNotNull(regId)) {
			return regId;
		}

		InputStream inputStream = NotificationSender.class.getClassLoader().getResourceAsStream(SecurityConfiguration.FCM_SERVICE_ACCOUNT_KEY_NAME);
		FirebaseOptions options = new FirebaseOptions.Builder().setServiceAccount(inputStream).setDatabaseUrl(SecurityConfiguration.FCM_DB_URL).build();
		FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
		FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);

		if (NullUtil.isNull(firebaseAuth)) {
			LogUtil.logMessage(NotificationSender.class, "firebaseAuth is null");
			return null;
		}

		regId = firebaseAuth.createCustomToken(SecurityConfiguration.FCM_USER_UID);
		LogUtil.logMessage(NotificationSender.class, "Generated token: " + regId);
		return regId;
	}

	private static Notification generateNotification(String title) {
		return new Notification().setTitle(title).setBody(new Date().toString());
	}

	private static void send(GcmPushImpl gcmPushImpl, GcmPushInfo gcmPushInfo) {
		try {
			if (NullUtil.isNull(gcmPushImpl)) {
				return;
			}

			gcmPushImpl.sendPush(gcmPushInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// GcmPushImpl gcmPushImpl = new
		// GcmPushImpl(SecurityConfiguration.FCM_SERVER_KEY);
		// ArrayList<String> regIds = new ArrayList<>();
		// regIds.add("czigGDynriM:APA91bEr91-U_nyGadFotz4X-NjSkHOC-qVDjLxn40ri-aX6OuAIJ9p2e1J51eCU3_bhdcerfzOh4Mw2EPC8u55IybcpIvrxXhkEUucY3mpyDAmeJjMmU8d11l-9w05xdqfx1sxZOvJU");
		//
		// serverOnline(gcmPushImpl, regIds);

		String json = "{\"id\":4,\"operationType\":{\"id\":2,\"operationTypeName\":\"SATIM\",\"sale\":true,\"note\":null,\"createdBy\":\"sebat.medikal\",\"createdDate\":1497156734363},\"product\":{\"id\":1,\"productName\":\"PRODUCT001\",\"barcod\":null,\"price\":10.10,\"image\":null,\"brand\":{\"id\":2,\"brandName\":\"BRAND02\",\"image\":null,\"note\":null,\"createdBy\":\"sebat.medikal\",\"createdDate\":1497156734281},\"note\":null,\"stock\":{\"id\":1,\"count\":425,\"createdBy\":\"sebat.medikal\",\"createdDate\":1497156734286},\"createdBy\":\"sebat.medikal\",\"createdDate\":1497156734294},\"count\":1,\"totalPrice\":10.1,\"note\":null,\"createdBy\":\"sebat.medikal\",\"createdDate\":1497156734414}";
		System.out.println(json);

	}
}

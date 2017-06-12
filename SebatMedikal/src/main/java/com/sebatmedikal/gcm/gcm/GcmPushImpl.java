package com.sebatmedikal.gcm.gcm;

import java.io.IOException;

import org.springframework.util.Assert;

import com.sebatmedikal.configuration.SecurityConfiguration;
import com.sebatmedikal.gcm.server.InvalidRequestException;
import com.sebatmedikal.gcm.server.Message;
import com.sebatmedikal.gcm.server.MulticastResult;
import com.sebatmedikal.gcm.server.Sender;
import com.sebatmedikal.util.LogUtil;

/**
 * 
 * @author orhan
 *
 */

public class GcmPushImpl {
	private Sender sender;

	public GcmPushImpl(String FCM_SERVER_KEY) {
		sender = new Sender(FCM_SERVER_KEY);
	}

	public GcmMulticatResult sendPush(GcmPushInfo info) throws GcmMulticastLimitExceededException, IllegalArgumentException, InvalidRequestException, IOException {
		Assert.notNull(info, "info should not be null.");
		// Assert.isTrue(info.getData() != null && info.getData().length() > 0,
		// "data should not be Null or empty string.");
		Assert.isTrue(info.getRegIdList() != null && info.getRegIdList().size() > 0, "regIdList should not be Null or empty string.");

		if (info.getRegIdList().size() > SecurityConfiguration.FCM_MULTICAST_LIMIT) {
			throw new GcmMulticastLimitExceededException();
		}

		Message message = new Message.Builder().addData("message", info.getData()).addNotification(info.getNotification()).delayWhileIdle(info.isDelayWhileIdle()).timeToLive(info.getTimeToLive())
				.collapseKey(info.getCollapseKey()).build();
		LogUtil.logMessage(getClass(), "Data: " + info.getData());
		LogUtil.logMessage(getClass(), "Notification: " + info.getNotification());

		MulticastResult multicastResult = sender.sendNoRetry(message, info.getRegIdList());
		GcmMulticatResult gcmMulticatResult = new GcmMulticatResult(multicastResult);
		LogUtil.logMessage(getClass(), gcmMulticatResult.toString());

		return gcmMulticatResult;
	}
}

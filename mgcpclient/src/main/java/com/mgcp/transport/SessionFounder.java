package com.mgcp.transport;

import java.net.DatagramPacket;

import com.mgcp.message.response.MGCPResponse;
import com.util.CustomThread;
import com.util.LogUtil;
import com.util.NullUtil;

public class SessionFounder extends CustomThread {
	private String message;
	private MGCPTransportLayer mgcpTransportLayer;

	public SessionFounder(DatagramPacket receivePacket, MGCPTransportLayer mgcpTransportLayer) {
		this.message = new String(receivePacket.getData());
		this.mgcpTransportLayer = mgcpTransportLayer;
	}

	@Override
	public void startThread() throws Exception {
		LogUtil.logMessage(getClass(), "Session Founder Thread started");
		MGCPResponse mgcpResponse = MGCPResponse.parse(message);

		int transactionId = mgcpResponse.getResponseLine().getTransactionId();

		MgcpSession session = (MgcpSession) mgcpTransportLayer.getSession(transactionId);
		if (NullUtil.isNull(session)) {
			throw new Exception("Session not found for transactionId: " + transactionId);
		}

		LogUtil.logMessage(getClass(), "Session Founder Thread finished");
		session.receive(mgcpResponse);
	}

}

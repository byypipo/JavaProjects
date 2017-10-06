package com.mgcp.transport;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

import com.configuration.GeneralConfiguration;
import com.mgcp.old.parameters.MgcpSharedParams;
import com.util.CustomThread;
import com.util.LogUtil;

public class MGCPTransportLayer extends CustomThread {
	private DatagramSocket serverSocket;

	private Properties sessions = new Properties();

	public MGCPTransportLayer() throws Exception {
		serverSocket = new DatagramSocket(GeneralConfiguration.mediaServerPort, InetAddress.getLocalHost());
		serverSocket.setSoTimeout(GeneralConfiguration.waitingResponseTimeout);
	}

	@Override
	public void startThread() throws Exception {
		LogUtil.logMessage(getClass(), "MGCPTransportLayer is started");

		while (!isStoped()) {
			receive();
		}

		LogUtil.logMessage(getClass(), "MGCPTransportLayer is finished");
	}

	public MgcpSession getSession(long transactionId) {
		return (MgcpSession) sessions.get(transactionId);
	}

	public void addSession(long transactionId, MgcpSession mgcpSession) {
		sessions.put(transactionId, mgcpSession);
	}

	public void send(MgcpSession mgcpSession) throws IOException {
		byte[] commandBytes = mgcpSession.getCurrentCommand().toString().getBytes();
		DatagramPacket sendPacket = new DatagramPacket(commandBytes, commandBytes.length, InetAddress.getByName(MgcpSharedParams.mediaServerAddress), MgcpSharedParams.mediaServerPort);

		serverSocket.send(sendPacket);

	}

	public void receive() throws Exception {
		DatagramPacket receivePacket = new DatagramPacket(new byte[MgcpSharedParams.MAXPACKETLENGTH], MgcpSharedParams.MAXPACKETLENGTH);
		serverSocket.receive(receivePacket);

		new SessionFounder(receivePacket, this).start();
	}
}

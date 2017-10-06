package com.mgcp.transport;

import java.util.ArrayList;

import com.mgcp.exceptions.MGCPParseException;
import com.mgcp.message.MGCPMessage.MgcpParametersEnum;
import com.mgcp.message.command.MGCPCommand;
import com.mgcp.message.parameter.MGCPParameter;
import com.mgcp.message.response.MGCPResponse;
import com.util.LogUtil;
import com.util.NullUtil;

public class MgcpSession {
	private long transactionId = -1;
	private MGCPTalk mgcpTalk;

	private ArrayList<MGCPTalk> talks = new ArrayList<>();

	private MGCPTransportLayer transportLayer;

	public MgcpSession(MGCPTransportLayer transportLayer, long transactionId) throws Exception {
		this.transportLayer = transportLayer;
		this.transportLayer.addSession(transactionId, this);
		this.transactionId = transactionId;
	}

	public void send(MGCPCommand mgcpCommand) throws Exception {
		if (NullUtil.isNotNull(mgcpTalk)) {
			throw new Exception("mgcpTalk must be null for send new command");
		}

		mgcpTalk = new MGCPTalk(mgcpCommand);
		LogUtil.logMessage(getClass(), "Sended Message:\n" + mgcpCommand.toString());
		transportLayer.send(this);
	}

	public void receive(MGCPResponse mgcpResponse) throws Exception {
		if (NullUtil.isNull(mgcpTalk)) {
			throw new Exception("currentCommand must be notnull for receive new response");
		}

		LogUtil.logMessage(getClass(), "Received Message:\n" + mgcpResponse.toString());
		mgcpTalk.setMgcpResponse(mgcpResponse);
		talks.add(mgcpTalk);
		mgcpTalk = null;
	}

	public MGCPCommand getCurrentCommand() {
		if (NullUtil.isNull(mgcpTalk)) {
			return null;
		}

		return mgcpTalk.getMgcpCommand();
	}

	public MGCPTalk getLastMgcpTalk() {
		if (NullUtil.isNull(talks)) {
			return null;
		}

		for (int i = (talks.size() - 1); i >= 0; i--) {
			if (talks.get(i).isCompleted()) {
				return talks.get(i);
			}
		}

		return null;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public ArrayList<MGCPTalk> getTalks() {
		return talks;
	}

	public MGCPParameter getCallId() throws MGCPParseException {
		return getParameter(MgcpParametersEnum.CallId);
	}

	public MGCPParameter getConnectionId() throws MGCPParseException {
		return getParameter(MgcpParametersEnum.ConnectionId);
	}

	private MGCPParameter getParameter(MgcpParametersEnum mgcpParametersEnum) throws MGCPParseException {
		if (talks.isEmpty()) {
			return null;
		}

		if (!talks.get(0).isCompleted()) {
			return null;
		}

		MGCPParameter mgcpParameter = talks.get(0).getMgcpResponse().getParameter(mgcpParametersEnum);
		if (NullUtil.isNotNull(mgcpParameter)) {
			return mgcpParameter;
		}

		return talks.get(0).getMgcpCommand().getParameter(mgcpParametersEnum);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("TransactionId: " + transactionId);
		for (int i = 0; i < talks.size(); i++) {
			builder.append(talks.get(i).toString());
		}

		return builder.toString();
	}
}

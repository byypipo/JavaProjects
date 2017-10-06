package com.mgcp.ui;

import java.util.ArrayList;

import com.configuration.Constants;
import com.mgcp.exceptions.MGCPParseException;
import com.mgcp.message.command.MGCPCommand;
import com.mgcp.message.command.commandLine.MGCPCommandLine;
import com.mgcp.message.command.commandLine.MGCPCommandLine.MGCPVerb;
import com.mgcp.message.command.commandLine.endpointName.EndpointName;
import com.mgcp.message.parameter.MGCPParameter;
import com.mgcp.message.parameter.callId.CallIdParameterValue;
import com.mgcp.message.parameter.connectionMode.ConnectionMode.ConnectionModes;
import com.mgcp.message.parameter.connectionMode.ConnectionModeParameterValue;
import com.mgcp.message.parameter.localConnectionOptions.LocalConnectionOptionsParameterValue;
import com.mgcp.message.parameter.notifiedEntity.NotifiedEntityParameterValue;
import com.mgcp.message.parameter.observedEvents.ObservedEventsParameterValue;
import com.mgcp.message.parameter.requestIdentifier.RequestIdentifierParameterValue;
import com.mgcp.message.parameter.requestedEvents.RequestedEvents;
import com.mgcp.message.parameter.requestedEvents.RequestedEventsParameterValue;
import com.mgcp.message.parameter.signalRequests.SignalRequests;
import com.mgcp.message.parameter.signalRequests.SignalRequestsParameterValue;
import com.mgcp.old.parameters.MgcpSharedParams;
import com.mgcp.transport.MGCPTransportLayer;
import com.mgcp.transport.MgcpSession;
import com.util.NullUtil;

public class MGCPUI {
	private MGCPTransportLayer mgcpTransportLayer;

	public MGCPUI() throws Exception {
		mgcpTransportLayer = new MGCPTransportLayer();
		mgcpTransportLayer.start();
	}

	private void createNTFY(int transactionId) throws Exception{
		MGCPCommandLine commandLine = new MGCPCommandLine(MGCPVerb.NTFY, transactionId,
				EndpointName.parse(MgcpSharedParams.ivrEndpointID + "@" + MgcpSharedParams.mediaServerAddress + ":" + MgcpSharedParams.mediaServerPort));

		ArrayList<MGCPParameter> parameters = new ArrayList<>();
		parameters.add(NotifiedEntityParameterValue.generate("ca",MgcpSharedParams.mediaServerAddress, MgcpSharedParams.mediaServerPort));
		parameters.add(RequestIdentifierParameterValue.generate("0123456789AC"));
		parameters.add(ObservedEventsParameterValue.generate("L/hd,D/9,D/1,D/2,D/0,D/1,D/8,D/2,D/9,D/4,D/2,D/6,D/6"));
		
		MGCPCommand commandNTFY = new MGCPCommand(commandLine, parameters);
		MgcpSession mgcpSession = new MgcpSession(mgcpTransportLayer, transactionId);
		mgcpSession.send(commandNTFY);

//		N: ca@ca1.whatever.net:5678
//	      X: 0123456789AC
//	      O: L/hd,D/9,D/1,D/2,D/0,D/1,D/8,D/2,D/9,D/4,D/2,D/6,D/6
	}

	private void createCRCX(int transactionId) throws Exception {
		MGCPCommandLine commandLine = new MGCPCommandLine(MGCPVerb.CRCX, transactionId,
				EndpointName.parse(MgcpSharedParams.ivrEndpointID + "@" + MgcpSharedParams.mediaServerAddress + ":" + MgcpSharedParams.mediaServerPort));

		ArrayList<MGCPParameter> parameters = new ArrayList<>();
		parameters.add(NotifiedEntityParameterValue.generate(MgcpSharedParams.mediaServerAddress, MgcpSharedParams.mediaServerPort));
		parameters.add(CallIdParameterValue.generate());
		parameters.add(LocalConnectionOptionsParameterValue.generate(20, "PCMU", "PCMA"));
		parameters.add(ConnectionModeParameterValue.generate(ConnectionModes.sendrecv));

		MGCPCommand commandCRCX = new MGCPCommand(commandLine, parameters);
		MgcpSession mgcpSession = new MgcpSession(mgcpTransportLayer, transactionId);
		mgcpSession.send(commandCRCX);
	}

	private void createDLCX(int transactionId) throws Exception {
		MGCPCommandLine commandLine = new MGCPCommandLine(MGCPVerb.DLCX, transactionId,
				EndpointName.parse(MgcpSharedParams.ivrEndpointID + "@" + MgcpSharedParams.mediaServerAddress + ":" + MgcpSharedParams.mediaServerPort));

		MgcpSession mgcpSession = mgcpTransportLayer.getSession(transactionId);

		ArrayList<MGCPParameter> parameters = new ArrayList<>();
		if (NullUtil.isNotNull(mgcpSession.getCallId())) {
			parameters.add(mgcpSession.getCallId());
		}

		parameters.add(mgcpSession.getConnectionId());

		MGCPCommand commandDLCX = new MGCPCommand(commandLine, parameters);
		mgcpSession.send(commandDLCX);
	}

	public static void main(String[] args) throws Exception {
		int transactionId = 1215;
		MGCPUI mgcpui = new MGCPUI();
		mgcpui.createCRCX(transactionId);
//		Thread.sleep(5000);
//		mgcpui.createNTFY(transactionId);
//		mgcpui.createDLCX(transactionId);

	}

}

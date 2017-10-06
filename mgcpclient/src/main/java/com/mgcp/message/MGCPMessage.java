package com.mgcp.message;

import java.util.ArrayList;

import com.mgcp.exceptions.MGCPParseException;
import com.mgcp.message.parameter.MGCPParameter;

public abstract class MGCPMessage {
	protected ArrayList<MGCPParameter> parameters = new ArrayList<>();
	protected String sdpInformation;

	public ArrayList<MGCPParameter> getParameters() {
		return parameters;
	}

	public MGCPParameter getParameter(MgcpParametersEnum mgcpParametersEnum) throws MGCPParseException {
		for (int i = 0; i < parameters.size(); i++) {
			if (!parameters.get(i).getParameterValue().getShortname().equals(mgcpParametersEnum.shortName)) {
				continue;
			}

			return parameters.get(i);
		}

		return null;
	}

	public void setParameters(ArrayList<MGCPParameter> parameters) {
		this.parameters = parameters;
	}

	public String getSdpInformation() {
		return sdpInformation;
	}

	public void setSdpInformation(String sdpInformation) {
		this.sdpInformation = sdpInformation;
	}

	public enum MgcpParametersEnum {
		ResponseAck("K"), BearerInformation("B"), CallId("C"), ConnectionId("I"), NotifiedEntity("N"), RequestIdentifier("X"), LocalConnectionOptions("L"), ConnectionMode("M"), RwequestedEvents(
				"R"), SignalRequests("S"), DigitMap("D"), ObservedEvents("O"), ConnectionParameters("P"), ReasonCode("E"), SpecificEndpointId("Z"), SecondEndpointId("Z2"), SecondConnectionId(
						"I2"), RequestedInfo("F"), QuarantineHandling("Q"), DetectEvents(
								"T"), RestartMethod("RM"), RestartDelay("RD"), Capabilities("A"), EventStates("ES"), PackageList("PL"), MaxMGCPDatagram("MD"), ExtensionParameters("");

		String shortName;

		private MgcpParametersEnum(String shortName) {
			this.shortName = shortName;
		}

		public String getShortName() {
			return shortName;
		}
	}
}

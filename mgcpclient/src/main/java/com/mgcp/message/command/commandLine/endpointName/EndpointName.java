package com.mgcp.message.command.commandLine.endpointName;

import com.mgcp.exceptions.MGCPParseException;
import com.mgcp.message.command.commandLine.endpointName.domainName.DomainName;
import com.mgcp.message.command.commandLine.endpointName.localEndpointName.LocalEndpointName;
import com.mgcp.message.parameter.ParameterValueContent;
import com.util.NullUtil;

public class EndpointName implements ParameterValueContent{
	private LocalEndpointName localEndpointName;
	private DomainName domainName;

	public EndpointName(LocalEndpointName localEndpointName, DomainName domainName) {
		this.localEndpointName = localEndpointName;
		this.domainName = domainName;
	}

	public LocalEndpointName getLocalEndpointName() {
		return localEndpointName;
	}

	public DomainName getDomainName() {
		return domainName;
	}

	@Override
	public String toString() {
		return localEndpointName + "@" + domainName;
	}

	public static EndpointName parse(String text) throws MGCPParseException {
		if (NullUtil.isNull(text)) {
			throw new MGCPParseException("text can not be null");
		}

		text = text.trim();
		if (!text.contains("@")) {
			throw new MGCPParseException("text is not contains '@'");
		}

		String[] parts = text.split("@");
		if (NullUtil.isNull(parts)) {
			throw new MGCPParseException("parts can not be null");
		}

		if (parts.length != 2) {
			throw new MGCPParseException("parts length must be 2");
		}

		return new EndpointName(LocalEndpointName.parse(parts[0]), DomainName.parse(parts[1]));
	}
}

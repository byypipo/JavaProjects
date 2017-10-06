package com.mgcp.message.parameter.callId;

import java.util.Random;

import com.mgcp.exceptions.MGCPParseException;
import com.mgcp.message.parameter.MGCPParameter;
import com.mgcp.message.parameter.ParameterValue;
import com.mgcp.message.parameter.SimpleValueContent;
import com.util.NullUtil;

public class CallIdParameterValue extends ParameterValue {
	private static final String finalShortname = "C";

	public CallIdParameterValue() {
		this(null);
	}

	public CallIdParameterValue(SimpleValueContent contents) {
		super(contents);
		this.shortname = finalShortname;
	}

	@Override
	public SimpleValueContent getValue() {
		return (SimpleValueContent) super.getValue();
	}

	@Override
	public CallIdParameterValue parse(String text) throws MGCPParseException {
		text = getValue(text);
		return new CallIdParameterValue(SimpleValueContent.parse(text));
	}

	public static MGCPParameter generate() {
		String CALLID = Integer.toHexString((new Random()).nextInt(Integer.MAX_VALUE)).toUpperCase();
		return new MGCPParameter(new CallIdParameterValue(new SimpleValueContent(CALLID)));
	}

	@Override
	public boolean isMapped(String text) throws MGCPParseException {
		if (NullUtil.isNull(text)) {
			throw new MGCPParseException("text can not be null");
		}

		text = text.trim();

		if (!text.startsWith(finalShortname)) {
			return false;
		}

		text = text.substring(finalShortname.length()).trim();
		if (!text.startsWith(":")) {
			return false;
		}

		return true;
	}
}

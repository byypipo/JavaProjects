package com.util;

public class ConverterUtil {
	public static int toInt(String text) {
		int result = 0;
		try {
			result = Integer.parseInt(text);
		} catch (Exception e) {
		}

		return result;
	}

}

package com.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.configuration.GeneralConfiguration;

public class LogUtil {

	public static void logMessage(Class<?> clazz, String message) {
		logMessage(clazz, Level.INFO, message);
	}

	public static void logMessage(Class<?> clazz, Level level, String message) {
		if (!GeneralConfiguration.logEnable) {
			return;
		}

		Logger.getLogger(clazz.getName()).log(level, "BYYPIPO----------> " + message);
	}
}

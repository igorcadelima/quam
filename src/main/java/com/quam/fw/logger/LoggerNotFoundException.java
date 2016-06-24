package com.quam.fw.logger;

/**
 * @author igorcadelima
 *
 */
public class LoggerNotFoundException extends RuntimeException {
	
	public LoggerNotFoundException() {
		super(
				"Logger not found. Please use LogHelper.createLogger() before trying to log into a logger.");
	}

	public LoggerNotFoundException(String msg) {
		super(msg);
	}
}

package com.quam.fw.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;

import com.quam.fw.logger.LogHelper;

/**
 * @author igorcadelima
 *
 */
aspect FrameworkSetup {
	declare precedence : FrameworkSetup, *;

	/**
	 * Advice before the main method execution
	 */
	before() : execution(void main(String[])) {

		// Hide error message when no configuration file for Log4j found
		StatusLogger.getLogger().setLevel(Level.OFF);

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmssXX");
		String date = df.format(new Date());

		String root = System.getProperty("user.dir");
		char sep = File.separatorChar;
		String logFolder = "logs";
		String path = root + sep + logFolder + sep + date + sep;

		LogHelper.createLogger(path, "architecture.log", "architecture-%i.log",
				LogHelper.ARCHITECTURE);

		LogHelper.createLogger(path, "normal.log", "normal-%i.log", LogHelper.AUDIT);
		
		LogHelper.createLogger(path, "abnormal.log", "abnormal-%i.log", LogHelper.EXCEPTION);

	}
}

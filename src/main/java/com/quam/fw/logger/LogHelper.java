package com.quam.fw.logger;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.quam.fw.element.LoggableElement;

public final class LogHelper {
	public static final String ARCHITECTURE = "ARCHITECTURE";
	public static final String AUDIT = "AUDIT";
	public static final String EXCEPTION = "EXCEPTION";

	private static ObjectMapper mapper = new ObjectMapper().configure(
			SerializationFeature.INDENT_OUTPUT, true).setVisibility(
			PropertyAccessor.FIELD, Visibility.ANY);

	private LogHelper() {
	}

	/**
	 * TODO refactor
	 * 
	 * @param fileName
	 * @param filePattern
	 * @param loggerName
	 */
	public static void createLogger(String path, String fileName,
			String filePattern, String loggerName) {

		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		Configuration config = context.getConfiguration();

		PatternLayout layout = PatternLayout.createLayout("%m\n", config, null,
				Charset.forName("UTF-8"), false, false, "", "");

		SizeBasedTriggeringPolicy policy = SizeBasedTriggeringPolicy
				.createPolicy("15MB");

		DefaultRolloverStrategy strategy = DefaultRolloverStrategy
				.createStrategy("10", "0", null, null, config);

		String sep = String.valueOf(File.separatorChar);
		if (!path.endsWith(sep)) {
			path = path + sep;
		}

		Appender appender = RollingFileAppender.createAppender(path + fileName,
				path + filePattern, "true", loggerName, "true", "8192", "true",
				policy, strategy, layout, null, "true", "false", null, config);
		appender.start();
		config.addAppender(appender);

		AppenderRef ref = AppenderRef.createAppenderRef("File", null, null);
		AppenderRef[] refs = new AppenderRef[] { ref };

		LoggerConfig loggerConfig = LoggerConfig.createLogger("false",
				Level.TRACE, loggerName, "true", refs, null, config, null);
		loggerConfig.addAppender(appender, null, null);

		config.addLogger(loggerName, loggerConfig);
		context.updateLoggers();

	}

	/**
	 * @param loggerName
	 * @param log
	 */
	public static void log(String loggerName, Object log) {

		// TODO Probably not the best way
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		if (!context.getConfiguration().getLoggers().containsKey(loggerName)) {
			throw new LoggerNotFoundException(
					"Logger "
							+ loggerName
							+ " not found. Please use LogHelper.createLogger() before trying to log into a logger.");
		}

		Logger logger = (Logger) LogManager.getLogger(loggerName);

		try {
			StringWriter sw = new StringWriter();
			mapper.writeValue(sw, log);
			logger.trace(sw);

		} catch (Exception e) {
			// TODO
			e.printStackTrace();

		}
	}
	
}

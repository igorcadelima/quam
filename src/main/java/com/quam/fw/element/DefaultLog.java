package com.quam.fw.element;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.quam.fw.annotations.Loggable;

/**
 * @author igorcadelima
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public final class DefaultLog implements LoggableElement {
	private String datetime;
	private String executable;
	private Map<String, Object> args;
	private double cpuTime;
	private double wallTime;
	private String returnType;
	private Object result;
	@JsonUnwrapped
	private Object extension;
	private Exception exception;
	
	public static final short DATE 			= 0; 
	public static final short EXECUTABLE 	= 1;
	public static final short ARGS 			= 2;
	public static final short CPU_TIME		= 3;
	public static final short WALL_TIME		= 4;
	public static final short EXCEPTION		= 5;
	public static final short RETURN_TYPE	= 6;
	public static final short RESULT		= 7;
	
	public DefaultLog() {
		args = new LinkedHashMap<String, Object>();
	}

	/**
	 * @return the executable's name
	 */
	@JsonIgnore
	public final String getExecutableName() {
		return executable;
	}

	/**
	 * @param executable the executable to set
	 */
	public final void setExecutable(Executable element) {
		if (element.getClass().equals(Method.class)) {
			// Needed a different approach. Otherwise, the entire path wouldn't
			// be logged
			executable = String.format("%s.%s", element.getDeclaringClass()
					.getName(), element.getName());

		} else if (element.getClass().equals(Constructor.class)) {
			executable = element.getName();

		} else {
			throw new InvalidParameterException(
					"\"element\" should be either a Constructor or a Method.");

		}
	}

	/**
	 * @return the args
	 */
	public final Map<String, Object> getArgs() {
		return args;
	}

	/**
	 * @param args the args to set
	 */
	public final void setArgs(Parameter[] params, Object[] args) {
		if (!(params.length == args.length)) {
			throw new RuntimeException("The number of parameters and arguments should be the same.");
		}
		
		Parameter param;
		for (int i = 0; i < params.length; i++) {
			param = params[i];
			if (args[i] instanceof Object[]) {
//				this.args.put(param.getName(), decoupleArray((Object[]) args[i]));
				this.args.put(param.getName(), args[i]);
			} else {
				this.args.put(param.getName(), args[i].toString());
			}
		}
	}
	
	/**
	 * @return The CPU time in seconds.
	 */
	public final double getCpuTime() {
		return cpuTime;
	}

	/**
	 * @param cpuTime The CPU time in nanoseconds.
	 */
	public final void setCpuTime(double totalCpuTime) {
		this.cpuTime = totalCpuTime / 1000000000.0;
	}

	/**
	 * @return The wall time in seconds.
	 */
	public final double getWallTime() {
		return wallTime;
	}

	/**
	 * @param wallTime The wall time in nanoseconds.
	 */
	public final void setWallTime(double totalWallTime) {
		this.wallTime = totalWallTime / 1000000000.0;
	}

	/**
	 * @return the exception, if any.
	 */
	public final Exception getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public final void setException(Throwable exception) {
		this.exception = Exception.newInstance(exception);;
	}

	/**
	 * @return The returnType as a string.
	 */
	public final String getReturnType() {
		return returnType;
	}

	/**
	 * @param type The return type of the executable element.
	 */
	public final void setReturnType(Type returnType) {
		this.returnType = returnType.getTypeName();
	}

	/**
	 * @return the result
	 */
	public final Object getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public final void setResult(Object result) {
		this.result = result;
	}
	
	/**
	 * @return
	 */
	public final Object getExtension() {
		return extension;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public final void setExtension(Object extension) {
		this.extension = extension;
	}

	/**
	 * @param date
	 */
	public final void setDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX");
		setDate(date, dateFormat);
	}
	
	/**
	 * @param date
	 * @param dateFormat
	 */
	public final void setDate(Date date, SimpleDateFormat dateFormat) {
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		setDate(date, dateFormat, timeZone);
	}
	
	/**
	 * @param date
	 * @param dateFormat
	 * @param timeZone
	 */
	public final void setDate(Date date, SimpleDateFormat dateFormat, TimeZone timeZone) {
//		dateFormat.setTimeZone(timeZone);
		this.datetime = dateFormat.format(date);
	}

	/**
	 * @param element
	 */
	private final Loggable extractAnnotation(Executable element) {
		Loggable elementAnnotation = element.getAnnotation(Loggable.class);
		if (elementAnnotation != null) {
			return elementAnnotation;
		}

		elementAnnotation = element.getDeclaringClass().getAnnotation(
				Loggable.class);
		if (elementAnnotation != null) {
			return elementAnnotation;
		}

		return null;
	}

	/**
	 * @param array
	 * @return Decoupled array as a String
	 */
	private final String decoupleArray(Object[] array) {
		List<String> decoupled = new ArrayList<String>();
		for (Object obj : array) {
			if (obj instanceof Object[]) {
				decoupled.add(decoupleArray((Object[]) obj));
			} else {
				decoupled.add(obj.toString());
			}
		}
		return decoupled.toString();
	}

}

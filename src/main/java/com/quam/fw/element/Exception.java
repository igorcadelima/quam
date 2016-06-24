package com.quam.fw.element;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author igorcadelima
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@SuppressWarnings("unused")
//@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Exception {
	private String name;
	private String file;
	private int line;
	private String message;
	
	private Exception() {
	}

	private Exception(Throwable exception){
		this.name = exception.getClass().getCanonicalName();
		StackTraceElement ste = exception.getStackTrace()[0];
		this.file = ste.getFileName();
		this.line = ste.getLineNumber();
		this.message = exception.getMessage();
	}
	
	static Exception newInstance(Throwable exception) {
		if (exception != null)
			return new Exception(exception);
			
		else
			return null;
		
	}
}

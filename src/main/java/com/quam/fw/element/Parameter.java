package com.quam.fw.element;

import java.lang.reflect.Modifier;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * @author igorcadelima
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@SuppressWarnings("unused")
public final class Parameter {
	private String type;
	private String name;
	private boolean isFinal;

	Parameter(java.lang.reflect.Parameter param) {
		name = param.getName();
		type = param.getType().getCanonicalName();
		isFinal = Modifier.isFinal(param.getModifiers());
	}

}

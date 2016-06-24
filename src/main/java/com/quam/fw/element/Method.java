package com.quam.fw.element;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * @author igorcadelima
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@SuppressWarnings("unused")
public final class Method {
	private String name;
	private String[] modifiers;
	private List<Parameter> parameters;
	private String returnType;
	private List<String> exceptions;

	Method(java.lang.reflect.Method method) {
		name = method.getName();
		modifiers = getModifiers(method);
		exceptions = new ArrayList<String>();
		parameters = new ArrayList<Parameter>();

		// Matches "class "
		String classRegex = "(class\\s)(.*\\b)";
		// Matches "[L...;"
		String arrayRegex = "(\\[L)(.*\\b)(;)";
		returnType = method.getGenericReturnType().toString()
				.replaceFirst(classRegex, "$2").replaceFirst(arrayRegex, "$2");

		for (java.lang.Class<?> exception : method.getExceptionTypes()) {
			exceptions.add(exception.getName());
		}

		for (java.lang.reflect.Parameter param : method.getParameters()) {
			parameters.add(new Parameter(param));
		}
	}

	/**
	 * @param m
	 * @return
	 */
	private String[] getModifiers(java.lang.reflect.Method m) {
		int encodedModifiers = m.getModifiers();
		if (encodedModifiers > 0) {
			String modifiers = Modifier.toString(m.getModifiers());
			return modifiers.split(" ");
		}
		return new String[0];
	}

}

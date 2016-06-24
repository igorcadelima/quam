package com.quam.fw.element;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * @author igorcadelima
 *
 */
@SuppressWarnings("unused")
//@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public final class Constructor {
	private String[] modifiers;
	private String name;
	private List<Parameter> parameters;
	private List<String> exceptions;

	private Constructor() {
		parameters = new ArrayList<Parameter>();
		exceptions = new ArrayList<String>();
	}

	/**
	 * @param className
	 * @param modifiers
	 * @param parameters
	 * @param exceptions
	 */
	Constructor(String name, String[] modifiers, List<Parameter> parameters,
			List<String> exceptions) {
		this.name = name;
		this.modifiers = modifiers;
		this.parameters = parameters;
		this.exceptions = exceptions;
	}

	Constructor(java.lang.reflect.Constructor<?> constructor) {
		name = constructor.getDeclaringClass().getSimpleName();
		modifiers = getModifiers(constructor);
		exceptions = new ArrayList<String>();
		parameters = new ArrayList<Parameter>();

		for (java.lang.Class<?> exception : constructor.getExceptionTypes()) {
			exceptions.add(exception.getName());
		}

		for (java.lang.reflect.Parameter param : constructor.getParameters()) {
			parameters.add(new Parameter(param));
		}
	}

	/**
	 * @param c
	 * @return
	 */
	private String[] getModifiers(java.lang.reflect.Constructor<?> c) {
		int encodedModifiers = c.getModifiers();
		if (encodedModifiers > 0) {
			String modifiers = Modifier.toString(c.getModifiers());
			return modifiers.split(" ");
		}
		return new String[0];
	}
}

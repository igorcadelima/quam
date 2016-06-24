package com.quam.fw.element;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * @author igorcadelima
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@SuppressWarnings("unused")
public final class Attribute {
	private String name;
	private String type;
	private String[] modifiers;
	
	private Attribute() {
		modifiers = new String[0];
	}
	
	Attribute(Field field) {
		name = field.getName();
		type = field.getGenericType().getTypeName();
		modifiers = getModifiers(field);
	}
	
	/**
	 * @param field
	 * @return
	 */
	private String[] getModifiers(Field field) {
		int encodedModifiers = field.getModifiers();
		if (encodedModifiers > 0) {
			String modifiers = Modifier.toString(field.getModifiers());
			return modifiers.split(" ");
		}
		return new String[0];
	}

}

package com.quam.fw.element;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * @author igorcadelima
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@SuppressWarnings("unused")
//@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public abstract class Element implements LoggableElement {
	private String pkg;
	private String[] modifiers;
	private String type;
	private String name;
	private List<String> interfaces;
	private List<Constructor> constructors;
	private List<Method> methods;
	private List<Attribute> attributes;
	private List<Class> innerClasses;
	private List<Interface> innerInterfaces;
	private List<Enum> innerEnums;

	protected Element() {
		interfaces = new ArrayList<String>();
		constructors = new ArrayList<Constructor>();
		methods = new ArrayList<Method>();
		attributes = new ArrayList<Attribute>();
		innerClasses = new ArrayList<Class>();
		innerInterfaces = new ArrayList<Interface>();
		innerEnums = new ArrayList<Enum>();
	}

	protected Element(java.lang.Class<?> clazz, String type) {
		if (clazz.getPackage() != null) {
			this.pkg = clazz.getPackage().getName();
		} else {
			this.pkg = null;
		}
		this.modifiers = getModifiers(clazz);
		this.type = type;
		this.name = clazz.getSimpleName();
		this.methods = getMethods(clazz);
		this.constructors = getConstructors(clazz);
		this.attributes = getAttributes(clazz);
		this.interfaces = getInterfaces(clazz);

		// Set innerClasses, innerInterfaces and innerEnums
		regInnerMembers(clazz);
	}

	/**
	 * @param clazz
	 * @return
	 */
	private List<Constructor> getConstructors(java.lang.Class<?> clazz) {
		java.lang.reflect.Constructor<?>[] constructors = clazz
				.getConstructors();
		List<Constructor> qConstructors = new ArrayList<Constructor>();

		for (java.lang.reflect.Constructor<?> constructor : clazz
				.getConstructors()) {
			qConstructors.add(new Constructor(constructor));
		}

		return qConstructors;
	}

	/**
	 * @param clazz
	 * @return
	 */
	private String[] getModifiers(java.lang.Class<?> clazz) {
		int encodedModifiers = clazz.getModifiers();
		if (encodedModifiers > 0) {
			String modifiers = Modifier.toString(clazz.getModifiers());
			return modifiers.split(" ");
		}
		return new String[0];
	}

	/**
	 * @param clazz
	 * @return
	 */
	private List<Method> getMethods(java.lang.Class<?> clazz) {
		java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();
		List<Method> qMethods = new ArrayList<Method>();
		Method qMethod;

		for (java.lang.reflect.Method method : methods) {
			if (!method.getName().contains("$")) {
				qMethod = new Method(method);
				qMethods.add(qMethod);
			}
		}
		return qMethods;
	}

	/**
	 * @param clazz
	 * @return
	 */
	private List<Attribute> getAttributes(java.lang.Class<?> clazz) {
		Field[] attrs = clazz.getDeclaredFields();
		List<Attribute> qAttrs = new ArrayList<Attribute>();
		Attribute qAttr;

		for (Field attr : attrs) {
			if (!attr.getName().contains("$")) {
				qAttr = new Attribute(attr);
				qAttrs.add(qAttr);
			}
		}
		return qAttrs;
	}

	/**
	 * @param clazz
	 * @return
	 */
	private List<String> getInterfaces(java.lang.Class<?> clazz) {
		List<String> interfaces = new ArrayList<String>();

		for (Type interfaceEl : clazz.getGenericInterfaces()) {
			interfaces.add(interfaceEl.getTypeName());
		}
		return interfaces;
	}

	/**
	 * @param clazz
	 */
	private void regInnerMembers(java.lang.Class<?> clazz) {
		innerClasses = new ArrayList<Class>();
		innerInterfaces = new ArrayList<Interface>();
		innerEnums = new ArrayList<Enum>();

		for (java.lang.Class<?> c : clazz.getDeclaredClasses()) {
			if (c.isInterface())
				innerInterfaces.add(Interface.newInstance(c));
			else if (c.isEnum())
				innerEnums.add(Enum.newInstance(c));
			else
				innerClasses.add(Class.newInstance(c));
		}
	}

}

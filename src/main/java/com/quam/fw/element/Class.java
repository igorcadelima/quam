package com.quam.fw.element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author igorcadelima
 *
 */
@SuppressWarnings("unused")
final class Class extends Element {
	private String superClass;

	private Class() {
	}

	private Class(java.lang.Class<?> clazz) {
		super(clazz, "class");
		this.superClass = clazz.getSuperclass() == null ? null : clazz
				.getSuperclass().getName();

	}

	/**
	 * @param clazz
	 */
	static Class newInstance(java.lang.Class<?> clazz) {
		if (clazz != null)
			return new Class(clazz);
		else
			return null;

	}

}

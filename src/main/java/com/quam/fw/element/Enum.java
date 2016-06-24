package com.quam.fw.element;

/**
 * @author igorcadelima
 *
 */
final class Enum extends Element {

	private Enum() {
	}

	private Enum(java.lang.Class<?> clazz) {
		super(clazz, "enum");

	}

	static Enum newInstance(java.lang.Class<?> clazz) {
		if (clazz.isEnum())
			return new Enum(clazz);

		return null;

	}

}

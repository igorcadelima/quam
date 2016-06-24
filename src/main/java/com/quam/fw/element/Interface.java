package com.quam.fw.element;

/**
 * @author igorcadelima
 *
 */
final class Interface extends Element {

	private Interface() {
	}

	private Interface(java.lang.Class<?> clazz) {
		super(clazz, "interface");
	}

	static Interface newInstance(java.lang.Class<?> clazz) {
		if (clazz.isInterface())
			return new Interface(clazz);

		return null;
	}

}

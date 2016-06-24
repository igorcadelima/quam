package com.quam.fw.element;

/**
 * Factory class to create Elements (class, interface, enum)
 * 
 * @author igorcadelima
 *
 */
public class ElementFactory {
	ElementFactory() {
	}

	/**
	 * @param clazz
	 * @return
	 */
	public static Element newElement(java.lang.Class<?> clazz) {
		if (clazz == null)
			return null;
		else if (clazz.isInterface())
			return Interface.newInstance(clazz);
		else if (clazz.isEnum())
			return Enum.newInstance(clazz);
		else
			return Class.newInstance(clazz);

	}
}

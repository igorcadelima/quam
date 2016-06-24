package com.quam.fw.core;

/**
 * @author igorcadelima
 *
 */
aspect ElementMonitor extends Monitor {

	/**
	 * Picks out annotated constructors and constructors in an annotated class
	 */
	pointcut loggableConstructor():
		(within(@com.quam.fw.annotations.Loggable *) 
			&& execution(*.*.new(..))
			&& !@annotation(com.quam.fw.annotations.NotLoggable))
		|| (execution(*.*.new(..)) 
			&& @annotation(com.quam.fw.annotations.Loggable) 
			&& !@annotation(com.quam.fw.annotations.NotLoggable));

	/**
	 * Picks out annotated methods and methods in an annotated class
	 */
	pointcut loggableMethod():
		(within(@com.quam.fw.annotations.Loggable *) 
			&& execution(* *.*(..))
			&& !@annotation(com.quam.fw.annotations.NotLoggable))
		|| (execution(* *.*(..)) 
			&& @annotation(com.quam.fw.annotations.Loggable) 
			&& !@annotation(com.quam.fw.annotations.NotLoggable));

	public pointcut customAdvice(): loggableMethod() || loggableConstructor();

	before() : customAdvice() {
		setUp(thisJoinPoint);
		loadDefaultLog();
	}

	after() : customAdvice() {
		logDown();
	}

}

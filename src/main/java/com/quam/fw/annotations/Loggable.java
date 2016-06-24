package com.quam.fw.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author igorcadelima
 *
 */
@Loggable
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface Loggable {

	/**
	 * TRACE level of logging.
	 */
//	final int TRACE = 0;

	/**
	 * DEBUG level of logging.
	 */
//	final int DEBUG = 1;

	/**
	 * INFO level of logging.
	 */
//	final int INFO = 2;

	/**
	 * WARN level of logging.
	 */
//	final int WARN = 3;

	/**
	 * ERROR level of logging.
	 */
//	final int ERROR = 4;

	/**
	 * Minimum amount allowed for this method. The method will not be logged if
	 * the elapsed CPU time is lesser than maxCpuTime.
	 */
	float minCpuTime() default 0; // Seconds

	/**
	 * Maximum amount allowed for this method. The method will not be logged if
	 * the elapsed CPU time is greater than maxCpuTime.
	 */
	float maxCpuTime() default 60; // Seconds

	/**
	 * Minimum amount allowed for this method. The method will not be logged if
	 * the elapsed wall time is lesser than maxWallTime.
	 */
	float minWallTime() default 0; // Seconds
	
	/**
	 * Maximum amount allowed for this method. The method will not be logged if
	 * the elapsed wall time is greater than maxWallTime.
	 */
	float maxWallTime() default 60; // Seconds

	/**
	 * Show the amount of time for which the CPU was used for processing the
	 * element
	 */
	boolean cpuTime() default true;

	/**
	 * Show the amount of time elapsed between the entry point and exit of the
	 * element. As it's very dependent on what the machine is currently
	 * performing, the wall time is <= cpu time
	 */
	boolean wallTime() default true;

	/**
	 * The precision (number of fractional digits) to be used when calculating
	 * the measured execution time when converted to seconds (both cpu and wall
	 * time).
	 */
//	int precision() default 5;

	/**
	 * List of exception types, which should not be logged if thrown.
	 */
	Class<? extends Throwable>[] ignore() default {};

	/**
	 * List of exception types, which should be caught/captured if thrown. The
	 * capture list has precedence over the ignore list, that is, any exception
	 * class which has been passed as argument of ignore() and capture at the
	 * same time will be caught
	 */
	Class<? extends Throwable>[] capture() default {};

	/**
	 * Time unit for cpuTimeLimit
	 */
	// TimeUnit cpuTimeLimitUnit() default TimeUnit.NANOSECONDS;

	/**
	 * Time unit for cpuLimit
	 */
	// TimeUnit wallTimeLimitUnit() default TimeUnit.NANOSECONDS;

	/**
	 * Time unit
	 */
	// TimeUnit unit() default TimeUnit.SECONDS;

	/**
	 * Level of logging.
	 */
	// int level() default Loggable.INFO;

	/**
	 * Method entry moment should be reported as well (by default only an exit
	 * moment is reported).
	 */
	// boolean prepend() default false;

	/**
	 * Log result, if any
	 */
	// boolean result() default true;

	/**
	 * Log arguments, if any
	 */
	// boolean args() default true;

	/**
	 * Add toString() result to log line.
	 */
	// boolean logThis() default false;

	/**
	 * The name of the logger to be used. If not specified, defaults to the
	 * class name of the annotated class or method.
	 */
	// String name() default "";

}

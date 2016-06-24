package com.quam.fw.core;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

import com.quam.fw.annotations.Loggable;
import com.quam.fw.element.DefaultLog;
import com.quam.fw.logger.LogHelper;

/**
 * @author igorcadelima
 *
 */
public abstract aspect Monitor percflow(customAdvice()){

	private Object[] args;
	private Executable element;
	private Throwable exception;
	private DefaultLog log;
	private long totalCpuTime;
	private long totalWallTime;
	private Object result;
	private List<Short> attributes;
	private Date date;

	protected abstract pointcut customAdvice();

	Object around() : customAdvice() {

		// Local variables declaration
		long initialWallTime = 0;
		long initialCpuTime = 0;
		ThreadMXBean thread = ManagementFactory.getThreadMXBean();

		try {
			initialWallTime = System.nanoTime();
			initialCpuTime = thread.getCurrentThreadCpuTime();
			result = proceed();
		} finally {
			totalWallTime = System.nanoTime() - initialWallTime;
			totalCpuTime = thread.getCurrentThreadCpuTime() - initialCpuTime;
		}

		return result;
	}

	after() throwing (Throwable exception) : customAdvice() {
		this.exception = exception;
	}

	/**
	 * @param joinPoint
	 */
	protected final void setUp(JoinPoint joinPoint) {

		// Variables initialisation
		args = joinPoint.getArgs();
		attributes = new ArrayList<Short>();
		date = new Date();
		log = new DefaultLog();
		totalCpuTime = totalWallTime = 0;

		CodeSignature signature = CodeSignature.class.cast(joinPoint
				.getSignature());

		// Method or constructor?
		if (signature instanceof MethodSignature) {
			element = MethodSignature.class.cast(signature).getMethod();
		} else if (signature instanceof ConstructorSignature) {
			element = ConstructorSignature.class.cast(signature)
					.getConstructor();
		}
	}

	/**
	 * @param joinPoint
	 */
	protected final void logDown() {
		if (matchRequirements(element, totalCpuTime, totalWallTime, exception)) {

			fillLogWithData();

			if (log.getException() == null) {
				LogHelper.log(LogHelper.AUDIT, log);
			} else {
				LogHelper.log(LogHelper.EXCEPTION, log);
			}
		}
	}

	/**
	 * 
	 */
	private void fillLogWithData() {
		for (short attributeId : attributes) {
			switch (attributeId) {
			case DefaultLog.DATE:
				log.setDate(date);
				break;
			case DefaultLog.EXECUTABLE:
				log.setExecutable(element);
				break;
			case DefaultLog.ARGS:
				log.setArgs(element.getParameters(), args);
				break;
			case DefaultLog.CPU_TIME:
				log.setCpuTime(totalCpuTime);
				break;
			case DefaultLog.WALL_TIME:
				log.setWallTime(totalWallTime);
				break;
			case DefaultLog.RETURN_TYPE:
				if (element.getClass().equals(Method.class)) {
					Method method = (Method) element;
					log.setReturnType(method.getGenericReturnType());
				}
				break;
			case DefaultLog.RESULT:
				log.setResult(result);
				break;
			case DefaultLog.EXCEPTION:
				log.setException(exception);
				break;
//			default:
//				throw new InvalidParameterException(
//						"The \"attributeId\" passed as argument is not valid. Please take a look at the static variable of DefaultLog.");
			}
		}
	}

	/**
	 * 
	 */
	protected void loadDefaultLog() {
		addAttribute(DefaultLog.DATE);
		addAttribute(DefaultLog.EXECUTABLE);
		addAttribute(DefaultLog.ARGS);
		addAttribute(DefaultLog.CPU_TIME);
		addAttribute(DefaultLog.WALL_TIME);
		addAttribute(DefaultLog.RETURN_TYPE);
		addAttribute(DefaultLog.RESULT);
		addAttribute(DefaultLog.EXCEPTION);
	}

	/**
	 * @param attributeId
	 */
	protected final void addAttribute(short attributeId) {
		this.attributes.add(attributeId);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	protected final void setExtensionObject(Object extension) {
		this.log.setExtension(extension);
	}
	
	/**
	 * @param element
	 * @param totalCpuTime
	 * @param totalWallTime
	 * @param exception
	 * @return
	 */
	private boolean matchRequirements(Executable element, long totalCpuTime,
			long totalWallTime, Throwable exception) {
		
		
		// Get the most specific annotation
		Loggable annotation = mostSpecificAnnotationOf(element);
		if (annotation != null) {
			
			if (!matchTimeRequirements(totalCpuTime, totalWallTime, annotation)) {
				return false;
			}

			if (exception != null) {
				for (Class<? extends Throwable> type : annotation.capture()) {
					if (isCause(type, exception))
						return true;
				}

				for (Class<? extends Throwable> type : annotation.ignore()) {
					if (isCause(type, exception))
						return false;
				}
			}
		}

		// Custom logger
		return true;
	}

	/**
	 * @param element
	 * @return Most specific annotation of element or null if annotation is not
	 *         found
	 */
	private Loggable mostSpecificAnnotationOf(Executable element) {
		
		Loggable annotation = element.getAnnotation(Loggable.class);

		if (annotation == null)
			annotation = element.getDeclaringClass().getAnnotation(
					Loggable.class);

		return annotation;
	}

	private boolean matchTimeRequirements(long totalCpuTime,
			long totalWallTime, Loggable annotation) {

		double minCpuTime = annotation.minCpuTime() * 1000000000;
		double maxCpuTime = annotation.maxCpuTime() * 1000000000;
		double minWallTime = annotation.minWallTime() * 1000000000;
		double maxWallTime = annotation.maxWallTime() * 1000000000;

		if ((minCpuTime > totalCpuTime) || (totalCpuTime > maxCpuTime)
				|| (minWallTime > totalWallTime)
				|| (totalWallTime > maxWallTime)) {
			return false;
		}

		return true;
	}

	/**
	 * @param expected
	 * @param exc
	 * @return True if expected is the cause (or ancestor) of exception
	 */
	private boolean isCause(Class<? extends Throwable> expected,
			Throwable exception) {
		return expected.isInstance(exception)
				|| (exception != null && isCause(expected, exception.getCause()));
	}

}

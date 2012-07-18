package com.sogou.bizdev.limit.wrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.sogou.bizdev.limit.annotation.TimeUnitLimitCall.TimeUnit;
import com.sun.org.apache.bcel.internal.util.ClassLoader;

public abstract class AbstractLimitWrapper {
	private Set<String> packagesToScan;
	private Map<String, Map<String, Object>> specifiedMethods;
	private AtomicBoolean init = new AtomicBoolean(false);
	protected static final Logger LOGGER = Logger.getLogger(AbstractLimitWrapper.class);
	public static final String METHOD_KEY = "method";
	public static final String LIMIT_KEY = "limitValue";
	public static final String TIME_UNIT_KEY = "timeUnit";
	public static final String TIME_OUT_KEY = "timeout";
	public static final String ASYNC_KEY = "async";
	protected final Map<Method, Boolean> limitMap = new ConcurrentHashMap<Method, Boolean>();
	protected abstract void regiestMethod(Method method);

	protected abstract void regiestMethodWithoutAnnotation(Method method, Long value, TimeUnit timeUnit, boolean async, Long timeout);

	protected abstract Class<? extends Annotation> getAnnotation();

	public void init() {
		if (!init.get()) {
			LOGGER.info("init start.");
			if ((packagesToScan == null || packagesToScan.isEmpty()) && (specifiedMethods == null || specifiedMethods.isEmpty())) {
				LOGGER.error("param invalidate:packagesToScan and specifiedMethods can't both be empty");
				throw new IllegalArgumentException("packagesToScan and specifiedMethods can't both be empty!");
			} else {
				synchronized (this) {
					if (packagesToScan != null && packagesToScan.size() > 0) {
						LOGGER.info("init from packageToScan:" + packagesToScan);
						for (String name : packagesToScan) {
							Reflections reflections = new Reflections(new ConfigurationBuilder().addUrls(
									ClasspathHelper.forPackage(name, new ClassLoader[] {})).setScanners(new MethodAnnotationsScanner()));
							Set<Method> annotMethods = reflections.getMethodsAnnotatedWith(getAnnotation());
							for (Method method : annotMethods) {
								LOGGER.info("regiest method:" + method.getName());
								regiestMethod(method);
							}
						}
					}
					// Support mix mode.
					if (specifiedMethods != null && specifiedMethods.size() > 0) {
						LOGGER.info("init from specifiedMethods.");
						for (String className : specifiedMethods.keySet()) {
							try {
								Class<?> class1 = Class.forName(className);
								Method[] declareMethods = class1.getDeclaredMethods();
								for (Method method : declareMethods) {
									Set<String> regiestMethods = (Set<String>) specifiedMethods.get(className).get(METHOD_KEY);
									if (regiestMethods.contains(method.getName())) {
										LOGGER.info("regiest method:" + className + "." + method.getName());
										regiestMethodWithoutAnnotation(
												method,
												(Long) specifiedMethods.get(className).get(LIMIT_KEY),
												TimeUnit.valueOf(((String) specifiedMethods.get(className).get(TIME_UNIT_KEY)).toUpperCase()),
												(specifiedMethods.get(className).get(ASYNC_KEY) == null ? true : (Boolean) specifiedMethods.get(
														className).get(ASYNC_KEY)), (specifiedMethods.get(className).get(TIME_OUT_KEY) == null ? -1
														: (Long) specifiedMethods.get(className).get(TIME_OUT_KEY)));
									}
								}
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			init.set(true);
			LOGGER.info("init finished.");
		}
	}

	public Set<String> getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(Set<String> packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public Map<String, Map<String, Object>> getSpecifiedMethods() {
		return specifiedMethods;
	}

	public void setSpecifiedMethods(Map<String, Map<String, Object>> specifiedMethods) {
		this.specifiedMethods = specifiedMethods;
	}

	public AtomicBoolean getInit() {
		return init;
	}
}

package com.sogou.bizdev.limit.wrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.sogou.bizdev.limit.annotation.InvokeeLimitCall;
import com.sogou.bizdev.limit.annotation.TimeUnitLimitCall.TimeUnit;
import com.sogou.bizdev.limit.core.InvokerLimitProcessor;
import com.sogou.bizdev.limit.exception.BizdevIncokeLimitationException;

public class InvokeLimitWrapper extends AbstractLimitWrapper {
	private InvokerLimitProcessor invokeLimitProcessor = InvokerLimitProcessor.getInstance();

	@Override
	protected void regiestMethod(Method method) {
		InvokeeLimitCall invokeLimit = method.getAnnotation(InvokeeLimitCall.class);
		LOGGER.info(String.format("Method[%s] regiest with value[%s], timeUnit[%s], ns per request[%s]", method.getName(),
				invokeLimit.value(), invokeLimit.unit(), invokeLimit.unit().getNs() / invokeLimit.value()));
		invokeLimitProcessor.regiest(method, invokeLimit);
	}

	@Override
	protected void regiestMethodWithoutAnnotation(Method method, Long value, TimeUnit timeUnit, boolean async, Long timeout) {
		LOGGER.info(String.format("Method[%s] regiest with value[%s], timeUnit[%s], ns per request[%s]", method.getName(), value, timeUnit,
				timeUnit.getNs() / value));
		invokeLimitProcessor.regiest(method, value, timeUnit);
	}

	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return InvokeeLimitCall.class;
	}

	/**
	 * 适用于用户使用AOP来对方法进行拦截实现，该方法约定，所有目标对象的第一个参数作为标识的key
	 * 
	 * @param jp
	 * @throws BizdevIncokeLimitationException
	 */
	public void beforeTargetInvoked(JoinPoint jp) throws BizdevIncokeLimitationException {
		if (!getInit().get()) {
			init();
		}
		MethodSignature methodSignature = (MethodSignature) jp.getSignature();
		if (shouldIntercept(methodSignature.getMethod())) {
			if (jp.getArgs() == null || jp.getArgs().length < 1) {
				throw new IllegalArgumentException("Target method required at least one param.");
			}
			beforeTargetInvokedManual(methodSignature.getMethod(), jp.getArgs()[0]);
		}
	}

	/**
	 * 适用于用户在程序中手动调用的情景
	 * 
	 * @param method
	 */
	public void beforeTargetInvokedManual(Method method, Object key) {
		if (!getInit().get()) {
			init();
		}
		invokeLimitProcessor.beforeInvoke(method, key);
	}

	private boolean shouldIntercept(Method method) {
		if (!limitMap.containsKey(method)) {
			limitMap.put(method, InvokerLimitProcessor.isInvokeLimitMethod(method));
		}
		return limitMap.get(method);
	}
}

package github.macrohuang.limit.wrapper;

import github.macrohuang.limit.annotation.SyncCall;
import github.macrohuang.limit.annotation.TimeUnitLimitCall.TimeUnit;
import github.macrohuang.limit.core.SyncCallProcessor;
import github.macrohuang.limit.exception.BizdevIncokeLimitationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;


public class SyncCallWrapper extends AbstractLimitWrapper {
	private SyncCallProcessor syncCallProcessor = SyncCallProcessor.getInstance();

	@Override
	protected void regiestMethod(Method method) {
		SyncCall syncCall = method.getAnnotation(SyncCall.class);
		LOGGER.info(String.format("Method[%s] regiest with sync,timeOut[%s]", method.getName(), syncCall.timeout()));
		syncCallProcessor.regiest(method, syncCall);
	}

	@Override
	protected void regiestMethodWithoutAnnotation(Method method, Long value, TimeUnit timeUnit, boolean async, Long timeout) {
		LOGGER.info(String.format("Method[%s] regiest with sync,timeOut[%s]", method.getName(), timeout));
		syncCallProcessor.regiest(method, timeout);
	}

	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return SyncCall.class;
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
		syncCallProcessor.beforeInvoke(method, key);
	}

	/**
	 * 适用于用户使用AOP来对方法进行拦截实现，该方法约定，所有目标对象的第一个参数作为标识的key
	 * 
	 * @param jp
	 * @throws BizdevIncokeLimitationException
	 */
	public void afterTargetInvoked(JoinPoint jp) throws BizdevIncokeLimitationException {
		if (!getInit().get()) {
			init();
		}
		MethodSignature methodSignature = (MethodSignature) jp.getSignature();
		if (shouldIntercept(methodSignature.getMethod())) {
			if (jp.getArgs() == null || jp.getArgs().length < 1) {
				throw new IllegalArgumentException("Target method required at least one param.");
			}
			afterTargetInvokedManual(methodSignature.getMethod(), jp.getArgs()[0]);
		}
	}

	/**
	 * 适用于用户在程序中手动调用的情景
	 * 
	 * @param method
	 */
	public void afterTargetInvokedManual(Method method, Object key) {
		if (!getInit().get()) {
			init();
		}
		syncCallProcessor.afterReturn(method, key);
	}

	private boolean shouldIntercept(Method method) {
		if (!limitMap.containsKey(method)) {
			limitMap.put(method, SyncCallProcessor.isSyncCallMethod(method));
		}
		return limitMap.get(method);
	}
}

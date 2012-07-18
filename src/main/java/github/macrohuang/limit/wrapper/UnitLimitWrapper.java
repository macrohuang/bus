package github.macrohuang.limit.wrapper;

import github.macrohuang.limit.annotation.TimeUnitLimitCall;
import github.macrohuang.limit.annotation.TimeUnitLimitCall.TimeUnit;
import github.macrohuang.limit.core.TimeUnitLimitProcessor;
import github.macrohuang.limit.exception.BizdevIncokeLimitationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;


public class UnitLimitWrapper extends AbstractLimitWrapper {
	private TimeUnitLimitProcessor unitLimitProcessor = TimeUnitLimitProcessor.getInstance();

	@Override
	protected void regiestMethod(Method method) {
		TimeUnitLimitCall unitLimit = method.getAnnotation(TimeUnitLimitCall.class);
		LOGGER.info(String.format("Method[%s] regiest with value[%s], timeUnit[%s], ns per request[%s]", method.getName(), unitLimit.value(),
				unitLimit.unit(), unitLimit.unit().getNs() / unitLimit.value()));
		unitLimitProcessor.regiest(method, unitLimit.value(), unitLimit.unit());
	}

	@Override
	protected void regiestMethodWithoutAnnotation(Method method, Long value, TimeUnit timeUnit, boolean async, Long timeout) {
		LOGGER.info(String.format("Method[%s] regiest with value[%s], timeUnit[%s], ns per request[%s]", method.getName(), value, timeUnit,
				timeUnit.getNs() / value));
		unitLimitProcessor.regiest(method, value, timeUnit);
	}

	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return TimeUnitLimitCall.class;
	}

	/**
	 * 适用于用户使用AOP来对方法进行拦截实现
	 * 
	 * @param jp
	 * @throws BizdevIncokeLimitationException
	 */
	public void beforeTargetInvoked(JoinPoint jp) throws BizdevIncokeLimitationException {
		if (!getInit().get()) {
			init();
		}
		MethodSignature methodSignature = (MethodSignature) jp.getSignature();
		if (methodSignature.getMethod().getAnnotation(TimeUnitLimitCall.class) == null)
			return;
		beforeTargetInvokedManual(methodSignature.getMethod());
	}

	/**
	 * 适用于用户在程序中手动调用的情景
	 * 
	 * @param method
	 */
	public void beforeTargetInvokedManual(Method method) {
		if (!getInit().get()) {
			init();
		}
		unitLimitProcessor.beforeInvoke(method);
	}
}

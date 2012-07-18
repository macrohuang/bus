package com.sogou.bizdev.limit.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.sogou.bizdev.limit.annotation.InvokeeLimitCall;
import com.sogou.bizdev.limit.annotation.TimeUnitLimitCall.TimeUnit;
import com.sogou.bizdev.limit.constants.ErrorCode;
import com.sogou.bizdev.limit.exception.BizdevIncokeLimitationException;
import com.sogou.bizdev.limit.struct.InvokeDataStruct;

public final class InvokerLimitProcessor {
	private static final Map<String, InvokeDataStruct> INVOKE_LIMIT_MAP = new ConcurrentHashMap<String, InvokeDataStruct>();
	private static final Logger LOGGER = Logger.getLogger(TimeUnitLimitProcessor.class);

	private static final InvokerLimitProcessor INSTANCE = new InvokerLimitProcessor();

	private InvokerLimitProcessor() {

	}

	public static InvokerLimitProcessor getInstance() {
		return INSTANCE;
	}

	public static boolean isInvokeLimitMethod(Method method) {
		return ProcessorHelper.hasInvokeLimitAnnotation(method) || INVOKE_LIMIT_MAP.containsKey(ProcessorHelper.getKey(method));
	}

	public boolean regiest(Method method, InvokeeLimitCall invokeLimit) {
		return INVOKE_LIMIT_MAP.put(ProcessorHelper.getKey(method), new InvokeDataStruct(invokeLimit.unit().getNs() / invokeLimit.value())) != null;
	}

	public boolean regiest(Method method, long limit, TimeUnit timeUnit) {
		return INVOKE_LIMIT_MAP.put(ProcessorHelper.getKey(method), new InvokeDataStruct(timeUnit.getNs() / limit)) != null;
	}

	public boolean unregiest(Method method) {
		return INVOKE_LIMIT_MAP.remove(ProcessorHelper.getKey(method)) != null;
	}

	public void beforeInvoke(Method method, Object key) throws BizdevIncokeLimitationException {
		if (!INVOKE_LIMIT_MAP.containsKey(ProcessorHelper.getKey(method))) {
			LOGGER.error("Method regiest required limit:" + ProcessorHelper.getKey(method));
			return;
		}
		process(method, INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method)), key);
	}

	private void process(Method method, InvokeDataStruct dataStruct, Object key) {
		if (dataStruct.getLastAccessTime(key) == null) {
			dataStruct.setLastAccessTime(key, 0l);
		}
		long currentNs = System.nanoTime();
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("dif:" + (currentNs - dataStruct.getLastAccessTime(key)));
		}

		if (currentNs - dataStruct.getLastAccessTime(key) < dataStruct.getValue()) {
			throw new BizdevIncokeLimitationException(ErrorCode.UnitLimitExceed, String.format("[%s]对方法[%s]的请求过于频繁，请过[%s]ns再访问", key,
					method.getName(), (System.nanoTime() - dataStruct.getLastAccessTime(key))));
		} else {
			dataStruct.setLastAccessTime(key, currentNs);
		}
	}
}

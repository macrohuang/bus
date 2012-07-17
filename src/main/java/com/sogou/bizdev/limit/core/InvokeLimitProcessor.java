package com.sogou.bizdev.limit.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.sogou.bizdev.limit.annotation.InvokeLimit;
import com.sogou.bizdev.limit.annotation.UnitLimit.TimeUnit;
import com.sogou.bizdev.limit.constants.ErrorCode;
import com.sogou.bizdev.limit.exception.BizdevIncokeLimitationException;
import com.sogou.bizdev.limit.struct.InvokeDataStruct;

public final class InvokeLimitProcessor {
	private static final Map<String, InvokeDataStruct> INVOKE_LIMIT_MAP = new ConcurrentHashMap<String, InvokeDataStruct>();
	private static final Logger LOGGER = Logger.getLogger(UnitLimitProcessor.class);

	private static final InvokeLimitProcessor INSTANCE = new InvokeLimitProcessor();

	private InvokeLimitProcessor() {

	}

	public static InvokeLimitProcessor getInstance() {
		return INSTANCE;
	}

	public boolean regiest(Method method, InvokeLimit invokeLimit) {
		return INVOKE_LIMIT_MAP.put(ProcessorHelper.getKey(method), new InvokeDataStruct(invokeLimit.unit().getNs() / invokeLimit.value(),
				invokeLimit.async(), (invokeLimit.timeOut() != -1 ? invokeLimit.timeOut() * (long) Math.pow(10, 6) : invokeLimit.timeOut()))) != null;
	}

	public boolean regiest(Method method, long limit, TimeUnit timeUnit, boolean async, Long timeout) {
		return INVOKE_LIMIT_MAP.put(ProcessorHelper.getKey(method),
 new InvokeDataStruct(timeUnit.getNs() / limit, async, (timeout == -1 ? timeout
				: timeout * (long) Math.pow(10, 6)))) != null;
	}

	public boolean unregiest(Method method) {
		return INVOKE_LIMIT_MAP.remove(ProcessorHelper.getKey(method)) != null;
	}

	public void beforeInvoke(Method method, Object key) throws BizdevIncokeLimitationException {
		if (!INVOKE_LIMIT_MAP.containsKey(ProcessorHelper.getKey(method))) {
			LOGGER.error("Method regiest required limit:" + ProcessorHelper.getKey(method));
			return;
		}
		if (processSync(method, INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method)), key))
			return;
		processNormal(method, INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method)), key);
	}

	public void afterReturn(Method method, Object key) throws BizdevIncokeLimitationException {
		if (!INVOKE_LIMIT_MAP.containsKey(ProcessorHelper.getKey(method))) {
			LOGGER.error("Method regiest required limit:" + ProcessorHelper.getKey(method));
			return;
		}
		if (INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method)).isAsync()) {
			return;
		} else {
			INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method)).removeAccessTime(key);
		}
	}

	private boolean processSync(Method method, InvokeDataStruct dataStruct, Object key) {
		if (dataStruct.isAsync()) {
			return false;
		} else {
			if (dataStruct.getLastAccessTime(key) == null) {// not invoke yet
				dataStruct.setLastAccessTime(key, System.nanoTime());
				return true;
			} else {
				if (dataStruct.getTimeout() != -1L && System.nanoTime() - dataStruct.getLastAccessTime(key) > dataStruct.getTimeout()) {// timeout
					dataStruct.setLastAccessTime(key, System.nanoTime());
					return true;
				} else {
					throw new BizdevIncokeLimitationException(ErrorCode.InvokeLimitExceed, String.format(
							"方法[%s]是一个同步方法，[%s]的请求还未返回，请等方法返回后或者过[%s]ns再访问", method.getName(), key,
							(System.nanoTime() - dataStruct.getLastAccessTime(key))));
				}
			}
		}
	}

	private void processNormal(Method method, InvokeDataStruct dataStruct, Object key) {
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

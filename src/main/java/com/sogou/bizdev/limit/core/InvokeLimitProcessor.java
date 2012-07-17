package com.sogou.bizdev.limit.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.sogou.bizdev.limit.annotation.InvokeLimit;
import com.sogou.bizdev.limit.annotation.UnitLimit.TimeUnit;
import com.sogou.bizdev.limit.constants.ErrorCode;
import com.sogou.bizdev.limit.exception.BizdevIncokeLimitationException;

public final class InvokeLimitProcessor {
	// long[0]: 平均请求时间间隔，单位是ns
	// long[1]: 上一次请求的时间点，ns
	private static final Map<String, Object[]> INVOKE_LIMIT_MAP = new ConcurrentHashMap<String, Object[]>();
	private static final Logger LOGGER = Logger.getLogger(UnitLimitProcessor.class);

	private static final InvokeLimitProcessor INSTANCE = new InvokeLimitProcessor();

	private InvokeLimitProcessor() {

	}

	public static InvokeLimitProcessor getInstance() {
		return INSTANCE;
	}

	public boolean regiest(Method method, InvokeLimit invokeLimit) {
		return INVOKE_LIMIT_MAP.put(ProcessorHelper.getKey(method), new Object[] { invokeLimit.unit().getNs() / invokeLimit.value(),
				new HashMap<Object, Long>(), invokeLimit.async(), (long) Math.pow(invokeLimit.timeOut() * 10, 9) }) != null;
	}

	public boolean regiest(Method method, long limit, TimeUnit timeUnit, boolean async, Long timeout) {
		return INVOKE_LIMIT_MAP.put(ProcessorHelper.getKey(method), new Object[] { timeUnit.getNs() / limit, new HashMap<Object, Long>(), async,
				(long) Math.pow(timeout * 10, 9) }) != null;
	}

	public boolean unregiest(Method method) {
		return INVOKE_LIMIT_MAP.remove(ProcessorHelper.getKey(method)) != null;
	}

	@SuppressWarnings("unchecked")
	public void beforeInvoke(Method method, Object key) throws BizdevIncokeLimitationException {
		if (!INVOKE_LIMIT_MAP.containsKey(ProcessorHelper.getKey(method))) {
			LOGGER.error("Method regiest required limit:" + ProcessorHelper.getKey(method));
			return;
		}
		if (processSync(method, key))
			return;
		if (((Map<Object, Long>) INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]).get(key) == null) {
			((Map<Object, Long>) INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]).put(key, 0l);
		}
		long currentNs = System.nanoTime();
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("dif:" + (currentNs - ((Map<Object, Long>) INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]).get(key)));
		}

		if ((currentNs - ((Map<Object, Long>) INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]).get(key)) < (Long) INVOKE_LIMIT_MAP
				.get(ProcessorHelper.getKey(method))[0]) {
			throw new BizdevIncokeLimitationException(ErrorCode.UnitLimitExceed);
		} else {
			((Map<Object, Long>) INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]).put(key, currentNs);
		}
	}

	@SuppressWarnings("unchecked")
	private boolean processSync(Method method, Object key) {
		if (!(Boolean) INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method))[2]) {
			if (((Map<Object, Long>) INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]).get(key) == null) {// not
																													// invoke
																													// yet
				((Map<Object, Long>) INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]).put(key, System.nanoTime());
				return true;
			} else {
				Long lastAccess = ((Map<Object, Long>) INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]).get(key);
				if (System.nanoTime() - lastAccess > (Long) INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method))[3]) {// timeout
					((Map<Object, Long>) INVOKE_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]).put(key, System.nanoTime());
					return true;
				} else {
					throw new BizdevIncokeLimitationException(ErrorCode.InvokeLimitExceed,
 String.format(
							"方法[%s]是一个同步方法，[%s]的请求还未返回，请等方法返回后或者过[%s]ms再访问", method.getName(), key,
							(System.nanoTime() - lastAccess) / (long) Math.pow(10, 6)));
				}
			}
		} else {
			return false;
		}
	}
}

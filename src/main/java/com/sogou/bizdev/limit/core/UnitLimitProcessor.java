package com.sogou.bizdev.limit.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.sogou.bizdev.limit.annotation.UnitLimit.TimeUnit;
import com.sogou.bizdev.limit.constants.ErrorCode;
import com.sogou.bizdev.limit.exception.BizdevIncokeLimitationException;

public final class UnitLimitProcessor {

	// long[0]: 平均请求时间间隔，单位是ns
	// long[1]: 上一次请求的时间点，ns
	private static final Map<String, long[]> UNIT_LIMIT_MAP = new ConcurrentHashMap<String, long[]>();
	private static final Logger LOGGER = Logger.getLogger(UnitLimitProcessor.class);

	private static final UnitLimitProcessor INSTANCE = new UnitLimitProcessor();
	private UnitLimitProcessor() {

	}

	public static UnitLimitProcessor getInstance() {
		return INSTANCE;
	}

	public boolean regiest(Method method, long limit, TimeUnit timeUnit) {
		return UNIT_LIMIT_MAP.put(ProcessorHelper.getKey(method), new long[] { timeUnit.getNs() / limit, 0 }) != null;
	}

	public boolean unregiest(Method method) {
		return UNIT_LIMIT_MAP.remove(ProcessorHelper.getKey(method)) != null;
	}

	public void beforeInvoke(Method method) throws BizdevIncokeLimitationException {
		if (!UNIT_LIMIT_MAP.containsKey(ProcessorHelper.getKey(method))) {
			LOGGER.error("Method regiest required limit:" + ProcessorHelper.getKey(method));
			return;
		}
		long currentNs = System.nanoTime();
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("dif:" + (currentNs  - UNIT_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]));
		}
		if ((currentNs  - UNIT_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]) < UNIT_LIMIT_MAP.get(ProcessorHelper.getKey(method))[0]) {
			throw new BizdevIncokeLimitationException(ErrorCode.UnitLimitExceed);
		} else {
			UNIT_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1] = currentNs;
		}
	}
}

package com.sogou.bizdev.limit.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.sogou.bizdev.limit.annotation.AnnotationUtils;
import com.sogou.bizdev.limit.constants.ErrorCode;
import com.sogou.bizdev.limit.exception.BizdevIncokeLimitationException;

public final class GlobalLimitProcessor {
	// int[0]: limit value
	// int[1]: current value
	private static final Map<String, int[]> GLOBAL_LIMIT_MAP = new ConcurrentHashMap<String, int[]>();
	private static final Logger LOGGER = Logger.getLogger(GlobalLimitProcessor.class);
	private static final GlobalLimitProcessor INSTANCE = new GlobalLimitProcessor();

	private GlobalLimitProcessor() {

	}

	public static GlobalLimitProcessor getInstance() {
		return INSTANCE;
	}

	public boolean regiest(Method method, int limit) {
		if (AnnotationUtils.isGlobalLimitMethod(method)) {
			GLOBAL_LIMIT_MAP.put(ProcessorHelper.getKey(method), new int[] { limit, 0 });
			return true;
	}
		return false;
	}

	public boolean unregiest(Method method) {
		return GLOBAL_LIMIT_MAP.remove(ProcessorHelper.getKey(method)) != null;
	}

	public void beforeInvoke(Method method) throws BizdevIncokeLimitationException {
		if (!GLOBAL_LIMIT_MAP.containsKey(ProcessorHelper.getKey(method))) {
			LOGGER.error("Method regiest required limit:" + ProcessorHelper.getKey(method));
			return;
		}
		GLOBAL_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]++;
		if (GLOBAL_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1] > GLOBAL_LIMIT_MAP.get(ProcessorHelper.getKey(method))[0]) {
			throw new BizdevIncokeLimitationException(ErrorCode.GlobalLimitExceed);
		}
	}

	public void afterReturn(Method method) {
		if (!GLOBAL_LIMIT_MAP.containsKey(ProcessorHelper.getKey(method))) {
			LOGGER.error("Method regiest required limit:" + ProcessorHelper.getKey(method));
			return;
		}
		GLOBAL_LIMIT_MAP.get(ProcessorHelper.getKey(method))[1]--;
	}
}

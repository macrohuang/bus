package github.macrohuang.limit.core;

import github.macrohuang.limit.annotation.TimeUnitLimitCall.TimeUnit;
import github.macrohuang.limit.constants.ErrorCode;
import github.macrohuang.limit.exception.BizdevIncokeLimitationException;
import github.macrohuang.limit.struct.UnitDataStruct;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;


public final class TimeUnitLimitProcessor {

	private static final Map<String, UnitDataStruct> UNIT_LIMIT_MAP = new ConcurrentHashMap<String, UnitDataStruct>();
	private static final Logger LOGGER = Logger.getLogger(TimeUnitLimitProcessor.class);

	private static final TimeUnitLimitProcessor INSTANCE = new TimeUnitLimitProcessor();
	private TimeUnitLimitProcessor() {

	}

	public static TimeUnitLimitProcessor getInstance() {
		return INSTANCE;
	}

	public static boolean isTimeUnitLimitMethod(Method method) {
		return ProcessorHelper.hasTimeUnitLimitAnnotation(method) || UNIT_LIMIT_MAP.containsKey(ProcessorHelper.getKey(method));
	}

	public boolean regiest(Method method, long limit, TimeUnit timeUnit) {
		return UNIT_LIMIT_MAP.put(ProcessorHelper.getKey(method), new UnitDataStruct(timeUnit.getNs() / limit, 0l, limit)) != null;
	}

	public boolean unregiest(Method method) {
		return UNIT_LIMIT_MAP.remove(ProcessorHelper.getKey(method)) != null;
	}

	public void beforeInvoke(Method method) throws BizdevIncokeLimitationException {
		if (!UNIT_LIMIT_MAP.containsKey(ProcessorHelper.getKey(method))) {
			LOGGER.error("Method regiest required limit:" + ProcessorHelper.getKey(method));
			return;
		}
		UnitDataStruct dataStruct = UNIT_LIMIT_MAP.get(ProcessorHelper.getKey(method));
		long currentNs = System.nanoTime();
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("dif:" + (currentNs - dataStruct.getLastAccessTime()));
		}
		if ((currentNs - dataStruct.getLastAccessTime()) < dataStruct.getValue()) {
			throw new BizdevIncokeLimitationException(ErrorCode.UnitLimitExceed, String.format("方法[%s]在单位时间内已经被调用超过[%s]次，请稍后再调用", method.getName(),
					dataStruct.getLimit()));
		} else {
			dataStruct.setLastAccessTime(currentNs);
		}
	}
}

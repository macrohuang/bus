package com.sogou.bizdev.limit.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.sogou.bizdev.limit.annotation.SyncCall;
import com.sogou.bizdev.limit.constants.ErrorCode;
import com.sogou.bizdev.limit.exception.BizdevIncokeLimitationException;
import com.sogou.bizdev.limit.struct.SyncDataStruct;

public class SyncCallProcessor {
	private static final Map<String, SyncDataStruct> SYNC_CALL_MAP = new ConcurrentHashMap<String, SyncDataStruct>();
	private static final Logger LOGGER = Logger.getLogger(SyncCallProcessor.class);

	private static final SyncCallProcessor INSTANCE = new SyncCallProcessor();

	private SyncCallProcessor() {

	}

	public static SyncCallProcessor getInstance() {
		return INSTANCE;
	}

	public static boolean isSyncCallMethod(Method method) {
		return ProcessorHelper.hasSyncCallAnnotation(method) || SYNC_CALL_MAP.containsKey(ProcessorHelper.getKey(method));
	}

	public boolean regiest(Method method, SyncCall sync) {
		return SYNC_CALL_MAP.put(ProcessorHelper.getKey(method), new SyncDataStruct(sync.timeout() == -1 ? sync.timeout() : sync.timeout()
				* (long) Math.pow(10, 6))) != null;
	}

	public boolean regiest(Method method, Long timeout) {
		return SYNC_CALL_MAP.put(ProcessorHelper.getKey(method), new SyncDataStruct(timeout == -1 ? timeout : timeout * (long) Math.pow(10, 6))) != null;
	}

	public boolean unregiest(Method method) {
		return SYNC_CALL_MAP.remove(ProcessorHelper.getKey(method)) != null;
	}

	public void beforeInvoke(Method method, Object key) throws BizdevIncokeLimitationException {
		if (!SYNC_CALL_MAP.containsKey(ProcessorHelper.getKey(method))) {
			LOGGER.error("Method regiest required limit:" + ProcessorHelper.getKey(method));
			return;
		}
		process(method, SYNC_CALL_MAP.get(ProcessorHelper.getKey(method)), key);
	}

	public void afterReturn(Method method, Object key) throws BizdevIncokeLimitationException {
		if (!SYNC_CALL_MAP.containsKey(ProcessorHelper.getKey(method))) {
			LOGGER.error("Method regiest required limit:" + ProcessorHelper.getKey(method));
			return;
		}
		SYNC_CALL_MAP.get(ProcessorHelper.getKey(method)).removeAccessTime(key);
	}

	private void process(Method method, SyncDataStruct syncDataStruct, Object key) {
		if (syncDataStruct.getLastAccessTime(key) == null) {// not invoke yet
			syncDataStruct.setLastAccessTime(key, System.nanoTime());
			return;
		} else {
			if (syncDataStruct.getTimeout() != -1L && System.nanoTime() - syncDataStruct.getLastAccessTime(key) > syncDataStruct.getTimeout()) {// timeout
				syncDataStruct.setLastAccessTime(key, System.nanoTime());
				return;
			} else {
				throw new BizdevIncokeLimitationException(ErrorCode.InvokeLimitExceed, String.format("方法[%s]是一个同步方法，[%s]的请求还未返回，请等方法返回后或者过[%s]ns再访问",
						method.getName(), key, (System.nanoTime() - syncDataStruct.getLastAccessTime(key))));
			}
		}
	}
}

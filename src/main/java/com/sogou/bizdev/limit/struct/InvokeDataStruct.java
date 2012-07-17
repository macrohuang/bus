package com.sogou.bizdev.limit.struct;

import java.util.HashMap;
import java.util.Map;

public class InvokeDataStruct {

	private long value;
	private Map<Object, Long> accessMap;
	private boolean async;
	private long timeout;

	public InvokeDataStruct() {
		value = 0l;
		accessMap = new HashMap<Object, Long>();
		async = true;
		timeout = -1;
	}

	public InvokeDataStruct(long value, boolean async, long timeout) {
		this.value = value;
		this.async = async;
		this.timeout = timeout;
		accessMap = new HashMap<Object, Long>();
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public Long getLastAccessTime(Object key) {
		return accessMap.get(key);
	}

	public void setLastAccessTime(Object key, long accessTime) {
		accessMap.put(key, accessTime);
	}

	public void removeAccessTime(Object key) {
		accessMap.remove(key);
	}
}

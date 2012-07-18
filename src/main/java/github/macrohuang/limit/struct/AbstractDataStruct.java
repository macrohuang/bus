package github.macrohuang.limit.struct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDataStruct {
	private Map<Object, Long> accessMap;

	public AbstractDataStruct() {
		this.accessMap = new ConcurrentHashMap<Object, Long>();
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

package github.macrohuang.limit.struct;

public class UnitDataStruct {
	private long value;
	private long lastAccessTime;
	private long limit;

	public UnitDataStruct() {

	}

	public UnitDataStruct(long value, long lastAccessTime, long limit) {
		this.value = value;
		this.lastAccessTime = lastAccessTime;
		this.limit = limit;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}
}

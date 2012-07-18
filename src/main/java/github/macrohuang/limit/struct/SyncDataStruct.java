package github.macrohuang.limit.struct;


public class SyncDataStruct extends AbstractDataStruct {
	private long timeout;

	public SyncDataStruct() {
		super();
		timeout = -1;
	}

	public SyncDataStruct(long timeout) {
		this();
		this.timeout = timeout;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}

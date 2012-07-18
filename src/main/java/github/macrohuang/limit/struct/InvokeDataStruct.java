package github.macrohuang.limit.struct;


public class InvokeDataStruct extends AbstractDataStruct {

	private long value;

	public InvokeDataStruct() {
		super();
		value = 0l;
	}

	public InvokeDataStruct(long value) {
		this();
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
}

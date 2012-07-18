package github.macrohuang.limit.constants;

public enum ErrorCode {
	GlobalLimitExceed(0), InvokeLimitExceed(100), UnitLimitExceed(200);

	private int code;
	private ErrorCode(int code){
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}

package github.macrohuang.limit.exception;

import github.macrohuang.limit.constants.ErrorCode;

public class BizdevIncokeLimitationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7129801987931112026L;

	private ErrorCode errorCode;

	public BizdevIncokeLimitationException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public BizdevIncokeLimitationException(ErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public BizdevIncokeLimitationException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public BizdevIncokeLimitationException(ErrorCode errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}

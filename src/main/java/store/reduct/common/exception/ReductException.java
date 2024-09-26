package store.reduct.common.exception;

import lombok.Getter;

public class ReductException extends RuntimeException {

	@Getter
	private int statusCode;

	public ReductException(String message) {
		super(message);
	}

	public ReductException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReductException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}
}

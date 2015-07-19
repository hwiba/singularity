package stag.exception;

public class SessionUserMismatchException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SessionUserMismatchException() {
		super();
	}

	public SessionUserMismatchException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SessionUserMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public SessionUserMismatchException(String message) {
		super(message);
	}

	public SessionUserMismatchException(Throwable cause) {
		super(cause);
	}
	
}
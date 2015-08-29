package singularity.user.exception;

public class ExistedUserException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ExistedUserException() {
		super();
	}
	
	public ExistedUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExistedUserException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ExistedUserException(String message) {
		super(message);
	}

	public ExistedUserException(Throwable cause) {
		super(cause);
	}

}

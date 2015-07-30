package singularity.exception;

import org.springframework.http.HttpStatus;

public class ExistedUserException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private HttpStatus httpStatus;
	
	public ExistedUserException() {
		super();
	}
	
	public ExistedUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExistedUserException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ExistedUserException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public ExistedUserException(String message) {
		super(message);
	}

	public ExistedUserException(Throwable cause) {
		super(cause);
	}
}

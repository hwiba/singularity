package singularity.party.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)
public class UnpermittedAccessException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public UnpermittedAccessException() {
		super();
	}
	public UnpermittedAccessException(Throwable cause) {
		super(cause);
	}
	public UnpermittedAccessException(String message) {
		super(message);
	}
}

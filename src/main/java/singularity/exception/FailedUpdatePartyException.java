package singularity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class FailedUpdatePartyException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FailedUpdatePartyException() {
		super();
	}

	public FailedUpdatePartyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedUpdatePartyException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedUpdatePartyException(String message) {
		super(message);
	}

	public FailedUpdatePartyException(Throwable cause) {
		super(cause);
	}

}

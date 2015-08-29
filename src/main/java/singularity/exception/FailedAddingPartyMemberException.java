package singularity.exception;

public class FailedAddingPartyMemberException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FailedAddingPartyMemberException() {
		super();
	}

	public FailedAddingPartyMemberException(String message, Throwable cause,
                                            boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedAddingPartyMemberException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedAddingPartyMemberException(String message) {
		super(message);
	}

	public FailedAddingPartyMemberException(Throwable cause) {
		super(cause);
	}
}

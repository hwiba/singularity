package singularity.party.exception;

public class PartyLeaveFailedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PartyLeaveFailedException() {
		super();
	}

	public PartyLeaveFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PartyLeaveFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public PartyLeaveFailedException(String message) {
		super(message);
	}

	public PartyLeaveFailedException(Throwable cause) {
		super(cause);
	}

}

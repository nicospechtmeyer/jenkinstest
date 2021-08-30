package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class InvalidQueryException extends Exception {

	public InvalidQueryException() {
		super();
	}

	public InvalidQueryException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
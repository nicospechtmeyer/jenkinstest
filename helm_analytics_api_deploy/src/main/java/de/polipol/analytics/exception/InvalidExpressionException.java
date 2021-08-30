package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class InvalidExpressionException extends Exception {

	public InvalidExpressionException() {
		super();
	}

	public InvalidExpressionException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

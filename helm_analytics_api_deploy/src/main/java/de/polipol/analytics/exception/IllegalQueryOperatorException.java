package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class IllegalQueryOperatorException extends Exception {

	public IllegalQueryOperatorException() {
		super();
	}

	public IllegalQueryOperatorException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

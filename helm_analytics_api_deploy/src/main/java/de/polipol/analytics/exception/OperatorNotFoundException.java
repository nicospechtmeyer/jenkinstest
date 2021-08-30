package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class OperatorNotFoundException extends Exception {

	public OperatorNotFoundException() {
		super();
	}

	public OperatorNotFoundException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
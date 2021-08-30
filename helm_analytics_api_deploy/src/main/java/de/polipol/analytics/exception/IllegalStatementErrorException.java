package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class IllegalStatementErrorException extends Exception {

	public IllegalStatementErrorException() {
		super();
	}

	public IllegalStatementErrorException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

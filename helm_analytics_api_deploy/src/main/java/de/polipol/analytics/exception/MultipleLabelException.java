package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class MultipleLabelException extends Exception {

	public MultipleLabelException() {
		super();
	}

	public MultipleLabelException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

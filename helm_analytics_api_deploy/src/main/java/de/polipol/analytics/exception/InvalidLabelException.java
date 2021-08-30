package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class InvalidLabelException extends Exception {

	public InvalidLabelException() {
		super();
	}

	public InvalidLabelException(final String e) {
		super(e);
	}
}

package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class NoLabelExistsException extends Exception {

	public NoLabelExistsException() {
		super();
	}

	public NoLabelExistsException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

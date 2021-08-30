package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class ElementNotFoundException extends Exception {

	public ElementNotFoundException() {
		super();
	}

	public ElementNotFoundException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
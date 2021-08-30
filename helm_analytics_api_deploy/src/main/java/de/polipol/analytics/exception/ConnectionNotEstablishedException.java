package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class ConnectionNotEstablishedException extends Exception {

	public ConnectionNotEstablishedException() {
		super();
	}

	public ConnectionNotEstablishedException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
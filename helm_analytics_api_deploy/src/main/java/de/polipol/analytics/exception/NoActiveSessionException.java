package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class NoActiveSessionException extends AnalyticsException {

	public NoActiveSessionException() {
		super();
	}

	public NoActiveSessionException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
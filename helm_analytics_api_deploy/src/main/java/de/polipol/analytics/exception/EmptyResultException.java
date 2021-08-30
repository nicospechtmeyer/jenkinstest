package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class EmptyResultException extends AnalyticsException {

	public EmptyResultException() {
		super();
	}

	public EmptyResultException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

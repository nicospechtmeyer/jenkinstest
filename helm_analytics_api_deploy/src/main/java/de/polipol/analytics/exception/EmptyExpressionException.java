package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class EmptyExpressionException extends AnalyticsException {

	public EmptyExpressionException() {
		super();
	}

	public EmptyExpressionException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

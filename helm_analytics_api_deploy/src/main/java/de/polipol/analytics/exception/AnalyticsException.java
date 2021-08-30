package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public class AnalyticsException extends RuntimeException {

	public AnalyticsException() {
		super();
	}

	public AnalyticsException(final Exception e) {
		super(e);
	}

	public AnalyticsException(final String e) {
		super(e);
	}
}
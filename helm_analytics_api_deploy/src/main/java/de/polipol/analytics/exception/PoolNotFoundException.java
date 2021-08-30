package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class PoolNotFoundException extends Exception {

	public PoolNotFoundException() {
		super();
	}

	public PoolNotFoundException(final String e) {
		super(e);
	}
}

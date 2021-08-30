package de.polipol.analytics.exception;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@SuppressWarnings("serial")
public final class PersistenceException extends Exception {

	String query;

	public PersistenceException() {
		super();
		this.query = EMPTY;
	}

	public PersistenceException(final String e, final String query) {
		super(e);
		this.query = query;
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	public String getQuery() {
		return query;
	}
}
package de.polipol.analytics.connect.data.sap;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

@BapiStructure
public final class Option {

	@Parameter(value = "TEXT")
	private String queryString;

	public Option() {
		super();
		this.queryString = EMPTY;
	}

	public Option(final String queryString) {
		this.queryString = queryString.toUpperCase();
	}

	public String getQueryString() {
		return this.queryString;
	}
}
package de.polipol.analytics.connect.data.sap;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

@BapiStructure
public final class Field {

	@Parameter(value = "FIELDNAME")
	private String name;

	public Field() {
		super();
		this.name = EMPTY;
	}

	public Field(final String name) {
		this.name = name.toUpperCase();
	}

	public String getName() {
		return name;
	}
}
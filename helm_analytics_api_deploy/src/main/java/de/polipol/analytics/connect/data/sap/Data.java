package de.polipol.analytics.connect.data.sap;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

@BapiStructure
public final class Data {

	private static final String SEPERATOR = ";";

	@Parameter("WA")
	private String result;

	public Data() {
		super();
		this.result = EMPTY;
	}

	public String[] getResult() {
		return result.trim().split(SEPERATOR);
	}
}
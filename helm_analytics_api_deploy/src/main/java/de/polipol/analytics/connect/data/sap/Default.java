package de.polipol.analytics.connect.data.sap;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

@BapiStructure
public final class Default {

	@Parameter("")
	private String result;

	public Default() {
		super();
		this.result = EMPTY;
	}

	public String[] getResult() {
		String[] stringArray = new String[1];
		stringArray[0] = result;
		return stringArray;
	}
}
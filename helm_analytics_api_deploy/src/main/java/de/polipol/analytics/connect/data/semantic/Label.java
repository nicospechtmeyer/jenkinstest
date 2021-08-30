package de.polipol.analytics.connect.data.semantic;

import java.util.Map;

public interface Label {

	static final String ANNOTATION_DELIMITER = "~";

	static final String CONTEXT_DELIMITER = ">";

	static final String ELEMENTSTRING_DELIMITER = ";";

	static final String NATIVESTRING_IDENTIFIER = ":";

	String getAnnotationString();

	Map<String, String> getContext();

	String getContextString();

	String getHeaderString();

	String getMeaning();

	boolean isNativeLabel();
}

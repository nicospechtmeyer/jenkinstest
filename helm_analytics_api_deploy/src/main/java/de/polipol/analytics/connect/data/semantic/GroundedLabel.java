package de.polipol.analytics.connect.data.semantic;

import de.polipol.analytics.connect.data.DataType;

public interface GroundedLabel extends Label {

	static final String GROUNDED_ASSIGNMENT_STRING = "->";

	boolean containsNativeLabel();

	String getElementName();

	String getFormatPattern();

	DataType getType();
}

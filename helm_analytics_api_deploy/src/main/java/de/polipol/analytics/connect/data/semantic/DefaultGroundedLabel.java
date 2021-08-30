package de.polipol.analytics.connect.data.semantic;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.connect.data.DataType;
import de.polipol.analytics.exception.InvalidLabelException;

public final class DefaultGroundedLabel extends DefaultLabel implements GroundedLabel {

	private String elementName;
	private String formatPattern;
	private DataType type;

	public DefaultGroundedLabel(final Label label, final String elementString) throws InvalidLabelException {
		super(label);
		setElementName(elementString);
		this.formatPattern = EMPTY;
		this.type = DataType.UNKNOWN;
	}

	public DefaultGroundedLabel(final String labelString, final String elementString) throws InvalidLabelException {
		super(labelString);
		setElementName(elementString);
		setFormatPattern(elementString);
		setType(elementString);
	}

	public DefaultGroundedLabel(final String labelString, final String elementString, final DataType type)
			throws InvalidLabelException {
		super(labelString);
		setElementName(elementString);
		setFormatPattern(elementString);
		this.type = type;
	}

	@Override
	public boolean containsNativeLabel() {
		return super.isNativeLabel();
	}

	@Override
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (object == this)
			return true;
		if (getClass() != object.getClass())
			return false;
		GroundedLabel other = (GroundedLabel) object;
		return StringUtils.equals(this.toString(), other.toString());
	}

	@Override
	public String getElementName() {
		return elementName;
	}

	@Override
	public String getFormatPattern() {
		return formatPattern;
	}

	@Override
	public DataType getType() {
		return type;
	}

	private void setElementName(String elementString) throws InvalidLabelException {
		String[] splittedElementString = StringUtils.split(elementString, ELEMENTSTRING_DELIMITER);
		this.elementName = splittedElementString[0];
	}

	private void setFormatPattern(String elementString) throws InvalidLabelException {
		String[] splittedElementString = StringUtils.split(elementString, ELEMENTSTRING_DELIMITER);
		if (splittedElementString.length > 2) {
			this.formatPattern = splittedElementString[2];
		} else {
			this.formatPattern = EMPTY;
		}
	}

	private void setType(String elementString) throws InvalidLabelException {
		String[] splittedElementString = StringUtils.split(elementString, ELEMENTSTRING_DELIMITER);
		if (splittedElementString.length > 1) {
			String type = splittedElementString[1];
			if (StringUtils.equalsIgnoreCase(DataType.NUMBER.toString(), type)) {
				this.type = DataType.NUMBER;
			} else if (StringUtils.equalsIgnoreCase(DataType.STRING.toString(), type)) {
				this.type = DataType.STRING;
			} else if (StringUtils.equalsIgnoreCase(DataType.DATE.toString(), type)) {
				this.type = DataType.DATE;
			} else if (StringUtils.equalsIgnoreCase(DataType.BOOLEAN.toString(), type)) {
				this.type = DataType.BOOLEAN;
			} else {
				this.type = DataType.UNKNOWN;
			}
		} else {
			this.type = DataType.UNKNOWN;
		}
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getElementName());
		builder.append(GROUNDED_ASSIGNMENT_STRING);
		builder.append(getAnnotationString());
		builder.append(ELEMENTSTRING_DELIMITER);
		builder.append(type);
		if (!formatPattern.isEmpty()) {
			builder.append(ELEMENTSTRING_DELIMITER);
			builder.append(formatPattern);
		}
		return builder.toString();
	}
}

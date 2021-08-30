package de.polipol.analytics.connect.data.semantic;

import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.exception.InvalidLabelException;

public final class DefaultColumnLabelKey extends DefaultContainerLabelKey implements ColumnLabelKey {

	private Label columnLabel;

	public DefaultColumnLabelKey(final Label columnLabel) throws InvalidLabelException {
		super("");
		this.columnLabel = columnLabel;
	}

	public DefaultColumnLabelKey(final Label container, final Label columnLabel) {
		super(container);
		this.columnLabel = columnLabel;
	}

	public DefaultColumnLabelKey(final String columnLabelString) throws InvalidLabelException {
		super("");
		this.columnLabel = new DefaultLabel(columnLabelString);
	}

	public DefaultColumnLabelKey(final String containerLabelString, final String columnLabelString)
			throws InvalidLabelException {
		super(containerLabelString);
		this.columnLabel = new DefaultLabel(columnLabelString);
	}

	@Override
	public Label getColumnLabel() {
		return columnLabel;
	}

	@Override
	public boolean hasContainerWildcard() {
		return StringUtils.isEmpty(getContainerLabel().getAnnotationString()) ? true : false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(columnLabel.toString());
		builder.append(containerLabel.toString());
		return builder.toString();
	}
}

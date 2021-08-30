package de.polipol.analytics.connect.data.semantic;

public interface ColumnLabelKey extends ContainerLabelKey {

	Label getColumnLabel();

	boolean hasContainerWildcard();
}

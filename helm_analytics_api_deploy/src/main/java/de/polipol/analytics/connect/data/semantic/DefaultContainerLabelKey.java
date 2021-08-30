package de.polipol.analytics.connect.data.semantic;

import static de.polipol.analytics.connect.data.semantic.Dictionary.CONTAINER_IDENTIFIER;

import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.exception.InvalidLabelException;

public class DefaultContainerLabelKey implements ContainerLabelKey {

	protected Label containerLabel;

	public DefaultContainerLabelKey(final Label containerLabel) {
		this.containerLabel = containerLabel;
	}

	public DefaultContainerLabelKey(final String containerLabelString) throws InvalidLabelException {
		this.containerLabel = new DefaultLabel(containerLabelString);
	}

	@Override
	public Label getContainerLabel() {
		return containerLabel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (StringUtils.isNotEmpty(containerLabel.getAnnotationString())) {
			builder.append(CONTAINER_IDENTIFIER);
			builder.append(containerLabel.getAnnotationString());
		}
		return builder.toString();
	}
}

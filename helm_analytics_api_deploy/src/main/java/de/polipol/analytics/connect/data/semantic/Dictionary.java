package de.polipol.analytics.connect.data.semantic;

import java.util.List;

import de.polipol.analytics.exception.InvalidLabelException;
import de.polipol.analytics.exception.NoLabelExistsException;

public interface Dictionary {

	static final String CONTAINER_IDENTIFIER = "@";

	static final String CONTAINER_PLACEHOLDER = "NATIVE";

	static final String MAPPING_FileExtension = "dict";

	List<GroundedLabel> resolveColumnLabels(GroundedLabel groundedContainerLabel, Label columnLabel)
			throws InvalidLabelException, NoLabelExistsException;

	List<GroundedLabel> resolveColumnLabels(GroundedLabel groundedContainerLabel, List<Label> columnLabel)
			throws InvalidLabelException, NoLabelExistsException;

	List<GroundedLabel> resolveContainerLabels(Label containerLabel)
			throws InvalidLabelException, NoLabelExistsException;
}

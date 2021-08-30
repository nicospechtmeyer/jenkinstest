package de.polipol.analytics.connect.data.query;

import java.util.List;

import de.polipol.analytics.connect.data.semantic.GroundedLabel;
import de.polipol.analytics.exception.IllegalQueryOperatorException;
import de.polipol.analytics.exception.IllegalStatementErrorException;

public interface QueryParser {

	String getStatement(GroundedLabel containerLabel, List<GroundedLabel> columns, List<String> selections)
			throws IllegalStatementErrorException, IllegalQueryOperatorException;
}

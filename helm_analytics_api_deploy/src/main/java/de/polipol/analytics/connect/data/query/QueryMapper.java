package de.polipol.analytics.connect.data.query;

import de.polipol.analytics.connect.data.DataType;
import de.polipol.analytics.exception.IllegalQueryOperatorException;

public interface QueryMapper {

	String getComparisonString(String elementName, ComparisonOperator comparisonOperator, String value, DataType type)
			throws IllegalQueryOperatorException;
}

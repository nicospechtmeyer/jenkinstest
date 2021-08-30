 package de.polipol.analytics.connect.data.sap;

import static de.polipol.analytics.connect.data.query.ComparisonOperator.EQUAL;
import static de.polipol.analytics.connect.data.query.ComparisonOperator.LIKE;
import static de.polipol.analytics.connect.data.query.ComparisonOperator.NOTEQUAL;

import de.polipol.analytics.connect.data.AbstractQueryMapper;
import de.polipol.analytics.connect.data.DataType;
import de.polipol.analytics.connect.data.query.ComparisonOperator;
import de.polipol.analytics.exception.IllegalQueryOperatorException;

public final class SapQueryMapper extends AbstractQueryMapper {

	private static final String OPERATOR_AND = "AND";
	private static final String OPERATOR_EQUAL = "EQ";
	private static final String OPERATOR_GREATER = "GT";
	private static final String OPERATOR_GREATER_EQUAL = "GE";
	private static final String OPERATOR_LESS = "LT";
	private static final String OPERATOR_LESS_EQUAL = "LE";
	private static final String OPERATOR_LIKE = "LIKE";
	private static final String OPERATOR_NOT_EQUAL = "NE";
	private static final String OPERATOR_OR = "OR";

	public SapQueryMapper() {
		super(OPERATOR_LESS, OPERATOR_LESS_EQUAL, OPERATOR_EQUAL, OPERATOR_NOT_EQUAL, OPERATOR_GREATER_EQUAL,
				OPERATOR_GREATER, OPERATOR_LIKE, OPERATOR_AND, OPERATOR_OR);
	}

	@Override
	public String getComparisonString(final String elementName, final ComparisonOperator comparisonOperator,
			final String value, final DataType type) throws IllegalQueryOperatorException {

		switch (type) {
		case STRING:
			if (comparisonOperator == EQUAL || comparisonOperator == NOTEQUAL || comparisonOperator == LIKE) {
				return formatComparisonString(elementName, comparisonOperator, value, ESCAPE);
			}
		case NUMBER:
			if (comparisonOperator != LIKE) {
				return formatComparisonString(elementName, comparisonOperator, value, PERCENT);
			}
		case DATE:
			if (comparisonOperator != NOTEQUAL && comparisonOperator != LIKE) {
				return formatComparisonString(elementName, comparisonOperator, value, ESCAPE);
			}
		case UNKNOWN:
			if (comparisonOperator == EQUAL || comparisonOperator == NOTEQUAL || comparisonOperator == LIKE) {
				return formatComparisonString(elementName, comparisonOperator, value, ESCAPE);
			}
		default:
			break;
		}
		throw new IllegalQueryOperatorException();
	}
}

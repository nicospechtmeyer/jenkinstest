package de.polipol.analytics.connect.data.jdbc;

import static de.polipol.analytics.connect.data.query.ComparisonOperator.EQUAL;
import static de.polipol.analytics.connect.data.query.ComparisonOperator.LIKE;
import static de.polipol.analytics.connect.data.query.ComparisonOperator.NOTEQUAL;

import de.polipol.analytics.connect.data.AbstractQueryMapper;
import de.polipol.analytics.connect.data.DataType;
import de.polipol.analytics.connect.data.query.ComparisonOperator;
import de.polipol.analytics.exception.IllegalQueryOperatorException;

public final class RelationalJdbcQueryMapper extends AbstractQueryMapper {

	private static final String BRACKET_LEFT = "(";
	private static final String BRACKET_RIGHT = ")";
	private static final String DATE = "DATE";

	private static final String OPERATOR_AND = "AND";
	private static final String OPERATOR_EQUAL = "=";
	private static final String OPERATOR_GREATER = ">";
	private static final String OPERATOR_GREATER_EQUAL = ">=";
	private static final String OPERATOR_LESS = "<";
	private static final String OPERATOR_LESS_EQUAL = "<=";
	private static final String OPERATOR_LIKE = "LIKE";

	private static final String OPERATOR_NOT_EQUAL = "!=";
	private static final String OPERATOR_OR = "OR";

	public RelationalJdbcQueryMapper() {
		super(OPERATOR_LESS, OPERATOR_LESS_EQUAL, OPERATOR_EQUAL, OPERATOR_NOT_EQUAL, OPERATOR_GREATER_EQUAL,
				OPERATOR_GREATER, OPERATOR_LIKE, OPERATOR_AND, OPERATOR_OR);
	}

	private String formatDateString(final String elementName) {
		final StringBuilder builder = new StringBuilder();
		builder.append(DATE);
		builder.append(BRACKET_LEFT);
		builder.append(elementName);
		builder.append(BRACKET_RIGHT);
		builder.append(SPACE);
		return builder.toString();
	}

	@Override
	public String getComparisonString(final String elementName, final ComparisonOperator comparisonOperator,
			final String value, final DataType type) throws IllegalQueryOperatorException {
		switch (type) {
		case STRING:
			if (comparisonOperator == EQUAL || comparisonOperator == NOTEQUAL) {
				return formatComparisonString(elementName, comparisonOperator, value, ESCAPE);
			} else if (comparisonOperator == LIKE) {
				return formatComparisonString(elementName, comparisonOperator, value, ESCAPE, PERCENT);
			}
		case NUMBER:
			if (comparisonOperator != LIKE) {
				return formatComparisonString(elementName, comparisonOperator, value);
			}
		case DATE:
			if (comparisonOperator != LIKE) {
				return formatComparisonString(formatDateString(elementName), comparisonOperator, value, ESCAPE);
			}
		case UNKNOWN:
			if (comparisonOperator == EQUAL || comparisonOperator == NOTEQUAL) {
				return formatComparisonString(elementName, comparisonOperator, value, ESCAPE);
			} else if (comparisonOperator == LIKE) {
				return formatComparisonString(elementName, comparisonOperator, value, ESCAPE, PERCENT);
			}
		default:
			break;
		}
		throw new IllegalQueryOperatorException();
	}
}

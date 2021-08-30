package de.polipol.analytics.connect.data;

import de.polipol.analytics.connect.data.query.ComparisonOperator;
import de.polipol.analytics.connect.data.query.ConjunctionOperator;
import de.polipol.analytics.connect.data.query.QueryMapper;
import de.polipol.analytics.exception.IllegalQueryOperatorException;

public abstract class AbstractQueryMapper implements QueryMapper {

	protected static final boolean ESCAPE = true;
	protected static final boolean PERCENT = true;
	protected static final String SPACE = " ";

	private String operatorAnd;
	private String operatorEqual;
	private String operatorGreater;
	private String operatorGreaterEqual;
	private String operatorLess;
	private String operatorLessEqual;
	private String operatorLike;

	private String operatorNotEqual;
	private String operatorOr;

	protected AbstractQueryMapper(final String operatorLess, final String operatorLessEqual, final String operatorEqual,
			final String operatorNotEqual, final String operatorGreaterEqual, final String operatorGreater,
			final String operatorLike, final String operatorAnd, final String operatorOr) {
		this.operatorLess = operatorLess;
		this.operatorLessEqual = operatorLessEqual;
		this.operatorEqual = operatorEqual;
		this.operatorNotEqual = operatorNotEqual;
		this.operatorGreaterEqual = operatorGreaterEqual;
		this.operatorGreater = operatorGreater;
		this.operatorLike = operatorLike;
		this.operatorAnd = operatorAnd;
		this.operatorOr = operatorOr;
	}

	protected String formatComparisonString(final String elementName, final ComparisonOperator comparisonOperator,
			final String value) throws IllegalQueryOperatorException {
		return formatComparisonString(elementName, comparisonOperator, value, !ESCAPE, !PERCENT);
	}

	protected String formatComparisonString(final String elementName, final ComparisonOperator comparisonOperator,
			final String value, final boolean escape) throws IllegalQueryOperatorException {
		return formatComparisonString(elementName, comparisonOperator, value, ESCAPE, !PERCENT);
	}

	protected String formatComparisonString(final String elementName, final ComparisonOperator comparisonOperator,
			final String value, final boolean escape, final boolean percent) throws IllegalQueryOperatorException {
		final StringBuilder builder = new StringBuilder();
		builder.append(elementName.toUpperCase());
		builder.append(SPACE);
		builder.append(getNativeComparisonOperator(comparisonOperator));
		builder.append(SPACE);
		if (escape) {
			builder.append("'");
		}
		if (percent) {
			builder.append("%");
		}
		builder.append(value.replaceAll("'", "\'")); // TODO maybe more escaping
		// necessary, check!
		if (percent) {
			builder.append("%");
		}
		if (escape) {
			builder.append("'");
		}
		return builder.toString();
	}

	protected String getNativeComparisonOperator(final ComparisonOperator comparisonOperator)
			throws IllegalQueryOperatorException {
		switch (comparisonOperator) {
		case LESS:
			return operatorLess;
		case LESSEQUAL:
			return operatorLessEqual;
		case EQUAL:
			return operatorEqual;
		case NOTEQUAL:
			return operatorNotEqual;
		case GREATEREQUAL:
			return operatorGreaterEqual;
		case GREATER:
			return operatorGreater;
		case LIKE:
			return operatorLike;
		default:
			throw new IllegalQueryOperatorException();
		}
	}

	protected String getNativeConjunctionOperator(final ConjunctionOperator conjunctionOperator)
			throws IllegalQueryOperatorException {
		switch (conjunctionOperator) {
		case AND:
			return operatorAnd;
		case OR:
			return operatorOr;
		default:
			throw new IllegalQueryOperatorException();
		}
	}
}

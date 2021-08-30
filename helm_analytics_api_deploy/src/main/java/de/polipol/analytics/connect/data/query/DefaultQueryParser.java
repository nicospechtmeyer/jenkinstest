package de.polipol.analytics.connect.data.query;

import static de.polipol.analytics.connect.data.semantic.Label.NATIVESTRING_IDENTIFIER;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.connect.data.DataType;
import de.polipol.analytics.connect.data.semantic.GroundedLabel;
import de.polipol.analytics.exception.IllegalQueryOperatorException;
import de.polipol.analytics.exception.IllegalStatementErrorException;

public final class DefaultQueryParser implements QueryParser {

	private Map<String, ComparisonOperator> comparisonOperatorMap;
	private Map<String, ConjunctionOperator> conjunctionOperatorMap;
	// private char DELIMITER;
	// private RService rService;
	// private Dictionary dictionary;
	private QueryMapper queryMapper;

	public DefaultQueryParser(final QueryMapper queryMapper) {
		super();
		// this.rService = rService;
		// this.dictionary = dictionary;
		this.queryMapper = queryMapper;
		// this.DELIMITER = DELIMITER;
		// order important
		this.comparisonOperatorMap = new LinkedHashMap<String, ComparisonOperator>();
		this.comparisonOperatorMap.put("!=", ComparisonOperator.NOTEQUAL);
		this.comparisonOperatorMap.put("<=", ComparisonOperator.LESSEQUAL);
		this.comparisonOperatorMap.put(">=", ComparisonOperator.GREATEREQUAL);
		this.comparisonOperatorMap.put("<", ComparisonOperator.LESS);
		this.comparisonOperatorMap.put(">", ComparisonOperator.GREATER);
		this.comparisonOperatorMap.put("=", ComparisonOperator.EQUAL);
		this.comparisonOperatorMap.put("LIKE", ComparisonOperator.LIKE);
		this.conjunctionOperatorMap = new HashMap<String, ConjunctionOperator>();
		this.conjunctionOperatorMap.put("AND", ConjunctionOperator.AND);
		this.conjunctionOperatorMap.put("OR", ConjunctionOperator.OR);
	}

	private String getComparisonColumnName(final GroundedLabel groundedContainerLabel,
			final List<GroundedLabel> groundedColumnLabels, final String selection)
			throws IllegalStatementErrorException {
		if (isComparisonString(selection)) {
			for (String operator : comparisonOperatorMap.keySet()) {
				if (StringUtils.contains(selection, operator)) {
					String column = StringUtils.splitByWholeSeparatorPreserveAllTokens(selection, operator)[0];
					if (column.startsWith(NATIVESTRING_IDENTIFIER)) {
						return column.substring(NATIVESTRING_IDENTIFIER.length(), column.length());
					} else {
						for (GroundedLabel groundedColumnLabel : groundedColumnLabels) {
							if (StringUtils.equalsIgnoreCase(groundedColumnLabel.getAnnotationString(), column)) {
								return groundedColumnLabel.getElementName();
							}
						}
					}
				}
			}
		}
		throw new IllegalStatementErrorException();
	}

	private ComparisonOperator getComparisonOperator(final String selection)
			throws IllegalStatementErrorException, IllegalQueryOperatorException {
		if (isComparisonString(selection)) {
			for (String operator : comparisonOperatorMap.keySet()) {
				if (StringUtils.contains(selection, operator)) {
					return comparisonOperatorMap.get(operator);
				}
			}
		}
		throw new IllegalStatementErrorException();
	}

	private DataType getComparisonType(final List<GroundedLabel> groundedColumnLabels, final String columnName) {
		for (GroundedLabel groundedColumnLabel : groundedColumnLabels) {
			if (StringUtils.equalsIgnoreCase(groundedColumnLabel.getElementName(), columnName)) {
				return groundedColumnLabel.getType();
			}
		}
		return DataType.STRING;
	}

	private String getComparisonValue(final GroundedLabel groundedContainerLabel,
			final List<GroundedLabel> groundedColumnLabels, final String selection)
			throws IllegalStatementErrorException {
		if (isComparisonString(selection)) {
			for (String operator : comparisonOperatorMap.keySet()) {
				if (StringUtils.contains(selection, operator)) {
					// String columnLabel =
					// StringUtils.splitByWholeSeparatorPreserveAllTokens(selection,
					// operator)[0];
					String value = StringUtils.splitByWholeSeparatorPreserveAllTokens(selection, operator)[1];
					// String formatPattern = EMPTY;
					// for (GroundedLabel groundedColumnLabel :
					// groundedColumnLabels) {
					// if
					// (StringUtils.equalsIgnoreCase(groundedColumnLabel.getAnnotationString(),
					// columnLabel)) {
					// formatPattern = groundedColumnLabel.getFormatPattern();
					// }
					// }
					// if (StringUtils.isNotEmpty(formatPattern)) {
					// try {
					// String transformattedSelection =
					// rService.getTransformationResult(formatPattern, value);
					//// logger.info(selection + " -> " +
					// transformattedSelection);
					//// return transformattedSelection;
					// } catch (RException exception) {
					// throw new IllegalStatementErrorException();
					// }
					// } else {
					return value;
					// }
				}
			}
		}
		throw new IllegalStatementErrorException();
	}

	private ConjunctionOperator getConjunctionOperator(final String selection)
			throws IllegalStatementErrorException, IllegalQueryOperatorException {
		if (isConjunctionString(selection)) {
			return this.conjunctionOperatorMap.get(selection.trim().toUpperCase());
		} else {
			throw new IllegalStatementErrorException();
		}
	}

	@Override
	public String getStatement(final GroundedLabel groundedContainerLabel,
			final List<GroundedLabel> groundedColumnLabels, final List<String> selections)
			throws IllegalStatementErrorException, IllegalQueryOperatorException {
		Stack<String> stack = new Stack<String>();
		for (String selection : selections) {
			if (isComparisonString(selection)) {
				String comparisionColumnName = getComparisonColumnName(groundedContainerLabel, groundedColumnLabels,
						selection);
				String comparisionString = queryMapper.getComparisonString(comparisionColumnName,
						getComparisonOperator(selection),
						getComparisonValue(groundedContainerLabel, groundedColumnLabels, selection),
						getComparisonType(groundedColumnLabels, comparisionColumnName));
				stack.push(comparisionString);
			} else if (isConjunctionString(selection)) {
				String columnName1 = stack.pop();
				String columnName2 = stack.pop();
				stack.push(columnName2 + " " + getConjunctionOperator(selection) + " " + columnName1);
			} else {
				new IllegalStatementErrorException();
			}
		}
		return stack.pop();
	}

	private boolean isComparisonString(final String selection) throws IllegalStatementErrorException {
		for (String operator : comparisonOperatorMap.keySet()) {
			if (StringUtils.contains(selection, operator)) {
				return true;
			}
		}
		return false;
	}

	private boolean isConjunctionString(final String selection) throws IllegalStatementErrorException {
		return this.conjunctionOperatorMap.containsKey(selection.trim().toUpperCase());
	}
}

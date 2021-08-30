package de.polipol.analytics.connect.r;

public abstract class RSymbols {

	public enum DataType {
		CHARACTER, COMPLEX, INTEGER, LOGICAL, NUMERIC, RAW
	}

	public static final String ASSIGNMENT_OPERATOR = "<-";
	public static final String COMMA = ",";
	public static final String DOLLAR_SIGN = "$";
	public static final String DOT = ".";
	public static final String DOUBLE_QUOTE = "\"";
	public static final String EQUAL = "=";
	public static final String LEFT_ARROW = "<";
	public static final String LEFT_BRACKET = "(";
	public static final String LEFT_SQUARE_BRACKET = "[";
	public static final String LINEBREAK = "\n";
	public static final String MINUS = "-";
	public static final String PLUS = "+";
	public static final String QUOTE = "'";
	public static final String RIGHT_ARROW = ">";
	public static final String RIGHT_BRACKET = ")";
	public static final String RIGHT_SQUARE_BRACKET = "]";
	public static final String SEMICOLON = ";";
	public static final String SPACE = " ";
	public static final String TABULAR = "\t";
	public static final String UNDERSCORE = "_";
}

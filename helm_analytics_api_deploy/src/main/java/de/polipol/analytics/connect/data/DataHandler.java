package de.polipol.analytics.connect.data;

import java.io.IOException;
import java.util.List;

import de.polipol.analytics.commons.io.OutputGenerator.OutputType;
import de.polipol.analytics.connect.Handler;
import de.polipol.analytics.exception.IllegalQueryOperatorException;
import de.polipol.analytics.exception.IllegalStatementErrorException;
import de.polipol.analytics.exception.InvalidLabelException;
import de.polipol.analytics.exception.InvalidQueryException;
import de.polipol.analytics.exception.NoLabelExistsException;
import de.polipol.analytics.exception.PersistenceException;

public interface DataHandler extends Handler {

	static final String ASSIGNMENT_OPERATOR = "=";

	static final int DEFAULT_NUMBER_OF_ROWS = 100000;

	static final int PREVIEW_RECORDS = 50;

	static final String QUERY_SELECTION_DELIMITER = "&";

	int getNumberOfColumns();

	int getNumberOfRows();

	String getResults(String containerLabelString, List<String> columnLabelStrings, int numberOfRows,
			OutputType outputType)
			throws InvalidQueryException, PersistenceException, IOException, IllegalStatementErrorException,
			IllegalQueryOperatorException, InvalidLabelException, NoLabelExistsException;

	String getResults(String containerLabelString, List<String> columnLabelStrings, List<String> selections,
			int numberOfRows, OutputType outputType)
			throws InvalidQueryException, PersistenceException, IOException, IllegalStatementErrorException,
			IllegalQueryOperatorException, InvalidLabelException, NoLabelExistsException;

	String getResults(String containerLabelString, List<String> columnLabelStrings, String nativeQuery,
			int numberOfRows, OutputType outputType)
			throws InvalidQueryException, PersistenceException, IOException, IllegalStatementErrorException,
			IllegalQueryOperatorException, InvalidLabelException, NoLabelExistsException;

	String getResults(String containerLabelString, List<String> columnLabelStrings, String nativeQuery,
			List<String> selections, int numberOfRows, OutputType outputType)
			throws InvalidQueryException, PersistenceException, IOException, IllegalStatementErrorException,
			IllegalQueryOperatorException, InvalidLabelException, NoLabelExistsException;
}

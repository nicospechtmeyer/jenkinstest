package de.polipol.analytics.connect.data;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.commons.io.OutputGenerator;
import de.polipol.analytics.commons.io.OutputGenerator.OutputType;
import de.polipol.analytics.connect.AbstractHandler;
import de.polipol.analytics.connect.Adapter;
import de.polipol.analytics.connect.Adapter.Status;
import de.polipol.analytics.connect.data.query.DefaultQueryParser;
import de.polipol.analytics.connect.data.query.QueryParser;
import de.polipol.analytics.connect.data.semantic.DefaultGroundedLabel;
import de.polipol.analytics.connect.data.semantic.DefaultLabel;
import de.polipol.analytics.connect.data.semantic.Dictionary;
import de.polipol.analytics.connect.data.semantic.GroundedLabel;
import de.polipol.analytics.connect.data.semantic.Label;
import de.polipol.analytics.exception.IllegalQueryOperatorException;
import de.polipol.analytics.exception.IllegalStatementErrorException;
import de.polipol.analytics.exception.InvalidLabelException;
import de.polipol.analytics.exception.InvalidQueryException;
import de.polipol.analytics.exception.NoLabelExistsException;
import de.polipol.analytics.exception.PersistenceException;

public final class DefaultDataHandler extends AbstractHandler implements DataHandler {

	private final Logger logger = LoggerFactory.getLogger(DefaultDataHandler.class);

	private Adapter adapter;
	private Dictionary dictionary;

	public DefaultDataHandler(final Dictionary dictionary, final Adapter adapter) {
		super();
		this.dictionary = dictionary;
		this.adapter = adapter;
	}

	@Override
	public Adapter getAdapter() {
		return adapter;
	}

	@Override
	public int getNumberOfColumns() {
		return adapter.getNumberOfColumns();
	}

	@Override
	public int getNumberOfRows() {
		return adapter.getNumberOfRows();
	}

	@Override
	public String getResults(final String containerLabelString, final List<String> columnLabelStrings,
			final int numberOfRows, final OutputType outputType)
			throws InvalidQueryException, PersistenceException, IOException, IllegalStatementErrorException,
			IllegalQueryOperatorException, InvalidLabelException, NoLabelExistsException {
		return getResults(containerLabelString, columnLabelStrings, "", new ArrayList<String>(), numberOfRows,
				outputType);
	}

	@Override
	public String getResults(final String containerLabelString, final List<String> columnLabelStrings,
			final List<String> selections, final int numberOfRows, final OutputType outputType)
			throws InvalidQueryException, PersistenceException, IOException, IllegalStatementErrorException,
			IllegalQueryOperatorException, InvalidLabelException, NoLabelExistsException {
		return getResults(containerLabelString, columnLabelStrings, "", selections, numberOfRows, outputType);
	}

	@Override
	public String getResults(final String containerLabelString, final List<String> columnLabelStrings,
			final String nativeQuery, final int numberOfRows, final OutputType outputType)
			throws InvalidQueryException, PersistenceException, IOException, IllegalStatementErrorException,
			IllegalQueryOperatorException, InvalidLabelException, NoLabelExistsException {
		return getResults(containerLabelString, columnLabelStrings, nativeQuery, new ArrayList<String>(), numberOfRows,
				outputType);
	}

	@Override
	public String getResults(final String containerLabelString, final List<String> columnLabelStrings,
			final String nativeQuery, final List<String> selections, final int numberOfRows,
			final OutputType outputType)
			throws InvalidQueryException, PersistenceException, IOException, IllegalStatementErrorException,
			IllegalQueryOperatorException, InvalidLabelException, NoLabelExistsException {
		List<GroundedLabel> groundedContainerLabels;
		List<GroundedLabel> groundedColumnLabels;
		GroundedLabel groundedContainerLabel;
		String groundedSelection;

		if (Utils.isDevMode()) {
			logger.info("CONTAINER: " + containerLabelString);
			logger.info("COLUMNS: " + columnLabelStrings);
			logger.info("QUERY: " + nativeQuery);
			logger.info("SELECTIONS: " + selections);
			logger.info("ROWS: " + numberOfRows);
		}

		try {
			groundedContainerLabels = dictionary.resolveContainerLabels(new DefaultLabel(containerLabelString));
			groundedContainerLabel = groundedContainerLabels.get(0); // TODO
		} catch (NoLabelExistsException exception) {
			if (StringUtils.isEmpty(nativeQuery) || !adapter.isNativeQuerySupported()) {
				throw new InvalidQueryException();
			} else {
				groundedContainerLabel = new DefaultGroundedLabel(Dictionary.CONTAINER_PLACEHOLDER,
						Dictionary.CONTAINER_PLACEHOLDER, DataType.CONTAINER);
			}
		}
		List<Label> columnLabels = new ArrayList<Label>();
		for (String columnLabel : columnLabelStrings) {
			columnLabels.add(new DefaultLabel(columnLabel));
		}
		groundedColumnLabels = dictionary.resolveColumnLabels(groundedContainerLabel, columnLabels);
		if (!nativeQuery.isEmpty()) {
			groundedSelection = nativeQuery;
		} else {
			if (!selections.isEmpty()) {
				QueryParser queryParser = new DefaultQueryParser(adapter.getQueryMapper());
				groundedSelection = queryParser.getStatement(groundedContainerLabel, groundedColumnLabels, selections);
			} else {
				groundedSelection = EMPTY;
			}
		}
		return process(groundedContainerLabel, groundedColumnLabels, groundedSelection, numberOfRows, outputType);
	}

	private String process(final GroundedLabel groundedContainerLabel, final List<GroundedLabel> groundedColumnLabels,
			final String groundedSelection, final int numberOfRows, OutputType outputType)
			throws PersistenceException, InvalidQueryException, IOException {
		String containerName = groundedContainerLabel.getElementName();
		List<String> columnNames = groundedColumnLabels.stream().map(x -> x.getElementName())
				.collect(Collectors.toList());
		List<String> displayedColumnNames = groundedColumnLabels.stream().map(x -> x.getHeaderString())
				.collect(Collectors.toList());
		List<DataType> types = groundedColumnLabels.stream().map(x -> x.getType()).collect(Collectors.toList());
		adapter.setStatus(Status.UNPROCESSED);
		adapter.clearResults();
		adapter.setNumberOfRows(numberOfRows);
		adapter.setQueryStatement(containerName, columnNames, groundedSelection);
		Date startDate = new Date();
		adapter.process();
		adapter.arrangeData();
		Date endDate = new Date();
		setQueryDuration(endDate.getTime() - startDate.getTime());
		setQueryTimestamp(Calendar.getInstance().getTime());
		adapter.setStatus(Status.PROCESSED);
		OutputGenerator outputGenerator = new OutputGenerator();
		return outputGenerator.process(adapter.getResults(), displayedColumnNames, types, outputType);
	}
}
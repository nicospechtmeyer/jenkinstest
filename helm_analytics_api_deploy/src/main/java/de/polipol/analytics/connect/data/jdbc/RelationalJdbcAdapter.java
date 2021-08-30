package de.polipol.analytics.connect.data.jdbc;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.polipol.analytics.connect.AbstractAdapter;
import de.polipol.analytics.exception.InvalidQueryException;
import de.polipol.analytics.exception.PersistenceException;

public final class RelationalJdbcAdapter extends AbstractAdapter {

	private static final String FROM = "FROM";
	private static final String SELECT = "SELECT";
	private static final String SEPERATOR = ",";
	private static final String WHERE = "WHERE";
	private final Logger logger = LoggerFactory.getLogger(RelationalJdbcAdapter.class);
	private Connection connection;
	private String query;
	private ResultSet resultSet;

	public RelationalJdbcAdapter(final Connection connection) {
		super();
		this.connection = connection;
		this.query = EMPTY;
		this.queryMapper = new RelationalJdbcQueryMapper();
	}

	@Override
	public void arrangeData() throws PersistenceException {
		try {
			int counter = 1;
			int columnCount = resultSet.getMetaData().getColumnCount();
			while (resultSet.next()) {
				if (counter <= rows) {
					final String[] row = new String[columnCount];
					for (int index = 0; index < columnCount; index++) {
						if (resultSet.getString(index + 1) != null) {
							row[index] = resultSet.getString(index + 1).trim();
						} else {
							row[index] = EMPTY;
						}
					}
					results.add(row);
					counter++;
				} else {
					logger.warn("Number of obtained records is greater than specified row capacity");
					break;
				}
			}
		} catch (SQLException exception) {
			throw new PersistenceException();
		}
	}

	private String getColumnArraysString(final List<String> columnNames) {
		String[] columnNamesArray = columnNames.stream().toArray(String[]::new);
		final StringBuilder builder = new StringBuilder();
		if (columnNamesArray.length == 0 || StringUtils.equals(columnNamesArray[0], "*")) {
			builder.append(SELECT);
		} else {
			builder.append(columnNamesArray[0]);
			for (int index = 1; index < columnNamesArray.length; index++) {
				builder.append(SEPERATOR);
				builder.append(columnNamesArray[index]);
			}
		}
		return builder.toString();
	}

	@Override
	public List<String> getColumnNames() throws PersistenceException {
		final List<String> columnNames = new ArrayList<String>();
		try {
			final ResultSetMetaData metaData = resultSet.getMetaData();
			for (int index = 0; index < metaData.getColumnCount(); index++) {
				columnNames.add(metaData.getColumnName(index + 1));
			}
			return columnNames;
		} catch (final SQLException exception) {
			throw new PersistenceException();
		}
	}

	public Connection getConnection() {
		return connection;
	}

	private String getQueryString(final String containerName, final List<String> columnNames, final String selection) {
		final StringBuilder builder = new StringBuilder();
		builder.append(SELECT);
		builder.append(SPACE);
		builder.append(getColumnArraysString(columnNames));
		builder.append(SPACE);
		builder.append(FROM);
		builder.append(SPACE);
		builder.append(containerName);
		if (!StringUtils.isEmpty(selection)) {
			builder.append(SPACE);
			builder.append(WHERE);
			builder.append(SPACE);
			builder.append(selection);
		}
		return builder.toString();
	}

	@Override
	public boolean isNativeQuerySupported() {
		return true;
	}

	@Override
	public void process() throws PersistenceException {
		if (StringUtils.isEmpty(query)) {
			throw new PersistenceException();
		}
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			statement.closeOnCompletion();
		} catch (final SQLException exception) {
			throw new PersistenceException(exception.getMessage(), query);
		}
	}

	@Override
	public void setQueryStatement(final String containerName, final List<String> columnNames, final String selection)
			throws InvalidQueryException {
		if (selection.startsWith(SELECT)) {
			this.query = selection;
		} else {
			this.query = getQueryString(containerName, columnNames, selection);
		}
	}
}
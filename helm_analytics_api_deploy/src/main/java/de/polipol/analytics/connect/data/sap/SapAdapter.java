package de.polipol.analytics.connect.data.sap;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;
import org.hibersap.session.Session;
import org.hibersap.session.Transaction;

import de.polipol.analytics.connect.AbstractAdapter;
import de.polipol.analytics.exception.PersistenceException;

@Bapi("RFC_READ_TABLE")
public final class SapAdapter extends AbstractAdapter {

	private static final String AND = "AND";

	private static final String MINUS_SIGN = "-";

	@Table
	@Parameter(value = "DATA")
	private List<Data> data;

	@Import
	@Parameter(value = "DELIMITER")
	private String DELIMITER = ";";

	@Table
	@Parameter(value = "FIELDS")
	private List<Field> fields;

	@Table
	@Parameter(value = "OPTIONS")
	private List<Option> options;

	@Import
	@Parameter(value = "QUERY_TABLE")
	private String queryTable;

	@Import
	@Parameter(value = "ROWCOUNT")
	private int rows;

	private Session session;

	public SapAdapter(final Session session) {
		super();
		this.session = session;
		this.fields = new ArrayList<Field>();
		this.queryMapper = new SapQueryMapper();
	}

	@Override
	public void arrangeData() throws PersistenceException {
		for (Data data : this.data) {
			String[] revisedRow = new String[data.getResult().length];
			int index = 0;
			for (String row : data.getResult()) {
				String trimmedRow = row.trim();
				if (trimmedRow.endsWith(MINUS_SIGN)) {
					revisedRow[index] = MINUS_SIGN + trimmedRow.substring(0, trimmedRow.length() - 1);
				} else {
					revisedRow[index] = trimmedRow;
				}
				index++;
			}
			results.add(revisedRow);
		}
	}

	@Override
	public List<String> getColumnNames() throws PersistenceException {
		List<String> columnNames = new ArrayList<String>();
		for (int index = 0; index < this.fields.size(); index++) {
			columnNames.add(this.fields.get(index).getName());
		}
		return columnNames;
	}

	public Session getConnection() {
		return session;
	}

	private String getSplitTokens() {
		final StringBuilder builder = new StringBuilder();
		builder.append(SPACE);
		builder.append(AND);
		builder.append(SPACE);
		return builder.toString();
	}

	@Override
	public boolean isNativeQuerySupported() {
		return false;
	}

	@Override
	public synchronized void process() throws PersistenceException {
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.execute(this);
			transaction.commit();
		} catch (final Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new PersistenceException();
		} finally {
			if (!session.isClosed()) {
				session.close();
			}
		}
	}

	private void setFields(final List<String> columnNames) {
		fields.clear();
		for (String field : columnNames) {
			fields.add(new Field(field));
		}
	}

	@Override
	public void setNumberOfRows(final int numberOfRows) {
		rows = numberOfRows;
	}

	private void setOption(final Option option) {
		List<Option> tmp = new ArrayList<Option>();
		String[] lines = StringUtils.splitByWholeSeparatorPreserveAllTokens(option.getQueryString(), getSplitTokens());
		for (int index = 0; index < lines.length; index++) {
			if (index == 0) {
				tmp.add(new Option(lines[index]));
			} else {
				tmp.add(new Option(new StringBuilder(getSplitTokens()).append(lines[index]).toString()));
			}
		}
		this.options = tmp;
	}

	private void setOption(final String query) {
		setOption(new Option(query));
	}

	@Override
	public void setQueryStatement(final String containerName, final List<String> columnNames, final String selection) {
		queryTable = containerName.toUpperCase();
		setFields(columnNames);
		if (StringUtils.isNotEmpty(selection)) {
			this.setOption(selection);
		}
	}
}
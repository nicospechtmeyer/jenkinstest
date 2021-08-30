package de.polipol.analytics.connect;

import java.util.List;

import de.polipol.analytics.connect.data.query.QueryMapper;
import de.polipol.analytics.exception.InvalidQueryException;
import de.polipol.analytics.exception.PersistenceException;

public interface Adapter {

	static enum Status {
		FAULTY, PROCESSED, UNPROCESSED
	}

	void arrangeData() throws PersistenceException;

	void clearResults();

	List<String> getColumnNames() throws PersistenceException;

	int getNumberOfColumns();

	int getNumberOfRows();

	QueryMapper getQueryMapper();

	List<String[]> getResults();

	Status getStatus();

	boolean isNativeQuerySupported();

	void process() throws PersistenceException;

	void setNumberOfRows(int numberOfRows);

	void setQueryStatement(String containerName, List<String> columnNames, String selection)
			throws InvalidQueryException;

	void setStatus(Status status);
}

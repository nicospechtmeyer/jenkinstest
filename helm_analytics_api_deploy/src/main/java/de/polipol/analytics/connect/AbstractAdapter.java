package de.polipol.analytics.connect;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import de.polipol.analytics.connect.data.query.QueryMapper;

public abstract class AbstractAdapter implements Adapter {

	protected static final String SPACE = " ";

	protected QueryMapper queryMapper;
	protected List<String[]> results;
	protected int rows;
	protected Status status;

	public AbstractAdapter() {
		results = new ArrayList<String[]>();
		status = Status.UNPROCESSED;
	}

	@Override
	public void clearResults() {
		results.clear();
	}

	@Override
	public int getNumberOfColumns() {
		if (CollectionUtils.isEmpty(results)) {
			return 0;
		} else {
			return results.get(0).length;
		}
	}

	@Override
	public int getNumberOfRows() {
		if (CollectionUtils.isEmpty(results)) {
			return 0;
		} else {
			return results.size();
		}
	}

	@Override
	public QueryMapper getQueryMapper() {
		return this.queryMapper;
	}

	@Override
	public List<String[]> getResults() {
		return results;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public void setNumberOfRows(final int numberOfRows) {
		rows = numberOfRows;
	}

	@Override
	public void setStatus(final Status status) {
		this.status = status;
	}
}

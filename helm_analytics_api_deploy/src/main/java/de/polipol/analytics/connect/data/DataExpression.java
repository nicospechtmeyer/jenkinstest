package de.polipol.analytics.connect.data;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class DataExpression {

	public String poolId;
	public String containerLabelString;
	public List<String> columnLabelStrings;
	public boolean nativeQuery;
	public String queryString;
	public int numberOfRows;

	public DataExpression() {
		super();
		this.poolId = EMPTY;
		this.containerLabelString = EMPTY;
		this.columnLabelStrings = new ArrayList<>();
		this.nativeQuery = false;
		this.queryString = EMPTY;
		this.numberOfRows = DataHandler.DEFAULT_NUMBER_OF_ROWS;
	}

	public String getPoolId() {
		return poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}

	public String getContainerLabelString() {
		return containerLabelString;
	}

	public void setContainerLabelString(String containerLabelString) {
		this.containerLabelString = containerLabelString;
	}

	public List<String> getColumnLabelStrings() {
		return columnLabelStrings;
	}

	public void setcolumnLabelStrings(List<String> columnLabelStrings) {
		this.columnLabelStrings = columnLabelStrings;
	}

	public boolean isNativeQuery() {
		return nativeQuery;
	}

	public void setNativeQuery(boolean nativeQuery) {
		this.nativeQuery = nativeQuery;
	}

	public int getNumberOfRows() {
		return !(numberOfRows > 0) ? DataHandler.DEFAULT_NUMBER_OF_ROWS : numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public boolean isValid() {
		return StringUtils.isNotEmpty(poolId)
				&& ((StringUtils.isNotEmpty(containerLabelString) && this.columnLabelStrings.size() > 0)
						|| this.isNativeQuery()) ? true : false;
	}
}

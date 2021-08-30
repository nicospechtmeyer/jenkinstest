package de.polipol.analytics.connect.data.olap;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.olap4j.Axis;
import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.OlapStatement;
import org.olap4j.Position;
import org.olap4j.metadata.Member;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import de.polipol.analytics.connect.AbstractAdapter;
import de.polipol.analytics.exception.InvalidQueryException;
import de.polipol.analytics.exception.PersistenceException;

public final class MultidimensionalAdapter extends AbstractAdapter {

	// private final Logger logger =
	// LoggerFactory.getLogger(MultidimensionalAdapter.class);

	private String catalogName;
	private CellSet cellSet;
	private Connection connection;
	private String query;

	public MultidimensionalAdapter(final Connection connection) {
		super();
		this.connection = connection;
		this.query = EMPTY;
	}

	@Override
	public void arrangeData() throws PersistenceException {
		final List<CellSetAxis> cellSetAxes = cellSet.getAxes();
		final CellSetAxis columnsAxis = cellSetAxes.get(Axis.COLUMNS.axisOrdinal());
		final CellSetAxis rowsAxis = cellSetAxes.get(Axis.ROWS.axisOrdinal());
		for (int rowIndex = 0; rowIndex < rowsAxis.getPositionCount(); rowIndex++) {
			final String[] row = new String[columnsAxis.getPositionCount()];
			for (int columnIndex = 0; columnIndex < columnsAxis.getPositionCount(); columnIndex++) {
				Cell cell = cellSet.getCell(Arrays.asList(columnIndex, rowIndex));
				try {
					row[columnIndex] = cell.getFormattedValue().trim();
				} catch (Exception exception) {
					row[columnIndex] = cell.getValue().toString();
				}
			}
			results.add(row);
		}
	}

	@Override
	public List<String> getColumnNames() throws PersistenceException {
		final List<String> columnNames = new ArrayList<String>();
		List<CellSetAxis> cellSetAxes = cellSet.getAxes();
		CellSetAxis columnsAxis = cellSetAxes.get(Axis.COLUMNS.axisOrdinal());
		for (Position position : columnsAxis.getPositions()) {
			final List<Member> members = position.getMembers();
			for (Member member : members) {
				columnNames.add(member.getName());
			}
		}
		return columnNames;
	}

	public Connection getConnection() {
		return connection;
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
			// logger.info(query);
			OlapStatement statement = (OlapStatement) connection.createStatement();
			connection.setCatalog(catalogName);
			cellSet = statement.executeOlapQuery(query);
		} catch (final SQLException exception) {
			throw new PersistenceException("", query);
		}
	}

	@Override
	public void setQueryStatement(final String containerName, final List<String> columnNames, final String selection)
			throws InvalidQueryException {
		this.catalogName = containerName;
		this.query = selection;
	}
}
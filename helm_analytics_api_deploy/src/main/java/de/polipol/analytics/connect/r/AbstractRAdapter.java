package de.polipol.analytics.connect.r;

import static de.polipol.analytics.connect.r.RSymbols.DOT;
import static de.polipol.analytics.connect.r.RSymbols.EQUAL;
import static de.polipol.analytics.connect.r.RSymbols.LEFT_BRACKET;
import static de.polipol.analytics.connect.r.RSymbols.QUOTE;
import static de.polipol.analytics.connect.r.RSymbols.RIGHT_BRACKET;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import de.polipol.analytics.connect.AbstractAdapter;
import de.polipol.analytics.exception.AnalyticsException;
import de.polipol.analytics.exception.InvalidQueryException;
import de.polipol.analytics.exception.PersistenceException;

public abstract class AbstractRAdapter extends AbstractAdapter implements RAdapter {

	private static final String COLUMN_SEPARATOR = ";";
	private static final String ROW_SEPARATOR = ",";

	protected List<String> columnNames;
	protected String result;
	protected String selection;

	@Override
	public void arrangeData() throws PersistenceException {
		int counter = 0;
		for (String row : result.trim().split(ROW_SEPARATOR)) {
			if (counter > 0) {
				results.add(row.trim().split(COLUMN_SEPARATOR));
			}
			counter++;
		}
	}

	@Override
	public void clearWorkspace() throws AnalyticsException {
		final StringBuilder builder = new StringBuilder();
		builder.append(RKeywords.RM);
		builder.append(LEFT_BRACKET);
		builder.append(RKeywords.LIST);
		builder.append(EQUAL);
		builder.append(RKeywords.LS);
		builder.append(LEFT_BRACKET);
		builder.append(RKeywords.ALL);
		builder.append(DOT);
		builder.append(RKeywords.NAMES);
		builder.append(EQUAL);
		builder.append(RKeywords.TRUE);
		builder.append(RIGHT_BRACKET);
		builder.append(RIGHT_BRACKET);
		eval(builder.toString());
	}

	@Override
	public List<String> getColumnNames() throws PersistenceException {
		final List<String> columnNames = new ArrayList<String>();
		String metaDataString = result.trim().split(ROW_SEPARATOR)[0];
		for (String column : metaDataString.trim().split(COLUMN_SEPARATOR)) {
			columnNames.add(column);
		}
		return columnNames;
	}

	@Override
	public Path getWorkingDirectory() throws AnalyticsException, IOException {
		StringBuilder builder = new StringBuilder();
		builder.append(RKeywords.GETWD);
		builder.append(LEFT_BRACKET);
		builder.append(RIGHT_BRACKET);
		String[] output = eval(builder.toString());
		if (output.length > 0) {
			Path workingDirectory = Paths.get(output[0]);
			if (Files.exists(workingDirectory)) {
				return workingDirectory;
			}
		}
		throw new IOException();
	}

	@Override
	public boolean isNativeQuerySupported() {
		return true;
	}

	@Override
	public void loadFile(final Path path) throws AnalyticsException, IOException {
//		Path workingDirectory = null;
//		try {
//			workingDirectory = getWorkingDirectory();
//		} catch (IOException | RException exception) {
//			logger.info("Getting workspace failed");
//		}
//		try {
		// setWorkingDirectory(path);
		StringBuilder builder = new StringBuilder();
		builder.append(RKeywords.SOURCE);
		builder.append(LEFT_BRACKET);
		builder.append(QUOTE);
		builder.append(path.getFileName());
		builder.append(QUOTE);
		builder.append(RIGHT_BRACKET);
		eval(builder.toString());
//			if (workingDirectory != null) {
//				setWorkingDirectory(workingDirectory);
//			}
//		} catch (IOException exception) {
//			logger.error("Setting workspace failed: " + e.getMessage());
//			throw new IOException(e);
//		}
	}

	@Override
	public void loadPackage(final String[] rPackage) throws AnalyticsException {
		final StringBuilder builder = new StringBuilder();
		builder.append(RKeywords.LIBRARY);
		builder.append(LEFT_BRACKET);
		builder.append(rPackage[0]);
		builder.append(RIGHT_BRACKET);
		eval(builder.toString());
	}

	@Override
	public void process() throws PersistenceException {
		try {
			this.eval(selection);
			// result = this.evalAsString("result <-
			// toString(capture.output(write.table(result, stdout(), sep='"
			// + COLUMN_SEPARATOR + "')))");
		} catch (AnalyticsException exception) {
			throw new PersistenceException();
		}
	}

	@Override
	public void setQueryStatement(String containerName, List<String> columnNames, String selection)
			throws InvalidQueryException {
		this.columnNames = columnNames;
		this.selection = selection;
	}

	@Override
	public void setWorkingDirectory(final Path path) throws AnalyticsException, IOException {
//		if (!Files.exists(path))
//			throw new IOException();
		String pathString = FilenameUtils.separatorsToUnix(path.toString());
		StringBuilder builder = new StringBuilder();
		builder.append(RKeywords.SETWD);
		builder.append(LEFT_BRACKET);
		builder.append(QUOTE);
		builder.append(pathString);
		builder.append(QUOTE);
		builder.append(RIGHT_BRACKET);
		eval(builder.toString());
	}
}

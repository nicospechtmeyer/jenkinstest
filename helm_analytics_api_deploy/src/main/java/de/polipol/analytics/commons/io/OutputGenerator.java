package de.polipol.analytics.commons.io;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;

import de.polipol.analytics.connect.data.DataType;
import de.polipol.analytics.exception.PersistenceException;

public final class OutputGenerator {

	public enum OutputType {
		JSON, PRETTYJSON, TEX_JSON, TXT;
	}

	private static final String LINEBREAK = "\n";

	private static final String TABLE_DELIMITER = "|";

	private String asJson(final List<String[]> results, final List<String> columnNames,
			final List<DataType> columnTypes, final FormattingStrategy formattingStrategy)
			throws IOException, PersistenceException {
		JsonFactory jsonfactory = new JsonFactory();
		JsonGenerator jsonGenerator = null;
		StringWriter stringWriter = new StringWriter();
		try {
			jsonGenerator = jsonfactory.createGenerator(stringWriter);
			MinimalPrettyPrinter prettyPrinter = new MinimalPrettyPrinter();
			jsonGenerator.setPrettyPrinter(prettyPrinter);
			jsonGenerator.writeStartArray();
			for (String[] row : results) {
				jsonGenerator.writeStartObject();
				for (int index = 0; index < row.length; index++) {
					String entry = row[index];
					entry = entry.trim();
					if (columnTypes.get(index) == DataType.STRING || columnTypes.get(index) == DataType.UNKNOWN) {
						if (StringUtils.isNotEmpty(entry)) {
							jsonGenerator.writeStringField(columnNames.get(index),
									formattingStrategy.formatString(entry));
						} else {
							jsonGenerator.writeStringField(columnNames.get(index), "");
						}
					} else if (columnTypes.get(index) == DataType.NUMBER) {
						double value = NumberUtils.toDouble(entry);
						jsonGenerator.writeNumberField(columnNames.get(index), value);
					} else if (columnTypes.get(index) == DataType.DATE) {
						if (StringUtils.isNotEmpty(entry)) {
							jsonGenerator.writeStringField(columnNames.get(index),
									formattingStrategy.formatString(entry));
						}
					}
				}
				jsonGenerator.writeEndObject();
			}
			jsonGenerator.writeEndArray();
		} finally {
			jsonGenerator.close();
		}
		String result = stringWriter.toString();
		stringWriter.close();
		return result;
	}

	private String asTxt(final List<String[]> results, final List<String> columnNames, final List<DataType> columnTypes,
			final FormattingStrategy formattingStrategy) throws IOException, PersistenceException {
		final StringBuilder builder = new StringBuilder();

		builder.append(TABLE_DELIMITER);
		for (String headRow : columnNames) {
			builder.append(headRow);
			builder.append(TABLE_DELIMITER);
		}
		builder.append(LINEBREAK);
		for (String[] rows : results) {
			builder.append(TABLE_DELIMITER);
			for (String row : rows) {
				builder.append(row);
				builder.append(TABLE_DELIMITER);
			}
			builder.append(LINEBREAK);
		}
		return builder.toString();
	}

	public String process(final List<String[]> results, final List<String> columnNames,
			final List<DataType> columnTypes, final OutputType outputType) throws IOException, PersistenceException {
		switch (outputType) {
		case JSON:
			return asJson(results, columnNames, columnTypes, new DefaultFormattingStrategy());
		case PRETTYJSON:
			return asJson(results, columnNames, columnTypes, new DefaultFormattingStrategy());
		case TEX_JSON:
			return asJson(results, columnNames, columnTypes, new TexFormattingStrategy());
		case TXT:
			return asTxt(results, columnNames, columnTypes, new DefaultFormattingStrategy());
		}
		return EMPTY;
	}
}

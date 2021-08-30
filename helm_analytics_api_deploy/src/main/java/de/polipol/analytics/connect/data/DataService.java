package de.polipol.analytics.connect.data;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.commons.io.OutputGenerator.OutputType;
import de.polipol.analytics.commons.props.ConnectionPropertiesReader;
import de.polipol.analytics.connect.AnalyticsService;
import de.polipol.analytics.connect.ConnectionManager;
import de.polipol.analytics.connect.ConnectionRepository;
import de.polipol.analytics.exception.AnalyticsException;
import de.polipol.analytics.file.FileExtension;

public final class DataService implements AnalyticsService {

	private ConnectionManager<?> connectionManager;
	private Path dictionaryDirectory;

	public DataService(final Path dictionaryDirectory) {
		this.dictionaryDirectory = dictionaryDirectory;
		ConnectionPropertiesReader propertiesReader = new ConnectionPropertiesReader();
		this.connectionManager = propertiesReader.getConnectionManager();
	}

	@Override
	public byte[] getDocument(final FileExtension sourceFormat, final FileExtension targetFormat,
			final String expression, final Map<String, String> parameters) throws AnalyticsException {
		return null;
	}

	@Override
	public byte[] getImage(final FileExtension targetFormat, final String expression,
			final Map<String, String> parameters, final int width, final int height, final int resolution)
			throws AnalyticsException {
		return null;
	}

	@Override
	public String getKernelId() {
		return EMPTY;
	}

	@Override
	public byte[] getText(final FileExtension targetFormat, final String expression, final String variable,
			final Map<String, String> preParameters, final Map<String, String> postParameters)
			throws AnalyticsException {
		DataHandler handler;
		String output = EMPTY;
		try {
			if (StringUtils.isNotEmpty(expression)) {
				DataExpression dataExpression = new Gson().fromJson(expression, DataExpression.class);
				if (dataExpression.isValid()) {
					handler = (DataHandler) ConnectionRepository.checkout(
							connectionManager.getConnectionPool(dataExpression.getPoolId()), dictionaryDirectory);
					if (dataExpression.isNativeQuery()) {
						output = handler.getResults(dataExpression.getContainerLabelString(),
								dataExpression.getColumnLabelStrings(), dataExpression.getQueryString(),
								dataExpression.getNumberOfRows(), OutputType.JSON);
					} else {
						if (StringUtils.isNotEmpty(dataExpression.getQueryString())) {
							List<String> selections = Arrays.asList(
									dataExpression.getQueryString().split(DataHandler.QUERY_SELECTION_DELIMITER));
							output = handler.getResults(dataExpression.getContainerLabelString(),
									dataExpression.getColumnLabelStrings(), selections,
									dataExpression.getNumberOfRows(), OutputType.JSON);
						} else {
							output = handler.getResults(dataExpression.getContainerLabelString(),
									dataExpression.getColumnLabelStrings(), dataExpression.getNumberOfRows(),
									OutputType.JSON);
						}
					}
					ConnectionRepository.checkin(connectionManager.getConnectionPool(dataExpression.getPoolId()),
							handler);
				}
			}
		} catch (Exception exception) {
			throw new AnalyticsException(exception);
		}
		return output.getBytes();
	}
}

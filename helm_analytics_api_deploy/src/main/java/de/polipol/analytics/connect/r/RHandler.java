package de.polipol.analytics.connect.r;

import java.io.IOException;
import java.util.Map;

import de.polipol.analytics.connect.Handler;
import de.polipol.analytics.exception.AnalyticsException;
import de.polipol.analytics.file.FileExtension;

public interface RHandler extends Handler {

	byte[] getDocument(String expression, Map<String, String> parameters, FileExtension sourceFormat,
			FileExtension targetFormat) throws IOException, AnalyticsException;

	byte[] getImage(String expression, Map<String, String> parameters, FileExtension targetFormat, int width,
			int height, int resolution) throws AnalyticsException, IOException, UnsupportedOperationException;

	byte[] getText(String expression, String variable, Map<String, String> preParameters,
			Map<String, String> postParameters, FileExtension targetFormat) throws AnalyticsException, IOException;

	void initialize() throws AnalyticsException, IOException;
}

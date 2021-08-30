package de.polipol.analytics.connect;

import java.util.Map;

import de.polipol.analytics.exception.AnalyticsException;
import de.polipol.analytics.file.FileExtension;

public interface AnalyticsService {

	byte[] getDocument(final FileExtension sourceFormat, final FileExtension targetFormat, final String expression,
			final Map<String, String> parameters) throws AnalyticsException;

	byte[] getImage(final FileExtension targetFormat, final String expression, final Map<String, String> parameters,
			final int width, final int height, final int resolution) throws AnalyticsException;

	String getKernelId();

	byte[] getText(final FileExtension targetFormat, final String expression, final String variable,
			final Map<String, String> preParameters, final Map<String, String> postParameters)
			throws AnalyticsException;
}

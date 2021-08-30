package de.polipol.analytics.web;

import static de.polipol.analytics.commons.TestConstants.TEST_KERNEL_ID;

import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import de.polipol.analytics.connect.AnalyticsService;
import de.polipol.analytics.exception.AnalyticsException;
import de.polipol.analytics.file.FileExtension;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class AnalyticsTestService implements AnalyticsService {

	@Override
	public byte[] getDocument(final FileExtension sourceFormat, final FileExtension targetFormat,
			final String expression, final Map<String, String> parameters) throws AnalyticsException {
		return expression.getBytes();
	}

	@Override
	public byte[] getImage(final FileExtension targetFormat, final String expression,
			final Map<String, String> parameters, final int width, final int height, final int resolution)
			throws AnalyticsException {
		return expression.getBytes();
	}

	@Override
	public String getKernelId() {
		return TEST_KERNEL_ID;
	}

	@Override
	public byte[] getText(final FileExtension targetFormat, final String expression, final String variable,
			final Map<String, String> preParameters, final Map<String, String> postParameters)
			throws AnalyticsException {
		return expression.getBytes();
	}
}

package de.polipol.analytics.connect.r;

import static de.polipol.analytics.commons.Constants.OUTPUT;
import static de.polipol.analytics.commons.Constants.R_KERNEL_ID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.math.R.Rsession;

import de.polipol.analytics.connect.AnalyticsService;
import de.polipol.analytics.connect.ConnectionManager;
import de.polipol.analytics.connect.ConnectionPool;
import de.polipol.analytics.connect.ConnectionPoolFactory;
import de.polipol.analytics.connect.ConnectionRepository;
import de.polipol.analytics.connect.DefaultConnectionManager;
import de.polipol.analytics.exception.AnalyticsException;
import de.polipol.analytics.exception.ConnectionNotEstablishedException;
import de.polipol.analytics.exception.InvalidLabelException;
import de.polipol.analytics.exception.InvalidReasonerException;
import de.polipol.analytics.exception.PoolInitException;
import de.polipol.analytics.exception.PoolNotFoundException;
import de.polipol.analytics.file.FileExtension;

public final class RService implements AnalyticsService {

	private ConnectionManager<Rsession> connectionManager;

	public RService() {
		this.connectionManager = new DefaultConnectionManager<>();
		ConnectionPool<Rsession> connectionPool = ConnectionPoolFactory.createRsessionConnectionPool();
		if (connectionPool != null) {
			connectionPool.setPoolId(R_KERNEL_ID);
			connectionManager.addConnectionPool(R_KERNEL_ID, connectionPool);
		}
		init();
	}

	public RService(final ConnectionManager<Rsession> connectionManager) {
		this.connectionManager = connectionManager;
		init();
	}

	@Override
	public byte[] getDocument(final FileExtension sourceFormat, final FileExtension targetFormat,
			final String expression, final Map<String, String> parameters) throws AnalyticsException {
		try {
			RHandler handler = ConnectionRepository.checkout(connectionManager.getConnectionPool(R_KERNEL_ID));
			byte[] output = handler.getDocument(expression, parameters, sourceFormat, targetFormat);
			ConnectionRepository.checkin(connectionManager.getConnectionPool(R_KERNEL_ID), handler);
			return output;
		} catch (AnalyticsException | PoolNotFoundException | InvalidReasonerException | InvalidLabelException
				| PoolInitException | IOException | ConnectionNotEstablishedException exception) {
			throw new AnalyticsException(exception);
		}
	}

	@Override
	public byte[] getImage(final FileExtension targetFormat, final String expression,
			final Map<String, String> parameters, final int width, final int height, final int resolution)
			throws AnalyticsException {
		try {
			RHandler handler = ConnectionRepository.checkout(connectionManager.getConnectionPool(R_KERNEL_ID));
			byte[] output = handler.getImage(expression, parameters, targetFormat, width, height, resolution);
			ConnectionRepository.checkin(connectionManager.getConnectionPool(R_KERNEL_ID), handler);
			return output;
		} catch (PoolNotFoundException | InvalidReasonerException | InvalidLabelException | PoolInitException
				| IOException | ConnectionNotEstablishedException | UnsupportedOperationException
				| AnalyticsException exception) {
			throw new AnalyticsException(exception);
		}
	}

	@Override
	public String getKernelId() {
		return R_KERNEL_ID;
	}

	@Override
	public byte[] getText(final FileExtension targetFormat, final String expression, final String variable,
			final Map<String, String> preParameters, final Map<String, String> postParameters)
			throws AnalyticsException {
		try {
			RHandler handler = ConnectionRepository.checkout(connectionManager.getConnectionPool(R_KERNEL_ID));
			byte[] output = handler.getText(expression, variable, preParameters, postParameters, targetFormat);
			ConnectionRepository.checkin(connectionManager.getConnectionPool(R_KERNEL_ID), handler);
			return output;
		} catch (PoolNotFoundException | InvalidReasonerException | InvalidLabelException | PoolInitException
				| IOException | ConnectionNotEstablishedException | AnalyticsException exception) {
			throw new AnalyticsException(exception);
		}
	}

	private void init() {
		Map<String, String> postParameters = new HashMap<>();
		Map<String, String> preParameters = new HashMap<>();
		this.getText(FileExtension.TXT, RKeywords.R_INIT_EXPRESSION, OUTPUT, preParameters, postParameters);
	}
}

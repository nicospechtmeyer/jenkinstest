package de.polipol.analytics.connect.data.sap;

import java.util.ArrayList;
import java.util.List;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.execution.jco.JCoContext;
import org.hibersap.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.polipol.analytics.connect.ConnectionPool;

public final class SapConnectionPool extends ConnectionPool<Session> {

	private final Logger logger = LoggerFactory.getLogger(SapConnectionPool.class);

	private SessionManagerConfig managerConfiguration;

	public SapConnectionPool(final String configurationName, final String client, final String userName,
			final String password, final String language, final String host, final String system) {
		super();
		managerConfiguration = new SessionManagerConfig(configurationName).setContext(JCoContext.class.getName())
				.setProperty("jco.client.client", client).setProperty("jco.client.user", userName)
				.setProperty("jco.client.passwd", password).setProperty("jco.client.lang", language)
				.setProperty("jco.client.ashost", host).setProperty("jco.client.sysnr", system);
		List<String> annotatedClasses = new ArrayList<String>();
		annotatedClasses.add("de.polipol.analytics.connect.data.sap.SapAdapter");
		managerConfiguration.setAnnotatedClasses(annotatedClasses);
	}

	@Override
	public Session create() {
		Session session = null;
		try {
			try {
				AnnotationConfiguration configuration = new AnnotationConfiguration(managerConfiguration);
				session = configuration.buildSessionManager().openSession();
			} catch (final Exception exception) {
				exception.printStackTrace();
			}
		} catch (final Exception exception) {
			exception.printStackTrace();
			if (session != null) {
				if (!session.isClosed()) {
					session.close();
				}
			}
			return (null);
		}
		return session;
	}

	@Override
	public void expire(final Session session) {
		try {
			if (!session.isClosed()) {
				session.close();
			}
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public boolean isConnected(final Session session) {
		try {
			return (!session.isClosed());
		} catch (final Exception exception) {
			logger.warn("Error while validating connection");
			return (false);
		}
	}
}
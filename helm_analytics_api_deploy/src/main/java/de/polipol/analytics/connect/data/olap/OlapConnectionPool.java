package de.polipol.analytics.connect.data.olap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.olap4j.OlapConnection;
import org.olap4j.OlapWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.polipol.analytics.connect.ConnectionPool;

public final class OlapConnectionPool extends ConnectionPool<Connection> {

	private final Logger logger = LoggerFactory.getLogger(OlapConnectionPool.class);

	private String password;
	private String url;
	private String userName;

	public OlapConnectionPool(final String driver, final String url, final String userName, final String password) {
		super();
		try {
			Class.forName(driver);
		} catch (final Exception exception) {
			logger.error(exception.getMessage());
		}
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	@Override
	public Connection create() {
		try {
			Connection connection = DriverManager.getConnection(url, userName, password);
			OlapWrapper wrapper = (OlapWrapper) connection;
			OlapConnection olapConnection = null;
			try {
				olapConnection = wrapper.unwrap(OlapConnection.class);
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
			return olapConnection;
		} catch (SQLException exception) {
			logger.warn("Connection cannot be established");
			return null;
		}
	}

	@Override
	public void expire(final Connection o) {
		try {
			o.close();
		} catch (SQLException exception) {
			logger.warn("Connection is not closed correctly");
		}
	}

	@Override
	public boolean isConnected(final Connection o) {
		try {
			return o.isValid(0);
		} catch (SQLException exception) {
			logger.warn("Error while validating connection");
			return false;
		}
	}
}
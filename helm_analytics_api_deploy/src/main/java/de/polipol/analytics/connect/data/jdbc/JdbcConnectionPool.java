package de.polipol.analytics.connect.data.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.polipol.analytics.connect.ConnectionPool;
import de.polipol.analytics.exception.ConnectionNotEstablishedException;

public final class JdbcConnectionPool extends ConnectionPool<Connection> {

	private final Logger logger = LoggerFactory.getLogger(JdbcConnectionPool.class);

	private String password;
	private String url;
	private String userName;

	public JdbcConnectionPool(final String driver, final String url, final String userName, final String password) {
		super();
		try {
			Class.forName(driver).getDeclaredConstructor();
		} catch (final Exception exception) {
			logger.error(exception.getMessage());
		}
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	@Override
	public Connection create() throws ConnectionNotEstablishedException {
		try {
			return (DriverManager.getConnection(url, userName, password));
		} catch (SQLException exception) {
			logger.warn("Connection cannot be established");
			throw new ConnectionNotEstablishedException(exception.getMessage());
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
			return (!o.isClosed());
		} catch (SQLException exception) {
			logger.warn("Error while validating connection");
			return false;
		}
	}
}
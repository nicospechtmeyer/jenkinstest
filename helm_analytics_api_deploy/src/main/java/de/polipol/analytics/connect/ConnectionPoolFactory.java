package de.polipol.analytics.connect;

import java.sql.Connection;

import org.hibersap.session.Session;
import org.math.R.Rsession;

import de.polipol.analytics.connect.data.jdbc.JdbcConnectionPool;
import de.polipol.analytics.connect.data.olap.OlapConnectionPool;
import de.polipol.analytics.connect.data.sap.SapConnectionPool;
import de.polipol.analytics.connect.r.RsessionConnectionPool;

public abstract class ConnectionPoolFactory {

	public static ConnectionPool<Connection> createMSSQLConnectionPool(final String host, final String port,
			final String databaseName, final String userName, final String password, final String driver) {
		ConnectionPool<Connection> connectionPool = new JdbcConnectionPool(driver, "jdbc:sqlserver://" + host + ":"
				+ port + ";databaseName=" + databaseName + ";integratedSecurity=false;", userName, password);
		return connectionPool;
	}

	public static ConnectionPool<Connection> createMySQLConnectionPool(final String host, final String port,
			final String databaseName, final String userName, final String password, final String driver) {
		ConnectionPool<Connection> connectionPool = new JdbcConnectionPool(driver, "jdbc:mysql://" + host + ":" + port
				+ "/" + databaseName + "?serverTimezone=UTC&user=" + userName + "&password=" + password, userName,
				password);
		return connectionPool;
	}

	public static ConnectionPool<Connection> createOlapConnectionPool(final String host, final String port,
			final String databaseName, final String userName, final String password, final String driver) {
		ConnectionPool<Connection> connectionPool = new OlapConnectionPool(driver,
				"jdbc:xmla:Server=http://" + host + ":" + port + "/olap/msmdpump.dll", userName, password);
		return connectionPool;
	}

	public static ConnectionPool<Rsession> createRsessionConnectionPool() {
		ConnectionPool<Rsession> connectionPool = new RsessionConnectionPool();
		return connectionPool;
	}

	public static ConnectionPool<Rsession> createRsessionConnectionPool(final String host, final int port) {
		ConnectionPool<Rsession> connectionPool = new RsessionConnectionPool(host, port);
		return connectionPool;
	}

	public static ConnectionPool<Session> createSapConnectionPool(final String connectionId, final String host,
			final String instance, final String client, final String userName, final String password,
			final String language) {
		ConnectionPool<Session> connectionPool = new SapConnectionPool(connectionId, client, userName, password,
				language, host, instance);
		return connectionPool;
	}
}
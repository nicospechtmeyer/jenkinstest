package de.polipol.analytics.commons.props;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import de.polipol.analytics.connect.ConnectionManager;
import de.polipol.analytics.connect.ConnectionPool;
import de.polipol.analytics.connect.ConnectionPoolFactory;
import de.polipol.analytics.connect.DefaultConnectionManager;

public class ConnectionPropertiesReader extends AbstractPropertiesReader {

	private static final String CLIENT = "client";
	private static final String CONFIG = "config";
	private static final String CONNECTION = "connection";
	private static final String DATABASE = "database";
	private static final String DRIVER = "driver";
	private static final String HOST = "host";
	private static final String INSTANCE = "instance";
	private static final String LANGUAGE = "language";
	private static final String MSSQL = "MSSQL";
	private static final String MYSQL = "MYSQL";
	private static final String NAME = "name";
	private static final String OLAP = "OLAP";
	private static final String PASSWORD = "password";
	private static final String PORT = "port";
	private static final String SAP = "SAP";
	private static final String TYPE = "type";
	private static final String USER = "user";
	public static final String PROPERTIES_FILENAME = "connection.properties";
	public static final Path PROPERTIES_FILE = Paths
			.get(System.getProperty("user.dir") + File.separator + CONFIG + File.separator + PROPERTIES_FILENAME);

	@SuppressWarnings("rawtypes")
	private ConnectionManager connectionManager;

	@SuppressWarnings("rawtypes")
	public ConnectionPropertiesReader() {
		super();
		this.file = PROPERTIES_FILE;
		this.connectionManager = new DefaultConnectionManager();
		init();
	}

	@SuppressWarnings("rawtypes")
	public ConnectionPropertiesReader(final Path file) {
		super();
		this.file = file;
		this.connectionManager = new DefaultConnectionManager();
		init();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void evaluateProperties() {
		for (String connectionId : getconnectionIds()) {
			final String connectionPrefix = CONNECTION + "." + connectionId + ".";
			final String connnectionType = this.properties.getProperty(connectionPrefix + TYPE);
			ConnectionPool<?> connectionPool = null;
			switch (connnectionType.toUpperCase()) {
			case SAP:
				String sapClient = properties.getProperty(connectionPrefix + CLIENT);
				String sapHost = properties.getProperty(connectionPrefix + HOST);
				String sapInstance = properties.getProperty(connectionPrefix + INSTANCE);
				String sapUserName = properties.getProperty(connectionPrefix + USER);
				String sapPassword = properties.getProperty(connectionPrefix + PASSWORD);
				String sapLanguage = properties.getProperty(connectionPrefix + LANGUAGE);
				connectionPool = ConnectionPoolFactory.createSapConnectionPool(connectionId, sapHost, sapInstance,
						sapClient, sapUserName, sapPassword, sapLanguage);
				connectionPool.setPoolId(connectionId);
				connectionManager.addConnectionPool(connectionId, connectionPool);
				break;
			case MSSQL:
				String mssqlHost = properties.getProperty(connectionPrefix + HOST);
				String mssqlPort = properties.getProperty(connectionPrefix + PORT);
				String mssqlDatabaseName = properties.getProperty(connectionPrefix + DATABASE);
				String mssqlUserName = properties.getProperty(connectionPrefix + USER);
				String mssqlPassword = properties.getProperty(connectionPrefix + PASSWORD);
				String mssqlDriver = properties.getProperty(connectionPrefix + DRIVER);
				connectionPool = ConnectionPoolFactory.createMSSQLConnectionPool(mssqlHost, mssqlPort,
						mssqlDatabaseName, mssqlUserName, mssqlPassword, mssqlDriver);
				connectionPool.setPoolId(connectionId);
				connectionManager.addConnectionPool(connectionId, connectionPool);
				break;
			case MYSQL:
				String mysqlHost = properties.getProperty(connectionPrefix + HOST);
				String mysqlPort = properties.getProperty(connectionPrefix + PORT);
				String mysqlDatabaseName = properties.getProperty(connectionPrefix + DATABASE);
				String mysqlUserName = properties.getProperty(connectionPrefix + USER);
				String mysqlPassword = properties.getProperty(connectionPrefix + PASSWORD);
				String mysqlDriver = properties.getProperty(connectionPrefix + DRIVER);
				connectionPool = ConnectionPoolFactory.createMySQLConnectionPool(mysqlHost, mysqlPort,
						mysqlDatabaseName, mysqlUserName, mysqlPassword, mysqlDriver);
				connectionPool.setPoolId(connectionId);
				connectionManager.addConnectionPool(connectionId, connectionPool);
				break;
			case OLAP:
				String olapHost = properties.getProperty(connectionPrefix + HOST);
				String olapPort = properties.getProperty(connectionPrefix + PORT);
				String olapDatabaseName = properties.getProperty(connectionPrefix + DATABASE);
				String olapUserName = properties.getProperty(connectionPrefix + USER);
				String olapPassword = properties.getProperty(connectionPrefix + PASSWORD);
				String olapDriver = properties.getProperty(connectionPrefix + DRIVER);
				connectionPool = ConnectionPoolFactory.createOlapConnectionPool(olapHost, olapPort, olapDatabaseName,
						olapUserName, olapPassword, olapDriver);
				connectionPool.setPoolId(connectionId);
				connectionManager.addConnectionPool(connectionId, connectionPool);
				break;
			}
		}
	}

	public ConnectionManager<?> getConnectionManager() {
		return this.connectionManager;
	}

	private List<String> getconnectionIds() {
		List<String> connectionIds = new ArrayList<String>();
		for (Object key : this.properties.keySet()) {
			String entry = (String) key;
			if (entry.startsWith(CONNECTION) && entry.endsWith(NAME)) {
				String connectionId;
				connectionId = entry.replace(CONNECTION + ".", "");
				connectionId = connectionId.replace("." + NAME, "");
				connectionIds.add(connectionId);
			}
		}
		return connectionIds;
	}
}
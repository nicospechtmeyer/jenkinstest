package de.polipol.analytics.connect;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibersap.session.Session;
import org.math.R.Rsession;

import de.polipol.analytics.connect.data.DataHandler;
import de.polipol.analytics.connect.data.DefaultDataHandler;
import de.polipol.analytics.connect.data.jdbc.JdbcConnectionPool;
import de.polipol.analytics.connect.data.jdbc.RelationalJdbcAdapter;
import de.polipol.analytics.connect.data.olap.MultidimensionalAdapter;
import de.polipol.analytics.connect.data.olap.OlapConnectionPool;
import de.polipol.analytics.connect.data.sap.SapAdapter;
import de.polipol.analytics.connect.data.sap.SapConnectionPool;
import de.polipol.analytics.connect.data.semantic.DefaultDictionary;
import de.polipol.analytics.connect.data.semantic.Dictionary;
import de.polipol.analytics.connect.data.semantic.Reasoner;
import de.polipol.analytics.connect.data.semantic.ReasonerFactory;
import de.polipol.analytics.connect.r.DefaultRHandler;
import de.polipol.analytics.connect.r.RAdapter;
import de.polipol.analytics.connect.r.RHandler;
import de.polipol.analytics.connect.r.RsessionAdapter;
import de.polipol.analytics.connect.r.RsessionConnectionPool;
import de.polipol.analytics.exception.AnalyticsException;
import de.polipol.analytics.exception.ConnectionNotEstablishedException;
import de.polipol.analytics.exception.InvalidLabelException;
import de.polipol.analytics.exception.InvalidReasonerException;
import de.polipol.analytics.exception.PoolInitException;
import de.polipol.analytics.exception.PoolNotFoundException;

public abstract class ConnectionRepository {

	@SuppressWarnings("unchecked")
	public static void checkin(final ConnectionPool<?> connectionPool, final Handler handler)
			throws PoolNotFoundException {
		if (connectionPool instanceof JdbcConnectionPool) {
			Connection connection = ((RelationalJdbcAdapter) handler.getAdapter()).getConnection();
			((ConnectionPool<Connection>) connectionPool).checkIn(connection);
		} else if (connectionPool instanceof SapConnectionPool) {
			Session session = ((SapAdapter) handler.getAdapter()).getConnection();
			((ConnectionPool<Session>) connectionPool).checkIn(session);
		} else if (connectionPool instanceof OlapConnectionPool) {
			Connection connection = ((MultidimensionalAdapter) handler.getAdapter()).getConnection();
			((ConnectionPool<Connection>) connectionPool).checkIn(connection);
		} else if (connectionPool instanceof RsessionConnectionPool) {
			Rsession connection = ((RsessionAdapter) handler.getAdapter()).getConnection();
			((ConnectionPool<Rsession>) connectionPool).checkIn(connection);
		}
	}

	public static RHandler checkout(final ConnectionPool<?> connectionPool)
			throws PoolNotFoundException, InvalidReasonerException, InvalidLabelException, PoolInitException,
			IOException, ConnectionNotEstablishedException {
		if (connectionPool instanceof RsessionConnectionPool) {
			RAdapter adapter = new RsessionAdapter((Rsession) connectionPool.checkOut());
			RHandler handler;
			try {
				handler = new DefaultRHandler(adapter);
			} catch (final AnalyticsException exception) {
				throw new PoolInitException();
			}
			return handler;
		}
		throw new PoolNotFoundException();
	}

	public static Handler checkout(final ConnectionPool<?> connectionPool, final Path dictionaryDirectory)
			throws PoolNotFoundException, InvalidReasonerException, InvalidLabelException, PoolInitException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
			ConnectionNotEstablishedException {
		Adapter adapter = null;
		Reasoner reasoner = ReasonerFactory.createDefaultReasoner();
		Dictionary dictionary = new DefaultDictionary(connectionPool.getPoolId(), dictionaryDirectory, reasoner);
		if (connectionPool instanceof RsessionConnectionPool) {
			adapter = new RsessionAdapter((Rsession) connectionPool.checkOut());
		} else if (connectionPool instanceof JdbcConnectionPool) {
			adapter = new RelationalJdbcAdapter((Connection) connectionPool.checkOut());
		} else if (connectionPool instanceof SapConnectionPool) {
			adapter = new SapAdapter((Session) connectionPool.checkOut());
		} else if (connectionPool instanceof OlapConnectionPool) {
			adapter = new MultidimensionalAdapter((Connection) connectionPool.checkOut());
		} else {
			throw new PoolNotFoundException();
		}
		DataHandler handler = new DefaultDataHandler(dictionary, adapter);
		return handler;
	}

	public static void close(final ConnectionPool<?> connectionPool, final Handler handler)
			throws PoolNotFoundException {
		if (connectionPool instanceof JdbcConnectionPool) {
			Connection connection = ((RelationalJdbcAdapter) handler.getAdapter()).getConnection();
			try {
				connection.close();
			} catch (SQLException exception) {
			}
		} else if (connectionPool instanceof SapConnectionPool) {
			Session session = ((SapAdapter) handler.getAdapter()).getConnection();
			session.close();
		} else if (connectionPool instanceof OlapConnectionPool) {
			Connection connection = ((MultidimensionalAdapter) handler.getAdapter()).getConnection();
			try {
				connection.close();
			} catch (SQLException exception) {
			}
		} else if (connectionPool instanceof RsessionConnectionPool) {
			Rsession connection = ((RsessionAdapter) handler.getAdapter()).getConnection();
			connection.end();
		}
	}
}

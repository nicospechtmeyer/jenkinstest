package de.polipol.analytics.connect;

import de.polipol.analytics.exception.PoolNotFoundException;

public interface ConnectionManager<T> {

	void addConnectionPool(String poolId, ConnectionPool<T> connectionPool);

	ConnectionPool<T> getConnectionPool(String poolId) throws PoolNotFoundException;

	void removeConnectionPool(String poolId);
}

package de.polipol.analytics.connect;

import java.util.HashMap;
import java.util.Map;

import de.polipol.analytics.exception.PoolNotFoundException;

public final class DefaultConnectionManager<T> implements ConnectionManager<T> {

	private Map<String, ConnectionPool<T>> pools;

	public DefaultConnectionManager() {
		super();
		pools = new HashMap<String, ConnectionPool<T>>();
	}

	@Override
	public void addConnectionPool(final String poolId, final ConnectionPool<T> pool) {
		pools.put(poolId, pool);
	}

	@Override
	public ConnectionPool<T> getConnectionPool(final String poolId) throws PoolNotFoundException {
		if (!pools.containsKey(poolId)) {
			throw new PoolNotFoundException();
		}
		return pools.get(poolId);
	}

	@Override
	public void removeConnectionPool(final String poolId) {
		pools.remove(poolId);
	}
}

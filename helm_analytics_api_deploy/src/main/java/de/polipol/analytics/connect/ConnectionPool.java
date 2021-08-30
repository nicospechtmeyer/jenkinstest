package de.polipol.analytics.connect;

import java.util.Enumeration;
import java.util.Hashtable;

import de.polipol.analytics.exception.ConnectionNotEstablishedException;

public abstract class ConnectionPool<T> {

	private final long expirationTime = 1000000;
	private Hashtable<T, Long> locked, unlocked;
	private String poolId;

	public ConnectionPool() {
		locked = new Hashtable<T, Long>();
		unlocked = new Hashtable<T, Long>();
	}

	public synchronized void checkIn(final T t) {
		locked.remove(t);
		unlocked.put(t, System.currentTimeMillis());
	}

	public synchronized T checkOut() throws ConnectionNotEstablishedException {
		long now = System.currentTimeMillis();
		T t;
		if (unlocked.size() > 0) {
			Enumeration<T> e = unlocked.keys();
			while (e.hasMoreElements()) {
				t = e.nextElement();
				if ((now - unlocked.get(t)) > expirationTime) {
					// connection expired
					unlocked.remove(t);
					expire(t);
					t = null;
				} else {
					if (isConnected(t)) {
						unlocked.remove(t);
						locked.put(t, now);
						return (t);
					} else {
						// connection validation failed
						unlocked.remove(t);
						expire(t);
						t = null;
					}
				}
			}
		}
		// no connection available
		t = create();
		locked.put(t, now);
		return (t);
	}

	protected abstract T create() throws ConnectionNotEstablishedException;

	protected abstract void expire(final T t);

	public String getPoolId() {
		return poolId;
	}

	protected abstract boolean isConnected(final T t);

	public void setPoolId(final String poolId) {
		this.poolId = poolId;
	}

	// public synchronized void closeAllConnections(T t) {
	// for (T connection : locked.keySet()) {
	// connection.
	// }
	// }
}
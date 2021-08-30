package de.polipol.analytics.cache;

public interface Cache {

	boolean exists(String key);

	void flushAll();

	byte[] get(byte[] key);

	String get(String key);

// 	boolean isConnected();

//	void publish(String channel, String message);

	void remove(String key);

	void sadd(String key, String member);

	void set(String key, byte[] bytes);

	void set(String key, String value);

//	void subscribe(String channel);
}

package de.polipol.analytics.cache;

import static de.polipol.analytics.commons.Constants.LOCALHOST;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public final class RedisMessageBroker implements MessageBroker {

	protected Jedis jedis;

	public RedisMessageBroker() {
		this.jedis = new Jedis();
		jedis.connect();
	}

	public RedisMessageBroker(int port) {
		this.jedis = new Jedis(LOCALHOST, port);
		jedis.connect();
	}

	public RedisMessageBroker(String host, int port) {
		this.jedis = new Jedis(host, port);
		jedis.connect();
	}

	@Override
	public boolean isConnected() {
		return jedis.isConnected();
	}

	@Override
	public void publish(String channel, String message) {
		this.jedis.publish(channel, message);
	}

	@Override
	public void subscribe(String channel, BrokerTask brokerTask) {
		final JedisPubSub jedisPubSub = new RedisBrokerMessageListener(brokerTask);
		jedis.subscribe(jedisPubSub, channel);
	}
}

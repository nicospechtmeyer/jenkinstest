package de.polipol.analytics.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPubSub;

public class RedisBrokerMessageListener extends JedisPubSub {

	private static final Logger logger = LoggerFactory.getLogger(RedisMessageBroker.class);

	protected BrokerTask brokerTask;

	public RedisBrokerMessageListener(BrokerTask brokerTask) {
		super();
		this.brokerTask = brokerTask;
	}

	@Override
	public void onMessage(String channel, String message) {
		logger.debug("*** SUBSCRIBE channel: " + channel + " - message received: " + message);
		brokerTask.execute(message);
		if (message.equalsIgnoreCase("quit")) {
			this.unsubscribe(channel);
		}
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		logger.info("broker subscribed");
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		logger.info("broker unsubscrinbed");
	}
}
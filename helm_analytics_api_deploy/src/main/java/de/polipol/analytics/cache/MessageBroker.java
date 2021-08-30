package de.polipol.analytics.cache;

public interface MessageBroker {

	boolean isConnected();

	void publish(String channel, String message);

	void subscribe(String channel, BrokerTask brokerTask);
}

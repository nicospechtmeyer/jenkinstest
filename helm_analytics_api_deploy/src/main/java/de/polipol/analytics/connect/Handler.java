package de.polipol.analytics.connect;

import java.util.Date;

import de.polipol.analytics.connect.Adapter.Status;

public interface Handler {

	Adapter getAdapter();

	Long getQueryDuration();

	Date getQueryTimestamp();

	String getStateInformation();

	Status getStatus();

	void setQueryDuration(Long queryDuration);

	void setQueryTimestamp(Date queryTimestamp);
}

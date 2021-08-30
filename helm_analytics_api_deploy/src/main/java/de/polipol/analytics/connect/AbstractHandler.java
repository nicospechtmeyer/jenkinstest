package de.polipol.analytics.connect;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.polipol.analytics.connect.Adapter.Status;

public abstract class AbstractHandler implements Handler {

	protected Long queryDuration;
	protected Date queryTimestamp;

	@Override
	public Long getQueryDuration() {
		return queryDuration;
	}

	@Override
	public Date getQueryTimestamp() {
		return queryTimestamp;
	}

	@Override
	public String getStateInformation() {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(getQueryTimestamp()) + ", " + getStatus() + ", " + queryDuration + "ms";
	}

	@Override
	public Status getStatus() {
		return null;
	}

	@Override
	public void setQueryDuration(final Long queryDuration) {
		this.queryDuration = queryDuration;
	}

	@Override
	public void setQueryTimestamp(final Date queryTimestamp) {
		this.queryTimestamp = queryTimestamp;
	}
}

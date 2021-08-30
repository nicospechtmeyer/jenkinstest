package de.polipol.analytics.model;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.UUID;

public abstract class DefaultJob extends AbstractElement implements Job {

	protected String cronExpression;
	protected String notebookId;
	// jobstatus 0 = stop; 1 = running
	protected int status;
	protected String filters;

	@Override
	public String getCronExpression() {
		return cronExpression;
	}

	@Override
	public String getFilters() {
		return filters;
	}

	@Override
	public String getNotebookId() {
		return notebookId;
	}

	@Override
	public int getStatus() {
		return status;
	}

	protected void init() {
		this.id = UUID.randomUUID().toString();
		this.createdAt = EMPTY;
		this.creator = EMPTY;
		this.cronExpression = EMPTY;
		this.notebookId = EMPTY;
		this.status = 0;
		this.filters = EMPTY;
	}

	@Override
	public void setCronExpression(final String cronExpression) {
		this.cronExpression = cronExpression;
	}

	@Override
	public void setFilters(final String filters) {
		this.filters = filters;
	}

	@Override
	public void setNotebookId(final String id) {
		this.notebookId = id;
	}

	@Override
	public void setStatus(final int status) {
		this.status = status;
	}
}

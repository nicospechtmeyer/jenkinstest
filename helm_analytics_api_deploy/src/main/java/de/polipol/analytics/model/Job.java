package de.polipol.analytics.model;

public interface Job extends Element {

	String getCronExpression();

	String getFilters();

	String getNotebookId();

	int getStatus();

	void setCronExpression(String cronExpression);

	public void setFilters(String filters);

	void setNotebookId(String id);

	void setStatus(int status);
}
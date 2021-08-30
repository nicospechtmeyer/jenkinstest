package de.polipol.analytics.model;

public interface Filter extends Element, Comparable<Filter> {

	@Override
	int compareTo(Filter filter);

	String getFilterString();

	String getFilterText();

	String getNotebookId();

	void setFilterString(String filterString);

	void setFilterText(String filterText);

	void setNotebookId(String notebookId);
}
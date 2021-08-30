package de.polipol.analytics.model;

public interface Element {

	String getCreatedAt();

	String getCreator();

	String getId();

	void setCreatedAt(String createdAt);

	void setCreator(String creator);

	void setId(String id);
}
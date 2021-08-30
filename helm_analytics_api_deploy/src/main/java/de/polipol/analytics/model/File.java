package de.polipol.analytics.model;

public interface File extends Element {

	byte[] getContent();

	long getSize();

	String getTitle();

	void setContent(byte[] bytes);

	void setSize(long size);

	void setTitle(String title);
}
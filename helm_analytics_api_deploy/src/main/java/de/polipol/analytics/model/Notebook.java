package de.polipol.analytics.model;

import java.util.List;

public interface Notebook extends Element {

	List<String> getGroups();

	String getTitle();

	boolean isPersonal();

	boolean isProcessing();

	boolean isShared();

	void setGroups(List<String> groups);

	void setPersonal(boolean personal);

	void setProcessing(boolean processing);

	void setShared(boolean shared);

	void setTitle(String title);
}
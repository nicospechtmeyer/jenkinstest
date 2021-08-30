package de.polipol.analytics.model;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultNotebook extends AbstractElement implements Notebook {

	protected String title;
	protected List<String> groups;
	protected boolean personal;
	protected boolean processing;
	protected boolean shared;

	public DefaultNotebook() {
		this.init();
	}

	@Override
	public List<String> getGroups() {
		return groups;
	}

	@Override
	public String getTitle() {
		return title;
	}

	protected void init() {
		this.id = UUID.randomUUID().toString();
		this.creator = EMPTY;
		this.createdAt = EMPTY;
		this.title = EMPTY;
		this.personal = true;
		this.processing = false;
		this.shared = false;
		this.groups = new ArrayList<>();
	}

	@Override
	public boolean isPersonal() {
		return personal;
	}

	@Override
	public boolean isProcessing() {
		return processing;
	}

	@Override
	public boolean isShared() {
		return shared;
	}

	@Override
	public void setGroups(final List<String> groups) {
		this.groups = groups;
	}

	@Override
	public void setPersonal(final boolean personal) {
		this.personal = personal;
	}

	@Override
	public void setProcessing(final boolean processing) {
		this.processing = processing;
	}

	@Override
	public void setShared(final boolean shared) {
		this.shared = shared;
	}

	@Override
	public void setTitle(final String title) {
		this.title = title;
	}
}

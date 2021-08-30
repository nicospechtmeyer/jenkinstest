package de.polipol.analytics.model;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.UUID;

public class DefaultFilter extends AbstractElement implements Filter {

	protected String notebookId;
	protected String filterString;
	protected String filterText;

	public DefaultFilter() {
		this.init();
	}

	@Override
	public int compareTo(final Filter filter) {
		if (Integer.valueOf(filter.getCreatedAt()) < Integer.valueOf(this.createdAt)) {
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	public String getFilterString() {
		return filterString;
	}

	@Override
	public String getFilterText() {
		return filterText;
	}

	@Override
	public String getNotebookId() {
		return notebookId;
	}

	protected void init() {
		this.id = UUID.randomUUID().toString();
		this.creator = EMPTY;
		this.createdAt = EMPTY;
		this.notebookId = EMPTY;
		this.filterString = EMPTY;
		this.filterText = EMPTY;
	}

	@Override
	public void setFilterString(final String filterString) {
		this.filterString = filterString;
	}

	@Override
	public void setFilterText(final String filterText) {
		this.filterText = filterText;
	}

	@Override
	public void setNotebookId(final String notebookId) {
		this.notebookId = notebookId;
	}
}
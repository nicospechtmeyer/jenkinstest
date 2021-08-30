package de.polipol.analytics.model;

public abstract class AbstractElement implements Element {

	protected transient String id;
	protected String createdAt;
	protected String creator;

	@Override
	public String getCreatedAt() {
		return createdAt;
	}

	@Override
	public String getCreator() {
		return creator;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
}
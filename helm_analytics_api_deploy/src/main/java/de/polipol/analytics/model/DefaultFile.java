package de.polipol.analytics.model;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

import de.polipol.analytics.commons.Utils;

public class DefaultFile extends AbstractElement implements File {

	protected String title;
	protected long size;
	protected byte[] content;

	public DefaultFile() {
		this.id = UUID.randomUUID().toString();
		this.createdAt = EMPTY;
		this.creator = EMPTY;
		this.title = EMPTY;
		this.size = 0;
		this.content = new byte[] {};
	}

	public DefaultFile(File file) {
		this.id = file.getId();
		this.createdAt = file.getCreatedAt();
		this.creator = file.getCreator();
		this.title = file.getTitle();
		this.size = file.getSize();
		this.content = file.getContent();
	}

	public DefaultFile(String id, byte[] content) {
		this.id = id;
		this.createdAt = EMPTY;
		this.creator = EMPTY;
		this.title = EMPTY;
		this.size = 0;
		this.content = content;
	}

	public DefaultFile(String id, java.io.File file, boolean content) {
		this.id = id;
		this.setTitle(file.getName());
		this.setCreator(EMPTY);
		this.setSize(file.length());
		try {
			this.setCreatedAt(Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime().toString());
			if (content) {
				this.content = Utils.getByteArray(file.toPath());
			}
		} catch (IOException exception) {
		}
	}

	@Override
	public byte[] getContent() {
		return this.content;
	}

	@Override
	public long getSize() {
		return this.size;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}
}
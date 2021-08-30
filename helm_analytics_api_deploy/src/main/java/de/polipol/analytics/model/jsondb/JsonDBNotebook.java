package de.polipol.analytics.model.jsondb;

import static de.polipol.analytics.model.jsondb.JsonDBConstants.CONTAINER_NOTEBOOKS;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import de.polipol.analytics.model.DefaultNotebook;
import de.polipol.analytics.model.Notebook;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = CONTAINER_NOTEBOOKS, schemaVersion = "1.0")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonDBNotebook extends DefaultNotebook {

	@Id
	private String id;

	public JsonDBNotebook() {
		this.id = UUID.randomUUID().toString();
		init();
	}

	public JsonDBNotebook(Notebook notebook) {
		if (StringUtils.isEmpty(notebook.getId())) {
			this.id = UUID.randomUUID().toString();
		} else {
			this.id = notebook.getId();
		}
		this.createdAt = notebook.getCreatedAt();
		this.creator = notebook.getCreator();
		this.groups = notebook.getGroups();
		this.title = notebook.getTitle();
		this.personal = notebook.isPersonal();
		this.processing = notebook.isProcessing();
		this.shared = notebook.isShared();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
}

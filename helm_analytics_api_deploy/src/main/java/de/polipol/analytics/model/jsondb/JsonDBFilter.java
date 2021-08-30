package de.polipol.analytics.model.jsondb;

import static de.polipol.analytics.model.jsondb.JsonDBConstants.CONTAINER_FILTERS;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import de.polipol.analytics.model.DefaultFilter;
import de.polipol.analytics.model.Filter;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = CONTAINER_FILTERS, schemaVersion = "1.0")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonDBFilter extends DefaultFilter {

	@Id
	protected String id;

	public JsonDBFilter() {
		this.id = UUID.randomUUID().toString();
		init();
	}

	public JsonDBFilter(Filter filter) {
		if (StringUtils.isEmpty(filter.getId())) {
			this.id = UUID.randomUUID().toString();
		} else {
			this.id = filter.getId();
		}
		this.createdAt = filter.getCreatedAt();
		this.creator = filter.getCreator();
		this.notebookId = filter.getNotebookId();
		this.filterString = filter.getFilterString();
		this.filterText = filter.getFilterText();
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

package de.polipol.analytics.model.jsondb;

import static de.polipol.analytics.model.jsondb.JsonDBConstants.CONTAINER_JOBS;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import de.polipol.analytics.model.DefaultJob;
import de.polipol.analytics.model.Job;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = CONTAINER_JOBS, schemaVersion = "1.0")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonDBJob extends DefaultJob {

	@Id
	protected String id;

	public JsonDBJob() {
		this.id = UUID.randomUUID().toString();
		init();
	}

	public JsonDBJob(Job job) {
		if (StringUtils.isEmpty(job.getId())) {
			this.id = UUID.randomUUID().toString();
		} else {
			this.id = job.getId();
		}
		this.createdAt = job.getCreatedAt();
		this.creator = job.getCreator();
		this.cronExpression = job.getCronExpression();
		this.notebookId = job.getNotebookId();
		this.status = job.getStatus();
		this.filters = job.getFilters();
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
package de.polipol.analytics.model.jsondb;

import java.util.Collection;
import java.util.Optional;

import de.polipol.analytics.cache.CacheService;
import de.polipol.analytics.model.Job;
import de.polipol.analytics.model.ModelService;

public final class JsonDBJobService extends JsonDBAbstractModelService<Job> implements ModelService<Job> {

	public JsonDBJobService(CacheService<Job> cacheService) {
		super(JsonDBJob.class, cacheService);
	}

	@Override
	public void delete(String id) {
		this.delete(JsonDBJob.class, id);
	}

	@Override
	public Optional<Job> find(String id, boolean useCache) {
		return this.findById(JsonDBJob.class, id, useCache);
	}

	@Override
	public Collection<Job> getAll() {
		return this.getAll(JsonDBJob.class);
	}

	@Override
	public Collection<Job> getAll(String selectorIdentifier, String selector) {
		return this.getAll(JsonDBJob.class, selectorIdentifier, selector);
	}

	@Override
	public Job upsert(Job job) {
		// this.upsert(JsonDBJob.class, job.getId(), new JsonDBJob(job));
		JsonDBJob jsonDBJob = new JsonDBJob(job);
		this.upsert(JsonDBJob.class, job.getId(), jsonDBJob);
		return jsonDBJob;
	}
}
package de.polipol.analytics.model.jsondb;

import java.util.Collection;
import java.util.Optional;

import de.polipol.analytics.cache.CacheService;
import de.polipol.analytics.model.Filter;
import de.polipol.analytics.model.ModelService;

public final class JsonDBFilterService extends JsonDBAbstractModelService<Filter> implements ModelService<Filter> {

	public JsonDBFilterService(CacheService<Filter> cacheService) {
		super(JsonDBFilter.class, cacheService);
	}

	@Override
	public void delete(String id) {
		this.delete(JsonDBFilter.class, id);
	}

	@Override
	public Optional<Filter> find(String id, boolean useCache) {
		return this.findById(JsonDBFilter.class, id, useCache);
	}

	@Override
	public Collection<Filter> getAll() {
		return this.getAll(JsonDBFilter.class);
	}

	@Override
	public Collection<Filter> getAll(String selectorIdentifier, String selector) {
		return this.getAll(JsonDBFilter.class, selectorIdentifier, selector);
	}

	@Override
	public Filter upsert(Filter filter) {
		JsonDBFilter jsonDBFilter = new JsonDBFilter(filter);
		this.upsert(JsonDBFilter.class, filter.getId(), jsonDBFilter);
		return jsonDBFilter;
	}
}
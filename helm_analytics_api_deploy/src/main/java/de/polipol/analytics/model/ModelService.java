package de.polipol.analytics.model;

import java.util.Collection;
import java.util.Optional;

import de.polipol.analytics.cache.Cache;

public interface ModelService<T> {

	void delete(String id);

	Optional<T> find(String id, boolean useCache);

	Collection<T> getAll();

	Collection<T> getAll(String roleIdentifier, String role);

	Cache getCache();

	T upsert(T element);
}
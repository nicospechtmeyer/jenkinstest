package de.polipol.analytics.cache;

import java.util.Optional;

import de.polipol.analytics.commons.Elements;

public interface CacheService<T> {

//	String createKey(String id);

	void delete(String key);

	boolean exists(String key);

	Optional<T> find(String key, Class<? extends T> clazz);

	Optional<Cache> getCache();

	Elements getType();

	void upsert(String key, T element);
}
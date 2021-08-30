package de.polipol.analytics.cache;

import java.util.Optional;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.polipol.analytics.commons.Elements;

public class GenericCacheService<T> implements CacheService<T> {

//	private static final Logger logger = LoggerFactory.getLogger(GenericCacheService.class);

	protected Cache cache;
	protected Elements type;

	public GenericCacheService(Cache cache, Elements type) {
		this.cache = cache;
		this.type = type;
	}

//	public String createKey(String id) {
//		return this.type + CACHE_SEPARATOR + id;
//	}

	@Override
	public void delete(String key) {
//		String key = this.createKey(id);
		this.cache.remove(key);
	}

	@Override
	public boolean exists(String key) {
//		String key = this.createKey(id);
		return this.cache.exists(key);
	}

	@Override
	public Optional<T> find(String key, Class<? extends T> clazz) {
		if (this.exists(key)) {
			// String key = this.createKey(id);
			Gson gson = new Gson();
			String value = this.cache.get(key);
			return Optional.of(gson.fromJson(value, clazz));
		}
		return Optional.empty();
	}

	@Override
	public Optional<Cache> getCache() {
		return Optional.of(this.cache);
	}

	@Override
	public Elements getType() {
		return this.type;
	}

	@Override
	public void upsert(String key, T element) {
//		String key = this.createKey(id);
		Gson gson = new Gson();
		this.cache.set(key, gson.toJson(element));
	}
}
package de.polipol.analytics.cache;

import static de.polipol.analytics.commons.Constants.CACHE_SEPARATOR;

import java.util.Optional;

import de.polipol.analytics.commons.Elements;
import de.polipol.analytics.model.DefaultFile;
import de.polipol.analytics.model.File;

public class FileCacheService implements CacheService<File> {

	protected Cache cache;
	protected Elements type;

	public FileCacheService(Cache cache) {
		this.cache = cache;
		this.type = Elements.FILES;
	}

	public String createKey(String id) {
		return this.type + CACHE_SEPARATOR + id;
	}

	@Override
	public void delete(String key) {
		this.cache.remove(key);
	}

	@Override
	public boolean exists(String key) {
		return this.cache.exists(key);
	}

	@Override
	public Optional<File> find(String key, Class<? extends File> clazz) {
		if (this.exists(key)) {
			byte[] value = this.cache.get(key.getBytes());
			File file = new DefaultFile();
			file.setContent(value);
			return Optional.of(file);
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
	public void upsert(String key, File element) {
		this.cache.set(key, element.getContent());
	}
}
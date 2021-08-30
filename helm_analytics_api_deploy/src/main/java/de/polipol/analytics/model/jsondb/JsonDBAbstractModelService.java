package de.polipol.analytics.model.jsondb;

import static de.polipol.analytics.commons.Constants.DB_FILES_LOCATION;
import static de.polipol.analytics.commons.Constants.DEFAULT_DB_BASE_SCAN_PACKAGE;
import static de.polipol.analytics.commons.Constants.DEFAULT_DB_FILES_LOCATION;
import static de.polipol.analytics.commons.Constants.LOGGER_SEPARATOR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.polipol.analytics.cache.Cache;
import de.polipol.analytics.cache.CacheService;
import de.polipol.analytics.commons.Utils;
import io.jsondb.JsonDBTemplate;

public abstract class JsonDBAbstractModelService<T> {

	private static final Logger logger = LoggerFactory.getLogger(JsonDBAbstractModelService.class);

	protected CacheService<T> cacheService;
	protected JsonDBTemplate jsonDBTemplate;

	public JsonDBAbstractModelService(Class<? extends T> clazz, CacheService<T> cacheService) {
		String dbFiles = Utils.getEnv(DB_FILES_LOCATION, DEFAULT_DB_FILES_LOCATION);
		this.cacheService = cacheService;
		this.jsonDBTemplate = new JsonDBTemplate(dbFiles, DEFAULT_DB_BASE_SCAN_PACKAGE);
		logger.info(LOGGER_SEPARATOR);
		logger.info(DB_FILES_LOCATION + " " + clazz.getName() + ": " + dbFiles);
		logger.info(LOGGER_SEPARATOR);
		if (!this.jsonDBTemplate.collectionExists(clazz)) {
			this.jsonDBTemplate.createCollection(clazz);
		}
	}

	public void delete(Class<? extends T> clazz, String id) {
		this.cacheService.delete(id);
		T element = this.jsonDBTemplate.findById(id, clazz);
		if (element != null) {
			this.jsonDBTemplate.remove(element, clazz);
		}
	}

	public Optional<T> findById(Class<? extends T> clazz, String id, boolean useCache) {
		Optional<T> element = Optional.empty();
		if (useCache) {
			element = this.cacheService.find(id, clazz);
		}
		if (element.isEmpty()) {
			this.jsonDBTemplate.reloadCollection(getContainerString());
			element = Optional.ofNullable(this.jsonDBTemplate.findById(id, clazz));
			if (element.isPresent()) {
				cacheService.upsert(id, element.get());
			}
		}
		return element;
	}

	public Collection<T> getAll(Class<? extends T> clazz) {
		this.jsonDBTemplate.reloadCollection(getContainerString());
		return new ArrayList<T>(this.jsonDBTemplate.findAll(clazz));
	}

	public Collection<T> getAll(Class<? extends T> clazz, String selectorIdentifier, String selector) {
		this.jsonDBTemplate.reloadCollection(getContainerString());
		String query = String.format("/.[" + selectorIdentifier + "='%s']", selector);
		return new ArrayList<T>(this.jsonDBTemplate.find(query, clazz));
	}

	public Cache getCache() {
		return this.cacheService.getCache().orElseThrow(IllegalStateException::new);
	}

	protected String getContainerString() {
		return cacheService.getType().toString().toLowerCase();
	}

	public void upsert(Class<? extends T> clazz, String id, T element) {
		this.jsonDBTemplate.upsert(element);
		this.cacheService.upsert(id, element);
	}
}
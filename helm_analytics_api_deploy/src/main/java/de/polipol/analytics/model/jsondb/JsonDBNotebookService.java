package de.polipol.analytics.model.jsondb;

import java.util.Collection;
import java.util.Optional;

import de.polipol.analytics.cache.CacheService;
import de.polipol.analytics.model.ModelService;
import de.polipol.analytics.model.Notebook;

public final class JsonDBNotebookService extends JsonDBAbstractModelService<Notebook>
		implements ModelService<Notebook> {

	public JsonDBNotebookService(CacheService<Notebook> cacheService) {
		super(JsonDBNotebook.class, cacheService);
	}

	@Override
	public void delete(String id) {
		this.delete(JsonDBNotebook.class, id);
	}

	@Override
	public Optional<Notebook> find(String id, boolean useCache) {
		return this.findById(JsonDBNotebook.class, id, useCache);
	}

	@Override
	public Collection<Notebook> getAll() {
		return this.getAll(JsonDBNotebook.class);
	}

	@Override
	public Collection<Notebook> getAll(String selectorIdentifier, String selector) {
		return this.getAll(JsonDBNotebook.class, selectorIdentifier, selector);
	}

	@Override
	public Notebook upsert(Notebook notebook) {
		JsonDBNotebook jsonDBNotebook = new JsonDBNotebook(notebook);
		this.upsert(JsonDBNotebook.class, notebook.getId(), jsonDBNotebook);
		return jsonDBNotebook;
	}
}
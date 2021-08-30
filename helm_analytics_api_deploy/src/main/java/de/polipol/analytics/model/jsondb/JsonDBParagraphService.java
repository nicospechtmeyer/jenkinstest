package de.polipol.analytics.model.jsondb;

import java.util.Collection;
import java.util.Optional;

import de.polipol.analytics.cache.CacheService;
import de.polipol.analytics.model.ModelService;
import de.polipol.analytics.model.Paragraph;

public final class JsonDBParagraphService extends JsonDBAbstractModelService<Paragraph>
		implements ModelService<Paragraph> {

	public JsonDBParagraphService(CacheService<Paragraph> cacheService) {
		super(JsonDBParagraph.class, cacheService);
	}

	@Override
	public void delete(String id) {
		this.delete(JsonDBParagraph.class, id);
	}

	@Override
	public Optional<Paragraph> find(String id, boolean useCache) {
		return this.findById(JsonDBParagraph.class, id, useCache);
	}

	@Override
	public Collection<Paragraph> getAll() {
		return this.getAll(JsonDBParagraph.class);
	}

	@Override
	public Collection<Paragraph> getAll(String roleIdentifier, String role) {
		return this.getAll(JsonDBParagraph.class, roleIdentifier, role);
	}

	@Override
	public Paragraph upsert(Paragraph paragraph) {
		JsonDBParagraph jsonDBParagraph = new JsonDBParagraph(paragraph);
		this.upsert(JsonDBParagraph.class, paragraph.getId(), jsonDBParagraph);
		return jsonDBParagraph;
	}
}
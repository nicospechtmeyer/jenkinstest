package de.polipol.analytics.model.jsondb;

import static de.polipol.analytics.commons.TestConstants.CREATED_AT;
import static de.polipol.analytics.commons.TestConstants.CREATOR;
import static de.polipol.analytics.commons.TestConstants.GROUPS;
import static de.polipol.analytics.commons.TestConstants.TITLE;
import static de.polipol.analytics.commons.TestConstants.VIEW_TYPES_VALUE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.fppt.jedismock.RedisServer;

import de.polipol.analytics.cache.Cache;
import de.polipol.analytics.cache.GenericCacheService;
import de.polipol.analytics.cache.RedisCache;
import de.polipol.analytics.commons.Elements;
import de.polipol.analytics.exception.ElementNotFoundException;
import de.polipol.analytics.model.ModelService;
import de.polipol.analytics.model.Notebook;
import de.polipol.analytics.model.Paragraph;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class JsonDBModelServiceTest {

	private RedisServer server;

	@After
	public void after() {
		this.server.stop();
		this.server = null;
	}

	@Test
	public void createAndRemoveNotebook() throws ElementNotFoundException {
		Cache cache = new RedisCache(server.getHost(), server.getBindPort());
		cache.flushAll();

		GenericCacheService<Notebook> cacheService = new GenericCacheService<>(cache, Elements.NOTEBOOKS);
		ModelService<Notebook> modelService = new JsonDBNotebookService(cacheService);

		JsonDBNotebook notebook1, notebook2;
		notebook1 = new JsonDBNotebook();
		notebook1.setTitle(TITLE);
		notebook1.setCreatedAt(CREATED_AT);
		notebook1.setCreator(CREATOR);
		notebook1.setPersonal(true);
		notebook1.setProcessing(true);
		notebook1.setShared(true);
		notebook1.setGroups(Arrays.asList(GROUPS));

		assertThat(modelService.find(notebook1.getId(), false).isEmpty(), equalTo(true));
		assertThat(modelService.find(notebook1.getId(), true).isEmpty(), equalTo(true));

		modelService.upsert(notebook1);
		notebook2 = (JsonDBNotebook) modelService.find(notebook1.getId(), false).get();

		assertThat(notebook2.getId(),
				equalTo(cacheService.find(notebook1.getId(), JsonDBNotebook.class).get().getId()));
		assertThat(notebook2.getId(), equalTo(notebook1.getId()));
		assertThat(notebook2.getTitle(), equalTo(TITLE));
		assertThat(notebook2.getCreatedAt(), equalTo(CREATED_AT));
		assertThat(notebook2.isPersonal(), equalTo(true));
		assertThat(notebook2.isProcessing(), equalTo(true));
		assertThat(notebook2.isShared(), equalTo(true));
		assertThat(notebook2.getGroups().size(), equalTo(GROUPS.length));

		modelService.delete(notebook1.getId());
	}

	@Test
	public void createAndRemoveParagraph() throws ElementNotFoundException {
		Cache cache = new RedisCache(server.getHost(), server.getBindPort());
		cache.flushAll();

		GenericCacheService<Paragraph> cacheService = new GenericCacheService<>(cache, Elements.PARAGRAPHS);
		ModelService<Paragraph> modelService = new JsonDBParagraphService(cacheService);

		JsonDBParagraph paragraph1, paragraph2;
		paragraph1 = new JsonDBParagraph();
		paragraph1.setTitle(TITLE);
		paragraph1.setCreatedAt(CREATED_AT);
		paragraph1.setCreator(CREATOR);
		paragraph1.setOrderId(3);
		paragraph1.setViewTypes(Arrays.toString(VIEW_TYPES_VALUE));
		assertThat(modelService.find(paragraph1.getId(), false).isEmpty(), equalTo(true));
		assertThat(modelService.find(paragraph1.getId(), true).isEmpty(), equalTo(true));

		modelService.upsert(paragraph1);
		paragraph2 = (JsonDBParagraph) modelService.find(paragraph1.getId(), false).get();

		assertThat(paragraph2.getId(),
				equalTo(cacheService.find(paragraph1.getId(), JsonDBParagraph.class).get().getId()));
		assertThat(paragraph2.getId(), equalTo(paragraph1.getId()));
		assertThat(paragraph2.getTitle(), equalTo(TITLE));
		assertThat(paragraph2.getCreatedAt(), equalTo(CREATED_AT));
		assertThat(paragraph2.getOrderId(), equalTo(3));
		assertThat(paragraph2.getViewTypes().split(",").length, equalTo(VIEW_TYPES_VALUE.length));

		modelService.delete(paragraph1.getId());
	}

	@Before
	public void init() throws IOException {
		this.server = new RedisServer();
		this.server.start();
	}
}

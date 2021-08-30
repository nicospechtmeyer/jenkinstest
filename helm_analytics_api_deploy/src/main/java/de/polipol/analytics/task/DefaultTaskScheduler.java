package de.polipol.analytics.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;

import de.polipol.analytics.cache.Cache;
import de.polipol.analytics.cache.CacheService;
import de.polipol.analytics.cache.GenericCacheService;
import de.polipol.analytics.cache.Message;
import de.polipol.analytics.cache.MessageBroker;
import de.polipol.analytics.commons.Elements;
import de.polipol.analytics.file.FileService;
import de.polipol.analytics.model.DefaultParagraph;
import de.polipol.analytics.model.Paragraph;
import de.polipol.analytics.web.ServerFactory;

public class DefaultTaskScheduler implements TaskScheduler {

	protected Cache cache;

	public DefaultTaskScheduler(Cache cache) {
		this.cache = cache;
	}

	@Override
	public void schedule(String connectionId, String role, String paragraphId) {
		schedule(connectionId, role, paragraphId, new HashMap<>());
	}

	@Override
	public void schedule(String connectionId, String role, String paragraphId, Map<String, String> variables) {
		CacheService<Paragraph> paragraphService = new GenericCacheService<Paragraph>(cache, Elements.PARAGRAPHS);
		Optional<Paragraph> paragraph = paragraphService.find(paragraphId, DefaultParagraph.class);

		if (paragraph.isPresent()) {
			String directoryString = FileService.getDirectoryString(paragraph.get().getNotebookId(), variables);
			String targetId = FileService.getId(directoryString, role,
					paragraphId + "." + TaskService.getFileExtension(paragraph.get()).toString().toLowerCase());
			Message message = new Message(role, targetId, paragraphId, variables);

			Optional<MessageBroker> broker = ServerFactory.createBroker();
			if (broker.isPresent()) {
				Gson gson = new Gson();
				broker.get().publish(connectionId, gson.toJson(message));
			}
			cache.set(targetId, new Gson().toJson(message));
		}
	}
}

package de.polipol.analytics.cache;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import de.polipol.analytics.commons.Elements;
import de.polipol.analytics.model.ModelService;
import de.polipol.analytics.model.Paragraph;
import de.polipol.analytics.model.jsondb.JsonDBParagraphService;
import de.polipol.analytics.task.TaskService;
import de.polipol.analytics.web.ServerFactory;

public class DefaultBrokerTask implements BrokerTask {

	protected TaskService taskService;

	public DefaultBrokerTask(TaskService taskService) {
		this.taskService = taskService;
	}

	@Override
	public void execute(String json) {
		Gson gson = new Gson();
		Message message = gson.fromJson(json, Message.class);
		Cache cache = ServerFactory.createCache().get();

		GenericCacheService<Paragraph> paragraphCacheService = new GenericCacheService<>(cache, Elements.PARAGRAPHS);
		ModelService<Paragraph> paragraphService = new JsonDBParagraphService(paragraphCacheService);

		if (StringUtils.isNotEmpty(message.getParagraphId())) {
			Optional<Paragraph> paragraph = paragraphService.find(message.getParagraphId(), true);
			if (paragraph.isPresent()) {
				byte[] bytes = taskService.evaluateParagraph(paragraph.get(), message.getVariables(),
						message.getRole());
				cache.set(message.getTargetKey(), bytes);
			}
		}
	}
}

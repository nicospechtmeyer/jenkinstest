package de.polipol.analytics.web;

import static de.polipol.analytics.commons.Constants.DEFAULT_DICTIONARY_FOLDER;
import static de.polipol.analytics.commons.Constants.DEFAULT_PRIVATE_FOLDER;
import static de.polipol.analytics.commons.Constants.DEFAULT_PUBLIC_FOLDER;
import static de.polipol.analytics.commons.Constants.DEFAULT_REDIS_HOST;
import static de.polipol.analytics.commons.Constants.DEFAULT_REDIS_PORT;
import static de.polipol.analytics.commons.Constants.DICTIONARY_FOLDER;
import static de.polipol.analytics.commons.Constants.LOGGER_SEPARATOR;
import static de.polipol.analytics.commons.Constants.PRIVATE_FOLDER;
import static de.polipol.analytics.commons.Constants.PUBLIC_FOLDER;
import static de.polipol.analytics.commons.Constants.REDIS_HOST;
import static de.polipol.analytics.commons.Constants.REDIS_PORT;
import static de.polipol.analytics.commons.Constants.R_KERNEL_ID;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fppt.jedismock.RedisServer;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

import de.polipol.analytics.cache.Cache;
import de.polipol.analytics.cache.DefaultBrokerTask;
import de.polipol.analytics.cache.FileCacheService;
import de.polipol.analytics.cache.GenericCacheService;
import de.polipol.analytics.cache.MessageBroker;
import de.polipol.analytics.cache.RedisCache;
import de.polipol.analytics.cache.RedisMessageBroker;
import de.polipol.analytics.commons.Elements;
import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.commons.props.InterpreterPropertiesReader;
import de.polipol.analytics.connect.AnalyticsService;
import de.polipol.analytics.connect.data.DataService;
import de.polipol.analytics.connect.r.RService;
import de.polipol.analytics.connect.r.RTaskService;
import de.polipol.analytics.file.DefaultFileService;
import de.polipol.analytics.file.FileService;
import de.polipol.analytics.file.FilesystemService;
import de.polipol.analytics.model.Filter;
import de.polipol.analytics.model.Interpreter;
import de.polipol.analytics.model.ModelService;
import de.polipol.analytics.model.Notebook;
import de.polipol.analytics.model.Paragraph;
import de.polipol.analytics.model.jsondb.JsonDBFilterService;
import de.polipol.analytics.model.jsondb.JsonDBNotebookService;
import de.polipol.analytics.model.jsondb.JsonDBParagraphService;
import de.polipol.analytics.task.TaskService;

public final class ServerFactory {

	private static final Logger logger = LoggerFactory.getLogger(ServerFactory.class);

	private static final int DEFAULT_FILE_PORT = 8082;
	private static final int DEFAULT_MODEL_PORT = 8084;

	public static Optional<MessageBroker> createBroker() {
		Optional<MessageBroker> broker = Optional.empty();
		String redisHost = Utils.getEnv(REDIS_HOST, DEFAULT_REDIS_HOST);
		int redisPort = Integer.parseInt(Utils.getEnv(REDIS_PORT, DEFAULT_REDIS_PORT));

		if (Utils.hasCacheMock()) {
			RedisServer server;
			try {
				server = new RedisServer();
				server.start();
				redisHost = server.getHost();
				redisPort = server.getBindPort();
			} catch (IOException exception) {
			}
		}
		broker = Optional.of(new RedisMessageBroker(redisHost, redisPort));
		return broker;
	}

	public static Optional<Cache> createCache() {
		Optional<Cache> cache = Optional.empty();
		String redisHost = Utils.getEnv(REDIS_HOST, DEFAULT_REDIS_HOST);
		int redisPort = Integer.parseInt(Utils.getEnv(REDIS_PORT, DEFAULT_REDIS_PORT));

		if (Utils.hasCacheMock()) {
			RedisServer server;
			try {
				server = new RedisServer();
				server.start();
				redisHost = server.getHost();
				redisPort = server.getBindPort();
			} catch (IOException exception) {
			}
		}
		cache = Optional.of(new RedisCache(redisHost, redisPort));
		return cache;
	}

	public static void importParagraphs() {
		Cache cache = createCache().orElseThrow(IllegalStateException::new);
		cache.flushAll();

		GenericCacheService<Paragraph> paragraphCacheService = new GenericCacheService<>(cache, Elements.PARAGRAPHS);
		ModelService<Paragraph> paragraphService = new JsonDBParagraphService(paragraphCacheService);

		Collection<Paragraph> paragraphs = paragraphService.getAll();

		if (CollectionUtils.isNotEmpty(paragraphs)) {
			logger.info(LOGGER_SEPARATOR);
			for (Paragraph paragraph : paragraphs) {
				paragraphCacheService.upsert(paragraph.getId(), paragraph);
			}
			logger.info(paragraphs.size() + " paragraphs cached.");
			logger.info(LOGGER_SEPARATOR);
		}
	}

	public static void startAll() {
		startFileServer(createCache().orElseThrow(IllegalStateException::new), DEFAULT_FILE_PORT);
		startJsonDBModelServer(createCache().orElseThrow(IllegalStateException::new), DEFAULT_MODEL_PORT);
	}

	public static void startDataServer(int port) {
		Path dictionaryFilesFolder = Paths.get(Utils.getEnv(DICTIONARY_FOLDER, DEFAULT_DICTIONARY_FOLDER));		
		AnalyticsService analyticsService = new DataService(dictionaryFilesFolder);
		MicroAnalyticsServer.start(analyticsService, port);
	}

	public static void startExtendedRServer(int port) {
		// Cache cache = createCache().orElseThrow(IllegalStateException::new);
		// FileCacheService cacheService = new FileCacheService(cache);
		Path privateFilesFolder = Paths.get(Utils.getEnv(PRIVATE_FOLDER, DEFAULT_PRIVATE_FOLDER));
		Path publicFilesFolder = Paths.get(Utils.getEnv(PUBLIC_FOLDER, DEFAULT_PUBLIC_FOLDER));
		logger.info(LOGGER_SEPARATOR);
		logger.info(PRIVATE_FOLDER + ": " + privateFilesFolder);
		logger.info(PUBLIC_FOLDER + ": " + publicFilesFolder);
		logger.info(LOGGER_SEPARATOR);
		FileService fileService = new FilesystemService(privateFilesFolder, publicFilesFolder);
		AnalyticsService analyticsService = new RService();

		MicroAnalyticsServer.startExtendedMode(analyticsService, fileService, port);
		// subscribeRMessageBroker();
	}

	private static FileService getFileService(Cache cache, int port) {
		FileCacheService cacheService = new FileCacheService(cache);
		Path privateFilesFolder = Paths.get(Utils.getEnv(PRIVATE_FOLDER, DEFAULT_PRIVATE_FOLDER));
		Path publicFilesFolder = Paths.get(Utils.getEnv(PUBLIC_FOLDER, DEFAULT_PUBLIC_FOLDER));
		logger.info(LOGGER_SEPARATOR);
		logger.info(PRIVATE_FOLDER + ": " + privateFilesFolder);
		logger.info(PUBLIC_FOLDER + ": " + publicFilesFolder);
		logger.info(LOGGER_SEPARATOR);
		return new DefaultFileService(cacheService, privateFilesFolder, publicFilesFolder);
	}
	
	private static void startFileServer(Cache cache, int port) {
		FileService fileService = getFileService(cache, port);
		MicroFileServer.start(fileService, port);
	}

	public static void startFileServer(int port) {
		Cache cache = createCache().orElseThrow(IllegalStateException::new);
		startFileServer(cache, port);
	}

	private static void startJsonDBModelServer(Cache cache, int port) {
		GenericCacheService<Notebook> notebookCacheService = new GenericCacheService<>(cache, Elements.NOTEBOOKS);
		ModelService<Notebook> notebookService = new JsonDBNotebookService(notebookCacheService);

		GenericCacheService<Paragraph> paragraphCacheService = new GenericCacheService<>(cache, Elements.PARAGRAPHS);
		ModelService<Paragraph> paragraphService = new JsonDBParagraphService(paragraphCacheService);

		GenericCacheService<Filter> filterCacheService = new GenericCacheService<>(cache, Elements.FILTERS);
		ModelService<Filter> filterService = new JsonDBFilterService(filterCacheService);

		InterpreterPropertiesReader kernelPropertiesReader = new InterpreterPropertiesReader();
		List<Interpreter> kernels = kernelPropertiesReader.getKernels();

		// MicroModelServer.start(kernels, notebookService, paragraphService, filterService, port);
		FileService fileService = getFileService(cache, port);
		MicroModelServer.startExtendedMode(kernels, notebookService, paragraphService, filterService, fileService, port);
	}

	public static void startJsonDBModelServer(int port) {
		Cache cache = createCache().orElseThrow(IllegalStateException::new);
		startJsonDBModelServer(cache, port);
	}

	public static void startVueServer(int port) {
		MicroVueServer.start(port);
	}
	
	public static void startRServer(int port) {
		AnalyticsService analyticsService = new RService();
		MicroAnalyticsServer.start(analyticsService, port);
		// subscribeRMessageBroker();
	}

	public static void startWebSocketServer(int port) {
		WebSocketServer.start(port);
	}

	public static void subscribeRMessageBroker() {
		Optional<MessageBroker> broker = createBroker();
		if (broker.isPresent()) {
			Cache cache = createCache().orElseThrow(IllegalStateException::new);
			TaskService taskService = new RTaskService(cache);
			DefaultBrokerTask brokerTask = new DefaultBrokerTask(taskService);
			broker.get().subscribe(R_KERNEL_ID, brokerTask);
		}
	}
}

package de.polipol.analytics.web.controller;

import static de.polipol.analytics.commons.TestConstants.PRIVATE_FILES_FOLDER;
import static de.polipol.analytics.commons.TestConstants.PUBLIC_FILES_FOLDER;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.fppt.jedismock.RedisServer;

import de.polipol.analytics.cache.Cache;
import de.polipol.analytics.cache.FileCacheService;
import de.polipol.analytics.cache.RedisCache;
import de.polipol.analytics.file.DefaultFileService;
import de.polipol.analytics.file.FileService;
import io.javalin.http.Context;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MicroFileContorllerTest {

	private Context ctx = mock(Context.class);
	private RedisServer server;
	private FileCacheService cacheService;
	private FileService fileService;

	@Test
	public void GET_to_fetch_files_returns_list_of_files_within_directory() {
		new FileController(fileService).getAll(ctx);
		verify(ctx).status(200);
	}

	@Before
	public void init() throws IOException {
		this.server = new RedisServer();
		this.server.start();

		Cache cache = new RedisCache(server.getHost(), server.getBindPort());
		cache.flushAll();

		this.cacheService = new FileCacheService(cache);
		this.fileService = new DefaultFileService(cacheService, PRIVATE_FILES_FOLDER, PUBLIC_FILES_FOLDER);
	}
}

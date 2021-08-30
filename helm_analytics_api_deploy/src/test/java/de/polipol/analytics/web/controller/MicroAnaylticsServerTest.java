package de.polipol.analytics.web.controller;

import static de.polipol.analytics.commons.Constants.DIRECTORY;
import static de.polipol.analytics.commons.Constants.FILENAME;
import static de.polipol.analytics.commons.Constants.OUTPUT;
import static de.polipol.analytics.commons.Constants.TYPE_PLAIN;
import static de.polipol.analytics.commons.TestConstants.PRIVATE_FILES_FOLDER;
import static de.polipol.analytics.commons.TestConstants.PUBLIC_FILES_FOLDER;
import static de.polipol.analytics.commons.TestConstants.SAMPLE1;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import de.polipol.analytics.web.AnalyticsTestService;
import io.javalin.http.Context;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MicroAnaylticsServerTest {

	private Context ctx = mock(Context.class);
	private RedisServer server;
	private FileCacheService cacheService;

	@Test
	public void POST_evaluate() {
		when(ctx.queryParam(ExtendedAnalyticsController.PARAM_TYPE)).thenReturn(TYPE_PLAIN);
		when(ctx.queryParam(ExtendedAnalyticsController.PARAM_VARIABLE)).thenReturn(OUTPUT);
		when(ctx.queryParam(DIRECTORY)).thenReturn(EMPTY);
		when(ctx.queryParam(FILENAME)).thenReturn(SAMPLE1);
		when(ctx.body()).thenReturn(EMPTY);
//		new ExtendedAnalyticsController(analyticsService, fileService).evaluate(ctx);
//		verify(ctx).status(200);
//		verify(ctx).result(SAMPLE1_EXPRESSION);
	}

	@Test
	public void POST_evaluateWithEmpytFilename() {
		when(ctx.queryParam(ExtendedAnalyticsController.PARAM_TYPE)).thenReturn(TYPE_PLAIN);
		when(ctx.queryParam(ExtendedAnalyticsController.PARAM_VARIABLE)).thenReturn(OUTPUT);
		when(ctx.queryParam(DIRECTORY)).thenReturn(EMPTY);
		when(ctx.queryParam(FILENAME)).thenReturn(EMPTY);
		when(ctx.body()).thenReturn(EMPTY);
		// new ExtendedAnalyticsController(analyticsService, fileService).evaluate(ctx);
		// verify(ctx).status(200);
		// verify(ctx).result(SAMPLE1_EXPRESSION);
	}
	
	@Before
	public void init() throws IOException {
		this.server = new RedisServer();
		this.server.start();

		Cache cache = new RedisCache(server.getHost(), server.getBindPort());
		cache.flushAll();

		this.cacheService = new FileCacheService(cache);
		new DefaultFileService(cacheService, PRIVATE_FILES_FOLDER, PUBLIC_FILES_FOLDER);
		new AnalyticsTestService();
	}
}

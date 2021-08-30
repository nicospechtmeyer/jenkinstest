package de.polipol.analytics.file;

import static de.polipol.analytics.commons.TestConstants.SAMPLE_ROLE;
import static de.polipol.analytics.commons.TestConstants.PRIVATE_FILE1;
import static de.polipol.analytics.commons.TestConstants.PRIVATE_FILE2;
import static de.polipol.analytics.commons.TestConstants.PRIVATE_FILES_FOLDER;
import static de.polipol.analytics.commons.TestConstants.PUBLIC_FILE1;
import static de.polipol.analytics.commons.TestConstants.PUBLIC_FILE2;
import static de.polipol.analytics.commons.TestConstants.PUBLIC_FILES_FOLDER;
import static de.polipol.analytics.commons.TestConstants.SAMPLE_DIRECTORY;
import static de.polipol.analytics.commons.TestConstants.TEST_FILE_JSON;
import static de.polipol.analytics.commons.TestConstants.TEST_JSON;
import static de.polipol.analytics.file.FileService.getId;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.fppt.jedismock.RedisServer;

import de.polipol.analytics.cache.Cache;
import de.polipol.analytics.cache.FileCacheService;
import de.polipol.analytics.cache.RedisCache;
import de.polipol.analytics.model.DefaultFile;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class FileServiceTest {

	private RedisServer server;
	private FileCacheService cacheService;
	private FileService fileService;

	@After
	public void after() {
		this.server.stop();
		this.server = null;
	}

	private void checkFiles(String directory, String role, String filename) throws IOException {
		assertThat(this.cacheService.find(getId(directory, role, filename), DefaultFile.class).isEmpty(),
				equalTo(true));
		assertThat(this.fileService.find(directory, role, filename, true).isPresent(), equalTo(true));
		assertThat(this.cacheService.find(getId(directory, role, filename), DefaultFile.class).isPresent(),
				equalTo(true));
	}

	@Test
	public void findPrivateFiles() throws IOException {
		this.checkFiles(EMPTY, SAMPLE_ROLE, PRIVATE_FILE1);
		this.checkFiles(SAMPLE_DIRECTORY, SAMPLE_ROLE, PRIVATE_FILE2);
	}

	@Test
	public void findPublicFiles() throws IOException {
		this.checkFiles(EMPTY, EMPTY, PUBLIC_FILE1);
		this.checkFiles(SAMPLE_DIRECTORY, EMPTY, PUBLIC_FILE2);
	}

	@Test
	public void getAllPrivateFiles() throws IOException {
		assertThat(fileService.getAll(EMPTY, SAMPLE_ROLE).size(), equalTo(2));
	}

	@Test
	public void getAllPrivateFilesWithinDirectory() throws IOException {
		assertThat(fileService.getAll(SAMPLE_DIRECTORY, SAMPLE_ROLE).size(), equalTo(1));
	}

	@Test
	public void getAllPublicFiles() throws IOException {
		assertThat(fileService.getAll(EMPTY, EMPTY).size(), greaterThan(2));
	}

	@Test
	public void getAllPublicFilesWithinDirectory() throws IOException {
		assertThat(fileService.getAll(SAMPLE_DIRECTORY, EMPTY).size(), equalTo(1));
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

	@Test
	public void upsertAndDeleteFile() throws IOException {
		assertThat(this.cacheService.find(getId(SAMPLE_DIRECTORY, SAMPLE_ROLE, TEST_FILE_JSON), DefaultFile.class).isEmpty(),
				equalTo(true));

		InputStream inputStream = IOUtils.toInputStream(TEST_JSON, StandardCharsets.UTF_8);

		fileService.upsert(SAMPLE_DIRECTORY, SAMPLE_ROLE, TEST_FILE_JSON, inputStream);
		assertThat(this.fileService.find(SAMPLE_DIRECTORY, SAMPLE_ROLE, TEST_FILE_JSON, true).isPresent(), equalTo(true));
		assertThat(this.cacheService.find(getId(SAMPLE_DIRECTORY, SAMPLE_ROLE, TEST_FILE_JSON), DefaultFile.class).isPresent(),
				equalTo(true));

		fileService.delete(SAMPLE_DIRECTORY, SAMPLE_ROLE, TEST_FILE_JSON);
		assertThat(this.fileService.find(SAMPLE_DIRECTORY, SAMPLE_ROLE, TEST_FILE_JSON, true).isEmpty(), equalTo(true));
		assertThat(this.cacheService.find(getId(SAMPLE_DIRECTORY, SAMPLE_ROLE, TEST_FILE_JSON), DefaultFile.class).isEmpty(),
				equalTo(true));
	}
}

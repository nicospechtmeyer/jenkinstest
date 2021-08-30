package de.polipol.analytics.cache;

import static de.polipol.analytics.commons.TestConstants.TEST_KEY;
import static de.polipol.analytics.commons.TestConstants.TEST_PATH_PNG;
import static de.polipol.analytics.commons.TestConstants.TMP_NEW_PATH_PNG;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.fppt.jedismock.RedisServer;

import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.model.DefaultFile;
import de.polipol.analytics.model.File;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CacheServiceTest {

	private RedisServer server;
	private FileCacheService cacheService;

	@Before
	public void init() throws IOException {
		this.server = new RedisServer();
		this.server.start();

		Cache cache = new RedisCache(server.getHost(), server.getBindPort());
		cache.flushAll();

		this.cacheService = new FileCacheService(cache);
	}

	@Test
	public void upsertAndDeleteFile() throws IOException {
		Path path = Paths.get(TEST_PATH_PNG);
		Path newPath = Paths.get(TMP_NEW_PATH_PNG);
		File defaultFile = new DefaultFile();
		defaultFile.setContent(Utils.getByteArray(path));
		this.cacheService.upsert(TEST_KEY, defaultFile);
		Optional<File> result = this.cacheService.find(TEST_KEY, DefaultFile.class);
		if (result.isPresent()) {
			FileUtils.writeByteArrayToFile(newPath.toFile(), result.get().getContent());
		}
		assertThat(FileUtils.contentEquals(path.toFile(), newPath.toFile()), equalTo(true));
		newPath.toFile().delete();
	}

	@After
	public void after() {
		this.server.stop();
	}
}
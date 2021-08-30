package de.polipol.analytics.commons;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Commonly used constants.
 *
 * @author Joern Sprado
 */
public abstract class TestConstants {
	
	public static final String TEST_KERNEL_ID = "test";
	
	// Dictionary
	public static final String COLUMN1 = "column1";
	public static final String COLUMN2 = "column2";
	public static final String CONTAINER1 = "container1";
	public static final Path TEST_RESOURCES_FOLDER = Paths.get("./src/test/resources");
	public static final String FORMAT_PATTERN = "test()";
	
	// Files
	public static final Path PRIVATE_FILES_FOLDER = Paths.get(TEST_RESOURCES_FOLDER + "/assets/private");
	public static final Path PUBLIC_FILES_FOLDER = Paths.get(TEST_RESOURCES_FOLDER + "/assets/public");
	public static final String SAMPLE_DIRECTORY = "sample";
	public static final String SAMPLE_ROLE = "user";
	public static final String PRIVATE_FILE1 = "private1.json";
	public static final String PRIVATE_FILE2 = "private2.json";
	public static final String PUBLIC_FILE1 = "public1.json";
	public static final String PUBLIC_FILE2 = "public2.json";
	public static final String TEST_FILE_JSON = "test.json";
	public static final String TEST_PATH_PNG = TEST_RESOURCES_FOLDER + "/test.png";
	public static final String TMP_NEW_PATH_PNG = TEST_RESOURCES_FOLDER + "/new_test.png";
	public static final String TEST_JSON = "{ \"string\": \"Hello World\" }";
	
	// JsonDB
	public static final String TITLE = "testId-123456789";
	public static final String CREATED_AT = "today";
	public static final String CREATOR = "unknown";
	public static final String[] GROUPS = new String[] { "group1", "group2", "group3" };
	public static final String TITLE_VALUE = "testId-123456789";
	public static final String VIEW_TYPES = "viewTypes";
	public static final String[] VIEW_TYPES_VALUE = new String[] { "text", "table" };
	
	// R
	public static final String SAMPLE1 = "sample1.R";
	public static final String SAMPLE1_EXPRESSION = "output <- 'test'";
	public static final String TEST_KEY = "test";
}

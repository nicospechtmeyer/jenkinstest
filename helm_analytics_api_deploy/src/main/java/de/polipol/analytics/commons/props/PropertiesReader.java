package de.polipol.analytics.commons.props;

import static de.polipol.analytics.commons.Messages.MESSAGE_READING_PROPERTIES_FILE_FAILED;
import static java.io.File.separator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.jasypt.properties.EncryptableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PropertiesReader {

	public static final String USER_DIR = "user.dir";

	private final Logger logger = LoggerFactory.getLogger(PropertiesReader.class);
	protected Path file;
	protected Properties properties;

	protected void evaluateProperties() {
	}

	protected Properties getProperties() {
		return this.properties;
	}

	protected Path getValidDirectory(final String directory) {
		Path directoryPath;
		directoryPath = Paths.get(directory);
		if (directoryPath.isAbsolute()) {
			return directoryPath;
		} else {
			return Paths.get(System.getProperty(USER_DIR) + separator + directory);
		}
	}

	protected void init() {
		try {
			loadProperties();
			evaluateProperties();
		} catch (IOException exception) {
			if (exception instanceof FileNotFoundException) {
				logger.error(this.file + MESSAGE_READING_PROPERTIES_FILE_FAILED);
			} else {
				logger.error(MESSAGE_READING_PROPERTIES_FILE_FAILED);
			}
		}
	}

	protected void loadProperties() throws IOException {
		this.properties = new EncryptableProperties(PropertiesEncryptorFactory.getEncryptor());
		Reader reader = new FileReader(file.toFile());
		this.properties.load(reader);
	}
}
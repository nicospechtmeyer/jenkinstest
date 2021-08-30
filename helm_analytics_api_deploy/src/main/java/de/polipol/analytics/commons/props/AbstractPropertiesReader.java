package de.polipol.analytics.commons.props;

import java.io.File;
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

public abstract class AbstractPropertiesReader {

	private final Logger logger = LoggerFactory.getLogger(AbstractPropertiesReader.class);
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
			return Paths.get(System.getProperty("user.dir") + File.separator + directory);
		}
	}

	protected void init() {
		try {
			loadProperties();
			evaluateProperties();
		} catch (IOException exception) {
			if (exception instanceof FileNotFoundException) {
				logger.info(this.file + " could not be found.");
			} else {
				logger.info("Error while reading properties file");
			}
		}
	}

	protected void loadProperties() throws IOException {
		this.properties = new EncryptableProperties(PropertiesEncryptorFactory.getEncryptor());
		Reader reader = new FileReader(file.toFile());
		this.properties.load(reader);
	}
}
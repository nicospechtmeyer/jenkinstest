package de.polipol.analytics.file;

import static de.polipol.analytics.file.FileService.getId;
import static java.io.File.separator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import de.polipol.analytics.cache.CacheService;
import de.polipol.analytics.cache.FileCacheService;
import de.polipol.analytics.model.DefaultFile;
import de.polipol.analytics.model.File;

public final class DefaultFileService implements FileService {

//	private static final Logger logger = LoggerFactory.getLogger(DefaultFileService.class);

	protected CacheService<File> cacheService;
	protected Path privateFolder;
	protected Path publicFolder;

	public DefaultFileService(FileCacheService cacheService, final Path privateFolder, final Path publicFolder) {
		this.cacheService = cacheService;
		this.privateFolder = privateFolder;
		this.publicFolder = publicFolder;
	}

	@Override
	public void delete(String directory, String role, String filename) throws IOException {
		this.cacheService.delete(getId(directory, role, filename));
		Optional<Path> path = this.findOnFilesystem(directory, role, filename);
		if (path.isPresent()) {
			Files.delete(path.get());
		}
	}

	@Override
	public Optional<File> find(String directory, String role, String filename, boolean useCache) {
		String id = getId(directory, role, filename);
		Optional<File> element = Optional.empty();
		if (useCache) {
			element = this.cacheService.find(id, DefaultFile.class);
		}
		if (element.isEmpty()) {
			Optional<Path> path = this.findOnFilesystem(directory, role, filename);
			if (path.isPresent()) {
				element = Optional.of(new DefaultFile(id, path.get().toFile(), true));
				if (element.isPresent()) {
					cacheService.upsert(id, element.get());
				}
			}
		}
		return element;
	}

	protected Optional<Path> findOnFilesystem(String directory, String role, String filename) {
		Optional<Path> directoryPath = this.getDirectory(directory, role);
		if (directoryPath.isPresent()) {
			String extension = FilenameUtils.getExtension(filename);
			if (!Arrays.asList(FileExtension.toArray()).contains(extension.toUpperCase())) {
				throw new UnsupportedOperationException();
			}
			String[] extensions = { extension };
			java.io.File directoryFile = directoryPath.get().toFile();
			if (directoryFile.isDirectory()) {
				for (java.io.File file : FileUtils.listFiles(directoryFile, extensions, false)) {
					if (StringUtils.startsWithIgnoreCase(file.getName(), filename)) {
						return Optional.of(file.toPath());
					}
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public Collection<File> getAll(String directory, String role) throws IOException {
		List<File> files = new ArrayList<>();
		Optional<Path> directoryPath = this.getDirectory(directory, role);
		if (directoryPath.isPresent()) {
			java.io.File directoryFile = directoryPath.get().toFile();
			if (directoryFile.isDirectory()) {
				Stream<Path> walk = Files.walk(directoryPath.get());
				for (Path path : walk
						.filter(file -> !file.toFile().getName().startsWith(".") && Files.isRegularFile(file))
						.collect(Collectors.toList())) {
					files.add(new DefaultFile(getId(directory, role, path.toFile().getName()), path.toFile(), false));
				}
				walk.close();
			}
		}
		return files;
	}

	private Optional<Path> getDirectory(final String directory, final String role) {
		Path path = null;
		if (StringUtils.isEmpty(role)) {
			if (StringUtils.isNotEmpty(directory)) {
				path = Paths.get(this.publicFolder + separator + directory);
			} else {
				path = Paths.get(this.publicFolder.toString());
			}
		} else {
			if (StringUtils.isNotEmpty(directory)) {
				path = Paths.get(this.privateFolder + separator + role + separator + directory);
			} else {
				path = Paths.get(this.privateFolder + separator + role);
			}
		}
		return Optional.of(path);
	}

	@Override
	public void upsert(String directory, String role, String filename, InputStream inputStream) throws IOException {
		Optional<Path> directoryPath = this.getDirectory(directory, role);
		if (directoryPath.isPresent()) {
			java.io.File directoryFile = directoryPath.get().toFile();
			if (directoryFile.exists()) {
				directoryFile.mkdir();
			}
		}
		java.io.File file = Paths.get(directoryPath.get() + separator + filename).toFile();
		FileUtils.copyInputStreamToFile(inputStream, file);
		String id = getId(directory, role, file.getName());
		this.cacheService.upsert(id, new DefaultFile(id, file, true));
	}
}

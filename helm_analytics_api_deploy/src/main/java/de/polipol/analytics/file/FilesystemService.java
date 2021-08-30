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

import de.polipol.analytics.model.DefaultFile;
import de.polipol.analytics.model.File;

public final class FilesystemService implements FileService {

	protected Path privateFolder;
	protected Path publicFolder;

	public FilesystemService(final Path privateFolder, final Path publicFolder) {
		this.privateFolder = privateFolder;
		this.publicFolder = publicFolder;
	}

	@Override
	public void delete(String directory, String role, String filename) throws IOException {
		Optional<Path> path = this.findOnFilesystem(directory, role, filename);
		if (path.isPresent()) {
			Files.delete(path.get());
		}
	}

	@Override
	public Optional<File> find(String directory, String role, String filename, boolean useCache) {
		String id = getId(directory, role, filename);
		Optional<File> element = Optional.empty();
		if (element.isEmpty()) {
			Optional<Path> path = this.findOnFilesystem(directory, role, filename);
			if (path.isPresent()) {
				element = Optional.of(new DefaultFile(id, path.get().toFile(), true));
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
	}
}

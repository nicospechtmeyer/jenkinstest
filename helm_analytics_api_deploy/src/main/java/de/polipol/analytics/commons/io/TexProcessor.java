package de.polipol.analytics.commons.io;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.polipol.analytics.file.FileExtension;

public final class TexProcessor implements DocumentProcessor {

	private static final String DOT = ".";
	private static final String INTERACTION_NONSTOPMODE = "-interaction=nonstopmode";
	private static final String OUTPUT_DIRECTORY_ARGUMENT = "-output-directory=";
	private static final String PROG = "pdflatex";

	private String directory;

	public TexProcessor(final String directory) {
		super();
		this.directory = directory;
	}

	private void cleanUp(final String filename) throws IOException {
//		Files.deleteIfExists(this.getFile(filename, FileExtension.OUT));
//		Files.deleteIfExists(this.getFile(filename, FileExtension.LOG));
//		Files.deleteIfExists(this.getFile(filename, FileExtension.AUX));
//		Files.deleteIfExists(this.getFile(filename, FileExtension.RNW));
//		Files.deleteIfExists(this.getFile(filename, FileExtension.TEX));
	}

	private Path getFile(final String filename, final FileExtension suffix) {
		final StringBuilder builder = new StringBuilder();
		builder.append(directory);
		builder.append(File.separator);
		builder.append(filename);
		builder.append(DOT);
		builder.append(suffix.toString().toLowerCase());
		return Paths.get(builder.toString());
	}

	private String process(final String filename) throws IOException {
		final StringBuilder builder = new StringBuilder();
		final ProcessBuilder texBuilder = new ProcessBuilder(PROG, OUTPUT_DIRECTORY_ARGUMENT + directory,
				INTERACTION_NONSTOPMODE, this.getFile(filename, FileExtension.TEX).toString());
		texBuilder.directory(new File(directory));
		Process process;
		BufferedReader reader = null;
		texBuilder.redirectErrorStream(true);
		try {
			for (int index = 0; index < 2; index++) {
				process = texBuilder.start();
				reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String tmp;
				while ((tmp = reader.readLine()) != null) {
					builder.append(tmp);
				}
			}
		} finally {
			reader.close();
		}
		return builder.toString();
	}

	@Override
	public String start(final String filename) throws IOException {
		String logs = EMPTY;
		logs = process(filename);
		cleanUp(filename);
		if (Files.notExists(this.getFile(filename, FileExtension.PDF))) {
			throw new FileNotFoundException();
		}
		return logs;
	}
}

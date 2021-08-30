package de.polipol.analytics.connect.r;

import static de.polipol.analytics.connect.r.RKeywords.DEV;
import static de.polipol.analytics.connect.r.RKeywords.HEIGHT;
import static de.polipol.analytics.connect.r.RKeywords.NULL;
import static de.polipol.analytics.connect.r.RKeywords.OFF;
import static de.polipol.analytics.connect.r.RKeywords.OUTPUT;
import static de.polipol.analytics.connect.r.RKeywords.RENDER;
import static de.polipol.analytics.connect.r.RKeywords.RESOLUTION;
import static de.polipol.analytics.connect.r.RKeywords.UNDEFINED_RESULT;
import static de.polipol.analytics.connect.r.RKeywords.WIDTH;
import static de.polipol.analytics.connect.r.RSymbols.ASSIGNMENT_OPERATOR;
import static de.polipol.analytics.connect.r.RSymbols.COMMA;
import static de.polipol.analytics.connect.r.RSymbols.DOT;
import static de.polipol.analytics.connect.r.RSymbols.EQUAL;
import static de.polipol.analytics.connect.r.RSymbols.LEFT_BRACKET;
import static de.polipol.analytics.connect.r.RSymbols.LINEBREAK;
import static de.polipol.analytics.connect.r.RSymbols.QUOTE;
import static de.polipol.analytics.connect.r.RSymbols.RIGHT_BRACKET;
import static de.polipol.analytics.connect.r.RSymbols.SPACE;
import static de.polipol.analytics.file.FileExtension.BMP;
import static de.polipol.analytics.file.FileExtension.JPEG;
import static de.polipol.analytics.file.FileExtension.JPG;
import static de.polipol.analytics.file.FileExtension.PDF;
import static de.polipol.analytics.file.FileExtension.PNG;
import static de.polipol.analytics.file.FileExtension.SVG;
import static de.polipol.analytics.file.FileExtension.TIFF;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.commons.io.DocumentProcessor;
import de.polipol.analytics.commons.io.TexProcessor;
import de.polipol.analytics.connect.AbstractHandler;
import de.polipol.analytics.exception.AnalyticsException;
import de.polipol.analytics.exception.EmptyExpressionException;
import de.polipol.analytics.exception.EmptyResultException;
import de.polipol.analytics.file.FileExtension;

public final class DefaultRHandler extends AbstractHandler implements RHandler {

	private static final String CANNOT_WRITE_TEMPORARY_FILE = "Cannot write temporary file";
	private final Path CONTAINER_TMP_DIRECTORY = Paths.get(System.getProperty("user.dir") + File.separator + "tmp");
	private final Path LOCAL_TMP_DIRECTORY = Paths.get(System.getProperty("user.dir") + File.separator + "tmp");
	private RAdapter adapter;

	public DefaultRHandler(final RAdapter adapter) throws AnalyticsException, IOException {
		super();
		this.adapter = adapter;
		this.adapter.clearWorkspace();
	}

	protected String buildExpression(final Map<String, String> parameter) throws AnalyticsException {
		final StringBuilder builder = new StringBuilder();
		if (!MapUtils.isEmpty(parameter)) {
			for (Entry<String, String> entry : parameter.entrySet()) {
				builder.append(entry.getKey());
				builder.append(SPACE);
				builder.append(ASSIGNMENT_OPERATOR);
				builder.append(SPACE);
				builder.append(QUOTE);
				builder.append(entry.getValue());
				builder.append(QUOTE);
				builder.append(LINEBREAK);
			}
		}
		return builder.toString();
	}

	protected Path createFileByString(Path path, String expression) throws IOException {
		File file = Files.createFile(path).toFile();
//		file.deleteOnExit();
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(expression);
		out.close();
		return file.toPath();
	}

	protected List<String> evaluate(final String expression) throws AnalyticsException {
		return Arrays.asList(adapter.eval(expression));
	}

	@Override
	public RAdapter getAdapter() {
		return adapter;
	}

	@Override
	public byte[] getDocument(final String expression, final Map<String, String> parameters, FileExtension sourceFormat,
			FileExtension targetFormat) throws IOException, AnalyticsException {

		if (StringUtils.isEmpty(expression))
			throw new EmptyExpressionException();
		initialize();

		final String uniqueIdentifierString = Utils.getUniqueIdentifierString();
		final Path targetDirectoryPath = Paths.get(LOCAL_TMP_DIRECTORY + File.separator + uniqueIdentifierString);
		final Path targetRDirectoryPath = Paths.get(CONTAINER_TMP_DIRECTORY + File.separator + uniqueIdentifierString);
		final String newFileName = uniqueIdentifierString;
		final String sourceSuffix = sourceFormat.toString().toLowerCase();
		final String targetSuffix = targetFormat.toString().toLowerCase();
		final Path targetFile = Paths.get(targetDirectoryPath + File.separator + newFileName + DOT + sourceSuffix);
		final Path documentPath = Paths.get(targetDirectoryPath + File.separator + newFileName + DOT + targetSuffix);

		Files.createDirectory(targetDirectoryPath);
		createFileByString(targetFile, expression);
		adapter.setWorkingDirectory(targetRDirectoryPath);

		StringBuilder builder = new StringBuilder();
		builder.append(buildExpression(parameters));
		if (targetFormat == FileExtension.DOCX) {
			if (sourceFormat == FileExtension.RMD) {
				builder.append(RKeywords.KNIT);
				builder.append(LEFT_BRACKET);
				builder.append(QUOTE);
				builder.append(newFileName);
				builder.append(DOT);
				builder.append(sourceSuffix);
				builder.append(QUOTE);
				builder.append(RIGHT_BRACKET);
				evaluate(builder.toString());

				builder = new StringBuilder();
				builder.append("pandoc");
				builder.append(LEFT_BRACKET);
				builder.append(QUOTE);
				builder.append(newFileName);
				builder.append(DOT);
				builder.append(FileExtension.MD.toString().toLowerCase());
				builder.append(QUOTE);
				builder.append(COMMA);
				builder.append(SPACE);
				builder.append("format=");
				builder.append(QUOTE);
				builder.append((FileExtension.DOCX.toString().toLowerCase()));
				builder.append(QUOTE);
				builder.append(RIGHT_BRACKET);
				evaluate(builder.toString());
			} else {
				throw new UnsupportedOperationException();
			}
		} else if (targetFormat == FileExtension.PDF) {
			if (sourceFormat == FileExtension.RNW) {
				builder.append(RKeywords.KNIT);
				builder.append(LEFT_BRACKET);
				builder.append(QUOTE);
				builder.append(newFileName);
				builder.append(DOT);
				builder.append(sourceSuffix);
				builder.append(QUOTE);
				builder.append(COMMA);
				builder.append(SPACE);
				builder.append(RKeywords.OUTPUT);
				builder.append(EQUAL);
				builder.append(QUOTE);
				builder.append(newFileName);
				builder.append(DOT);
				builder.append(FileExtension.TEX.toString().toLowerCase());
				builder.append(QUOTE);
				builder.append(RIGHT_BRACKET);
				evaluate(builder.toString());

				DocumentProcessor processor = new TexProcessor(
						FilenameUtils.separatorsToUnix(targetDirectoryPath.toString()));
				processor.start(newFileName);
			} else if (sourceFormat == FileExtension.RMD) {
				builder.append(RKeywords.KNIT);
				builder.append(LEFT_BRACKET);
				builder.append(QUOTE);
				builder.append(newFileName);
				builder.append(DOT);
				builder.append(sourceSuffix);
				builder.append(QUOTE);
				builder.append(RIGHT_BRACKET);
				evaluate(builder.toString());

				builder = new StringBuilder();
				builder.append("pandoc");
				builder.append(LEFT_BRACKET);
				builder.append(QUOTE);
				builder.append(newFileName);
				builder.append(DOT);
				builder.append(FileExtension.MD.toString().toLowerCase());
				builder.append(QUOTE);
				builder.append(COMMA);
				builder.append(SPACE);
				builder.append("format=");
				builder.append(QUOTE);
				builder.append("latex");
				builder.append(QUOTE);
				builder.append(RIGHT_BRACKET);
				evaluate(builder.toString());
			} else if (sourceFormat == FileExtension.MD) {
				builder = new StringBuilder();
				builder.append("pandoc");
				builder.append(LEFT_BRACKET);
				builder.append(QUOTE);
				builder.append(newFileName);
				builder.append(DOT);
				builder.append(FileExtension.MD.toString().toLowerCase());
				builder.append(QUOTE);
				builder.append(COMMA);
				builder.append(SPACE);
				builder.append("format=");
				builder.append(QUOTE);
				builder.append("latex");
				builder.append(QUOTE);
				builder.append(RIGHT_BRACKET);
				evaluate(builder.toString());
			} else {
				throw new UnsupportedOperationException();
			}
		} else if (targetFormat == FileExtension.HTML) {
			if (sourceFormat == FileExtension.RHTML) {
				builder.append(RKeywords.KNIT);
				builder.append(LEFT_BRACKET);
				builder.append(QUOTE);
				builder.append(newFileName);
				builder.append(DOT);
				builder.append(sourceSuffix);
				builder.append(QUOTE);
				builder.append(COMMA);
				builder.append(SPACE);
				builder.append(RKeywords.OUTPUT);
				builder.append(EQUAL);
				builder.append(QUOTE);
				builder.append(newFileName);
				builder.append(DOT);
				builder.append((FileExtension.HTML.toString().toLowerCase()));
				builder.append(QUOTE);
				builder.append(RIGHT_BRACKET);
				evaluate(builder.toString());
			} else if (sourceFormat == FileExtension.RMD) {
				builder.append(RENDER);
				builder.append(LEFT_BRACKET);
				builder.append(QUOTE);
				builder.append(newFileName);
				builder.append(DOT);
				builder.append(sourceSuffix);
				builder.append(QUOTE);
				builder.append(RIGHT_BRACKET);
				evaluate(builder.toString());
			} else if (sourceFormat == FileExtension.MD) {
				builder.append(RENDER);
				builder.append(LEFT_BRACKET);
				builder.append(QUOTE);
				builder.append(newFileName);
				builder.append(DOT);
				builder.append(sourceSuffix);
				builder.append(QUOTE);
				builder.append(RIGHT_BRACKET);
				evaluate(builder.toString());
			} else {
				throw new UnsupportedOperationException();
			}
//		} else if (targetFormat == FileExtension.MD) {
//			if (sourceFormat == FileExtension.RMD) {
//				builder.append(RENDER);
//				builder.append(LEFT_BRACKET);
//				builder.append(QUOTE);
//				builder.append(newFileName);
//				builder.append(DOT);
//				builder.append(sourceSuffix);
//				builder.append(QUOTE);
//				builder.append(RIGHT_BRACKET);
//				evaluate(builder.toString());
//			}
		} else {
			throw new UnsupportedOperationException();
		}
		adapter.setWorkingDirectory(CONTAINER_TMP_DIRECTORY);
		byte[] bytes = new byte[0];
		if (Utils.isDevMode()) {
			bytes = Utils.getByteArray(documentPath);
		} else {
			bytes = Utils.getByteArrayAndDeleteFolder(documentPath);
		}
		return bytes;
	}

	@Override
	public byte[] getImage(final String expression, final Map<String, String> parameters,
			final FileExtension targetFormat, final int width, final int height, final int resolution)
			throws AnalyticsException, IOException, UnsupportedOperationException {

		String functionName;
		if (targetFormat == PDF || targetFormat != JPEG || targetFormat != JPG || targetFormat != PNG
				|| targetFormat != TIFF || targetFormat != BMP || targetFormat != SVG) {
			if (targetFormat == JPG) {
				functionName = FileExtension.JPEG.toString().toLowerCase();
			} else {
				functionName = targetFormat.toString().toLowerCase();
			}
		} else {
			throw new UnsupportedOperationException();
		}

		if (StringUtils.isEmpty(expression))
			throw new EmptyExpressionException();
		initialize();

		final String uniqueIdentifierString = Utils.getUniqueIdentifierString();
		final Path targetDirectoryPath = Paths.get(LOCAL_TMP_DIRECTORY + File.separator + uniqueIdentifierString);
		final Path targetRDirectoryPath = Paths.get(CONTAINER_TMP_DIRECTORY + File.separator + uniqueIdentifierString);
		final String newFileName = uniqueIdentifierString;
		final String targetSuffix = targetFormat.toString().toLowerCase();
		final Path documentPath = Paths.get(LOCAL_TMP_DIRECTORY + File.separator + uniqueIdentifierString
				+ File.separator + newFileName + DOT + targetSuffix);

		Files.createDirectory(targetDirectoryPath);
		adapter.setWorkingDirectory(targetRDirectoryPath);

		final StringBuilder builder = new StringBuilder();
		builder.append(buildExpression(parameters));
		builder.append(functionName);
		builder.append(LEFT_BRACKET);
		builder.append(QUOTE);
		builder.append(newFileName);
		builder.append(DOT);
		builder.append(targetFormat.toString().toLowerCase());
		builder.append(QUOTE);
		if (width > 0 && height > 0) {
			builder.append(COMMA);
			builder.append(SPACE);
			builder.append(WIDTH);
			builder.append(EQUAL);
			builder.append(width);
			builder.append(COMMA);
			builder.append(SPACE);
			builder.append(HEIGHT);
			builder.append(EQUAL);
			builder.append(height);
			if (targetFormat != FileExtension.PDF) {
				builder.append(COMMA);
				builder.append(SPACE);
				builder.append(RESOLUTION);
				builder.append(EQUAL);
				builder.append(resolution);
			}
		}
		builder.append(RIGHT_BRACKET);
		builder.append(LINEBREAK);
		builder.append(expression);
		builder.append(LINEBREAK);
		builder.append(LINEBREAK);
		builder.append(DEV);
		builder.append(DOT);
		builder.append(OFF);
		builder.append(LEFT_BRACKET);
		builder.append(RIGHT_BRACKET);
		evaluate(builder.toString());
		adapter.setWorkingDirectory(CONTAINER_TMP_DIRECTORY);
		byte[] bytes = new byte[0];
		if (Utils.isDevMode()) {
			bytes = Utils.getByteArray(documentPath);
		} else {
			bytes = Utils.getByteArrayAndDeleteFolder(documentPath);
		}
		return bytes;
	}

	protected String getJson(final String variable, final boolean pretty) throws AnalyticsException {
		final StringBuilder builder = new StringBuilder();
		builder.append(RKeywords.TO_JSON);
		builder.append(LEFT_BRACKET);
		builder.append(variable);
		if (pretty) {
			builder.append(COMMA);
			builder.append(SPACE);
			builder.append(RKeywords.PRETTY);
			builder.append(EQUAL);
			builder.append(RKeywords.TRUE);
		}
		builder.append(RIGHT_BRACKET);
		return builder.toString();
	}

	@Override
	public byte[] getText(final String expression, final String variable, final Map<String, String> preParameters,
			final Map<String, String> postParameters, final FileExtension targetFormat)
			throws AnalyticsException, IOException {

		initialize();
		final StringBuilder builder = new StringBuilder();
		builder.append(OUTPUT + SPACE + ASSIGNMENT_OPERATOR + SPACE + NULL + SPACE + LINEBREAK);
		builder.append(buildExpression(preParameters));
		builder.append(expression);
		builder.append(buildExpression(postParameters));
		load(builder.toString());
		List<String> result = new ArrayList<String>();
		if (StringUtils.isNotEmpty(variable)) {
			switch (targetFormat) {
			case TXT:
				result = evaluate(variable);
				break;
			case PRETTYJSON:
				result = evaluate(getJson(variable, true));
				break;
			case JSON:
				result = evaluate(getJson(variable, false));
				break;
			default:
				break;
			}
		}
		final StringBuilder resultBuilder = new StringBuilder();
		for (String row : result) {
			resultBuilder.append(row);
		}
		if (StringUtils.isEmpty(resultBuilder.toString()))
			throw new EmptyResultException(UNDEFINED_RESULT);

		return resultBuilder.toString().getBytes();
	}

	@Override
	public void initialize() throws AnalyticsException, IOException {
		adapter.clearWorkspace();
		adapter.loadPackage(RLibraries.JSONLITE);
		adapter.loadPackage(RLibraries.USETHIS);
		adapter.loadPackage(RLibraries.DEVTOOLS);
		adapter.loadPackage(RLibraries.DPLYR);
		adapter.loadPackage(RLibraries.LUBRIDATE);
		adapter.loadPackage(RLibraries.STRINGR);
		adapter.loadPackage(RLibraries.HTTR);
		adapter.loadPackage(RLibraries.KNITR);
		adapter.loadPackage(RLibraries.CURL);
		adapter.loadPackage(RLibraries.RMARKDOWN);
//		adapter.loadFile(INIT_R_SCRIPTFILE);
	}

	protected void load(final String expression) throws AnalyticsException {
		final StringBuilder builder = new StringBuilder();
		builder.append(expression);
		builder.append(LINEBREAK);
		final String fileFilename = Utils.getUniqueIdentifierString() + DOT + FileExtension.R;
		final Path filePath = Paths.get(LOCAL_TMP_DIRECTORY + File.separator + fileFilename);
		try {
			FileUtils.writeStringToFile(filePath.toFile(), builder.toString(), StandardCharsets.UTF_8);
			adapter.setWorkingDirectory(CONTAINER_TMP_DIRECTORY);
			adapter.loadFile(filePath);
			FileUtils.deleteQuietly(filePath.toFile());
		} catch (IOException exception) {
			FileUtils.deleteQuietly(filePath.toFile());
			throw new AnalyticsException(CANNOT_WRITE_TEMPORARY_FILE);
		}
	}
}
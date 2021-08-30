package de.polipol.analytics.commons;

import static de.polipol.analytics.commons.Constants.ID;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import de.polipol.analytics.file.FileExtension;
import de.polipol.analytics.web.ErrorResponse;
import io.javalin.http.Context;

public final class Utils {

	private static final String FILENAME_DELIMITER = "_";

	private static final String FOLDER_DATEFORMAT = "yyyyMMdd_HHmmssSSS";

	public static byte[] getBase64(final Path file) throws IOException {
		return Base64.encodeBase64(getByteArray(file));
	}

	public static byte[] getBase64AndDeleteFolder(final Path file) throws IOException {
		byte[] data = getBase64(file);
		FileUtils.deleteQuietly(file.getParent().toFile());
		return data;
	}

	public static String getBase64String(final Path file) throws IOException {
		return Base64.encodeBase64String(getByteArray(file));
	}

	public static String getBase64String(final String string) throws IOException {
		return Base64.encodeBase64String(string.getBytes());
	}

	public static String getBase64StringAndDeleteFolder(final Path file) throws IOException {
		String data = Base64.encodeBase64String(getByteArray(file));
		FileUtils.deleteQuietly(file.getParent().toFile());
		return data;
	}

	public static byte[] getByteArray(final Path file) throws IOException {
		byte[] data = FileUtils.readFileToByteArray(file.toFile());
		return data;
	}

	public static byte[] getByteArrayAndDeleteFile(final Path file) throws IOException {
		byte[] data = getByteArray(file);
		file.toFile().deleteOnExit();
		return data;
	}

	public static byte[] getByteArrayAndDeleteFolder(final Path file) throws IOException {
		byte[] data = getByteArray(file);
		FileUtils.deleteQuietly(file.getParent().toFile());
		return data;
	}

	public static String getEnv(String name, String defaultValue) {
		return Optional.ofNullable(System.getenv(name)).orElse(defaultValue);
	}

	public static List<String> getMembers(final String json) {
		List<String> members = new ArrayList<>();
		Gson gson = new Gson();
		try {
			JsonObject[] objects = gson.fromJson(json, JsonObject[].class);
			if (objects.length > 0) {
				Set<Entry<String, JsonElement>> entries = objects[0].entrySet();
				for (Map.Entry<String, JsonElement> entry : entries) {
					members.add(entry.getKey());
				}
			}
		} catch (Exception exception) {
			members = new ArrayList<>();
		}
		return members;
	}

	public static String getString(final Path file) throws IOException {
		return new String(Files.readAllBytes(file));
	}

	public static String getUniqueIdentifierString() {
		final StringBuilder builder = new StringBuilder();
		final Date date = new Date();
		final SimpleDateFormat dateFormat = new SimpleDateFormat(FOLDER_DATEFORMAT);
		final Random rand = new Random();
		rand.nextInt(50);
		builder.append(dateFormat.format(date));
		builder.append(FILENAME_DELIMITER);
		builder.append(rand.nextInt(100000));
		return builder.toString();
	}

	public static boolean hasCacheMock() {
		String hasCache = getEnv(Constants.CACHE_MOCK, EMPTY);
		if (StringUtils.isNotEmpty(hasCache)) {
			if (StringUtils.equalsIgnoreCase(hasCache, Boolean.toString(true))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isDevMode() {
		String isDevMode = getEnv(Constants.DEV_MODE, EMPTY);
		if (StringUtils.isNotEmpty(isDevMode)) {
			if (StringUtils.equalsIgnoreCase(isDevMode, Boolean.toString(true))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isStandalone() {
		String isStandaloneMode = getEnv(Constants.STANDALONE, EMPTY);
		if (StringUtils.isNotEmpty(isStandaloneMode)) {
			if (StringUtils.equalsIgnoreCase(isStandaloneMode, Boolean.toString(true))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isCorsEnabled() {
		String isCorsMode = getEnv(Constants.CORS_ENABLED, EMPTY);
		if (StringUtils.isNotEmpty(isCorsMode)) {
			if (StringUtils.equalsIgnoreCase(isCorsMode, Boolean.toString(true))) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isJson(String json) {
		Gson gson = new Gson();
		try {
			gson.fromJson(json, Object.class);
			Object jsonObjType = gson.fromJson(json, Object.class).getClass();
			if (jsonObjType.equals(String.class)) {
				return false;
			}
			return true;
		} catch (com.google.gson.JsonSyntaxException exception) {
			return false;
		}
	}

	public static boolean isSecured() {
		String authEnabled = getEnv("AUTH_ENABLED", EMPTY);
		if (StringUtils.isNotEmpty(authEnabled)) {
			if (StringUtils.equalsIgnoreCase(authEnabled, Boolean.toString(true))) {
				return true;
			}
		}
		return false;
	}

	public static boolean match(String filename, FileExtension fileExtension) {
		return StringUtils.endsWithIgnoreCase(filename, fileExtension.toString());
	}

	public static String toJson(Exception exception) {
		String json = EMPTY;
		json = new Gson().toJson(new ErrorResponse(exception.getCause().getMessage()));
		return json;
	}

//	public static String toJson(final List<Object> list) {
//		String json = EMPTY;
//		try {
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
//		} catch (JsonProcessingException exception) {
//		}
//		return json;
//	}
//
//	public static String toJson(final Object object) {
//		String json = EMPTY;
//		try {
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
//		} catch (JsonProcessingException exception) {
//		}
//		return json;
//	}

	public static String validPathParamId(Context ctx) {
		return ctx.pathParam(ID, String.class).get();
	}
}

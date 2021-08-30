package de.polipol.analytics.file;

import static de.polipol.analytics.commons.Constants.CACHE_SEPARATOR;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import de.polipol.analytics.commons.Elements;
import de.polipol.analytics.model.File;

public interface FileService {

	public static String getDirectoryString(String elemenmtId, Map<String, String> variables) {
		String separator = java.io.File.separator.toString();
		String filterPathString = separator + elemenmtId;
		List<String> sortedKeys = Lists.newArrayList(variables.keySet());
		Collections.sort(sortedKeys);
		for (String key : sortedKeys) {
			filterPathString = filterPathString + separator + variables.get(key);
		}
		return filterPathString;
	}

	public static String getId(final String directory, final String role, final String filename) {
		String id = EMPTY;
		if (StringUtils.isNotEmpty(filename) && StringUtils.isNotEmpty(directory)) {
			id = Elements.FILES + CACHE_SEPARATOR + role + CACHE_SEPARATOR + directory + CACHE_SEPARATOR + filename;
		}
		return id;
	}

	void delete(String directory, String role, String filename) throws IOException;

	Optional<File> find(String directory, String role, String filename, boolean useCache);

	Collection<File> getAll(String directory, String role) throws IOException;

	void upsert(String directory, String role, String filename, InputStream inputStream) throws IOException;
}

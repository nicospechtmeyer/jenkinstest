package de.polipol.analytics.model;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public final class ModelUtils {

	public static Filter getFilterFromJson(String json) {
		Filter filter = new Gson().fromJson(json, DefaultFilter.class);
		return filter;
	}

	public static Filter getFilterFromJson(String id, String json) {
		Filter filter = getFilterFromJson(json);
		filter.setId(id);
		return filter;
	}

	public static Job getJobFromJson(String json) {
		Job job = new Gson().fromJson(json, DefaultJob.class);
		return job;
	}

	public static Job getJobFromJson(String id, String json) {
		Job job = getJobFromJson(json);
		job.setId(id);
		return job;
	}

	public static Notebook getNotebookFromJson(String json) {
		Notebook notebook = new Gson().fromJson(json, DefaultNotebook.class);
		return notebook;
	}

	public static Notebook getNotebookFromJson(String id, String json) {
		Notebook notebook = getNotebookFromJson(json);
		notebook.setId(id);
		return notebook;
	}

	public static Paragraph getParagraphFromJson(String json) {
		Paragraph paragraph = new Gson().fromJson(json, DefaultParagraph.class);
		return paragraph;
	}

	public static Paragraph getParagraphFromJson(String id, String json) {
		Paragraph paragraph = getParagraphFromJson(json);
		paragraph.setId(id);
		return paragraph;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Element> T getUpdatedElement(Element notebook, String field, String data, Class clazz) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
		Map<String, String> objectMap = objectMapper.convertValue(notebook, Map.class);
		if (!StringUtils.isEmpty(data)) {
			objectMap.put(field, data);
		} else {
			objectMap.put(field, EMPTY);
		}
		T targetElement = (T) objectMapper.convertValue(objectMap, clazz);
		return targetElement;
	}

	public static DefaultFilter getUpdatedElement(Filter paragraph, String data) {
		DefaultFilter element = ModelUtils.getUpdatedElement(paragraph, EMPTY, data, DefaultFilter.class);
		return element;
	}

	public static DefaultFilter getUpdatedElement(Filter paragraph, String field, String data) {
		DefaultFilter element = ModelUtils.getUpdatedElement(paragraph, field, data, DefaultFilter.class);
		return element;
	}

	public static DefaultJob getUpdatedElement(Job paragraph, String data) {
		DefaultJob element = ModelUtils.getUpdatedElement(paragraph, EMPTY, data, DefaultJob.class);
		return element;
	}

	public static DefaultJob getUpdatedElement(Job paragraph, String field, String data) {
		DefaultJob element = ModelUtils.getUpdatedElement(paragraph, field, data, DefaultJob.class);
		return element;
	}

	public static DefaultNotebook getUpdatedElement(Notebook paragraph, String data) {
		DefaultNotebook element = ModelUtils.getUpdatedElement(paragraph, EMPTY, data, DefaultNotebook.class);
		return element;
	}

	public static DefaultNotebook getUpdatedElement(Notebook paragraph, String field, String data) {
		DefaultNotebook element = ModelUtils.getUpdatedElement(paragraph, field, data, DefaultNotebook.class);
		return element;
	}

	public static DefaultParagraph getUpdatedElement(Paragraph paragraph, String data) {
		DefaultParagraph element = ModelUtils.getUpdatedElement(paragraph, EMPTY, data, DefaultParagraph.class);
		return element;
	}

	public static DefaultParagraph getUpdatedElement(Paragraph paragraph, String field, String data) {
		DefaultParagraph element = ModelUtils.getUpdatedElement(paragraph, field, data, DefaultParagraph.class);
		return element;
	}
}

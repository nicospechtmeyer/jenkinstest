package de.polipol.analytics.connect.data.semantic;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.exception.InvalidLabelException;

public class DefaultLabel implements Label {

	protected Map<String, String> context;
	protected String meaning;

	public DefaultLabel(final Label label) {
		super();
		this.meaning = label.getMeaning();
		this.context = label.getContext();
	}

	public DefaultLabel(final String labelString) throws InvalidLabelException {
		super();
		this.meaning = EMPTY;
		this.context = new HashMap<String, String>();
		evaluateLabelString(labelString);
	}

	private void evaluateLabelString(final String labelString) throws InvalidLabelException {
		String[] splittedLabelString = StringUtils.split(labelString, ELEMENTSTRING_DELIMITER);
		if (splittedLabelString.length > 0) {
			String annotation = splittedLabelString[0];
			String[] splittedAnnotation = StringUtils.split(annotation, ANNOTATION_DELIMITER);
			if (splittedAnnotation.length > 0) {
				this.meaning = splittedAnnotation[0];
				if (splittedAnnotation.length > 1) {
					for (int index = 1; index < splittedAnnotation.length; index++) {
						String[] splittedContext = StringUtils.split(splittedAnnotation[index], CONTEXT_DELIMITER);
						if (splittedContext.length == 2) {
							context.put(splittedContext[0], splittedContext[1]);
						} else {
							throw new InvalidLabelException();
						}
					}
				}
			} else {
				throw new InvalidLabelException();

			}
		}
	}

	@Override
	public String getAnnotationString() {
		return getAnnotationString(ANNOTATION_DELIMITER, CONTEXT_DELIMITER);
	}

	public String getAnnotationString(final String delimiter, final String contextDelimiter) {
		final StringBuilder builder = new StringBuilder();
		builder.append(getMeaning());
		if (!context.isEmpty()) {
			builder.append(delimiter);
		}
		builder.append(getContextString(contextDelimiter));
		return builder.toString();
	}

	@Override
	public Map<String, String> getContext() {
		return context;
	}

	@Override
	public String getContextString() {
		return getContextString(CONTEXT_DELIMITER);
	}

	public String getContextString(final String delimiter) {
		final StringBuilder builder = new StringBuilder();
		List<String> sortedContextKeys = new ArrayList<String>();
		sortedContextKeys.addAll(context.keySet());
		Collections.sort(sortedContextKeys);
		for (String contextKey : sortedContextKeys) {
			builder.append(contextKey);
			builder.append(delimiter);
			builder.append(context.get(contextKey));
		}
		return builder.toString();
	}

	@Override
	public String getHeaderString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getAnnotationString(ANNOTATION_DELIMITER, CONTEXT_DELIMITER));
		return builder.toString();
	}

	@Override
	public String getMeaning() {
		return meaning;
	}

	@Override
	public boolean isNativeLabel() {
		return this.getAnnotationString().startsWith(NATIVESTRING_IDENTIFIER) ? true : false;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getAnnotationString(ANNOTATION_DELIMITER, CONTEXT_DELIMITER));
		return builder.toString();
	}
}

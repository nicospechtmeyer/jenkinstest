package de.polipol.analytics.commons.io;

import org.apache.commons.lang3.StringUtils;

public final class DefaultFormattingStrategy implements FormattingStrategy {

	@Override
	public String formatString(final String string) {
		return StringUtils.trim(string);
	}
}

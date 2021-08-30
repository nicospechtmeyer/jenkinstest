package de.polipol.analytics;

import de.polipol.analytics.web.ServerFactory;

public final class MicroAll {

	public static void main(final String[] args) {
		ServerFactory.importParagraphs();
		ServerFactory.startAll();
	}
}

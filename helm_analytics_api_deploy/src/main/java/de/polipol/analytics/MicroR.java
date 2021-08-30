package de.polipol.analytics;

import static de.polipol.analytics.commons.Constants.ARG_PORT;
import static de.polipol.analytics.commons.Constants.ARG_PORT_DESC;
import static de.polipol.analytics.commons.Constants.ARG_PORT_SHORT;
import static de.polipol.analytics.commons.Constants.DEFAULT_PORT;
import static de.polipol.analytics.commons.Messages.MESSAGE_PARSING_FAILED_REASON;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.web.ServerFactory;

public final class MicroR {

	private static final Logger logger = LoggerFactory.getLogger(MicroR.class);

	public static void main(final String[] args) {

		Options options = new Options();
		Option input;

		input = new Option(ARG_PORT_SHORT, ARG_PORT, true, ARG_PORT_DESC);
		input.setRequired(false);
		options.addOption(input);

		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
			int port = NumberUtils.toInt(cmd.getOptionValue(ARG_PORT, String.valueOf(DEFAULT_PORT)));

			if (Utils.isStandalone()) {
				ServerFactory.startRServer(port);
			} else {
				ServerFactory.startExtendedRServer(port);
			}
		} catch (ParseException exception) {
			logger.error(MESSAGE_PARSING_FAILED_REASON + exception.getMessage());
		}
	}
}

package de.polipol.analytics.web;

import static de.polipol.analytics.commons.Constants.APPLICATION_JSON;
import static de.polipol.analytics.commons.Constants.AUTH_CALLBACK;
import static de.polipol.analytics.commons.Constants.AUTH_CLIENT_ID;
import static de.polipol.analytics.commons.Constants.AUTH_ENABLED;
import static de.polipol.analytics.commons.Constants.CORS_ENABLED;
import static de.polipol.analytics.commons.Constants.AUTH_HOST;
import static de.polipol.analytics.commons.Constants.AUTH_REALM;
import static de.polipol.analytics.commons.Constants.CACHE_MOCK;
import static de.polipol.analytics.commons.Constants.DEV_MODE;
import static de.polipol.analytics.commons.Constants.LOGGER_SEPARATOR;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.polipol.analytics.commons.Utils;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public final class AppFactory {

	private static final String STATIC_FILES_TESTS = "./tests";
	private static final Logger logger = LoggerFactory.getLogger(AppFactory.class);

	public static Optional<Javalin> createApp() {
		return create(false);
	}
	
	public static Optional<Javalin> createVueApp() {
		return create(true);
	}
	
	private static Optional<Javalin> create(boolean vueApp) {
		logger.info(LOGGER_SEPARATOR);
		logger.info(DEV_MODE + ": " + Utils.isDevMode());
		logger.info(CACHE_MOCK + ": " + Utils.hasCacheMock());
		logger.info(AUTH_ENABLED + ": " + Utils.isSecured());
		logger.info(CORS_ENABLED + ": " + Utils.isCorsEnabled());
		if (Utils.isSecured()) {
			logger.info(AUTH_HOST + ": " + System.getenv(AUTH_HOST));
			logger.info(AUTH_CLIENT_ID + ": " + System.getenv(AUTH_CLIENT_ID));
			logger.info(AUTH_REALM + ": " + System.getenv(AUTH_REALM));
			logger.info(AUTH_CALLBACK + ": " + System.getenv(AUTH_CALLBACK));
		}
		logger.info(LOGGER_SEPARATOR);
		Optional<Javalin> app = Optional.empty();
		if (Utils.isDevMode()) {
			app = Optional.ofNullable(Javalin.create(config -> {
				if (vueApp) {
					config.enableDevLogging();
					config.enableCorsForAllOrigins();
					config.enableWebjars();		
					config.addStaticFiles(STATIC_FILES_TESTS, Location.EXTERNAL);
				} else {
					config.enableDevLogging();
					config.enableCorsForAllOrigins();
					config.defaultContentType = APPLICATION_JSON;
					config.registerPlugin(AbstractMicroServer.getConfiguredOpenApiPlugin());
				}
			}));
		} else {
			app = Optional.ofNullable(Javalin.create(config -> {
				if (vueApp) {
					config.enableWebjars();
					config.addStaticFiles(STATIC_FILES_TESTS, Location.EXTERNAL);
				} else {
					config.defaultContentType = APPLICATION_JSON;
					config.registerPlugin(AbstractMicroServer.getConfiguredOpenApiPlugin());
				}
				if (Utils.isCorsEnabled()) {
					config.enableCorsForAllOrigins();
				}
			}));
		}
		return app;
	}
}

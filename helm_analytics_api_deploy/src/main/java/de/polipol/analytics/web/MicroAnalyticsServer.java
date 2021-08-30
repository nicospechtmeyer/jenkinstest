package de.polipol.analytics.web;

import static de.polipol.analytics.commons.Constants.ENDPOINT_ENCRYPT;
import static de.polipol.analytics.commons.Constants.ENDPOINT_EVALUATE;
import static de.polipol.analytics.commons.Constants.ENDPOINT_EVALUATE_FILE;
import static de.polipol.analytics.commons.Constants.ENDPOINT_INFOS;
import static de.polipol.analytics.commons.Constants.SECURITY_AUTHORIZER;
import static de.polipol.analytics.commons.Constants.SECURITY_CLIENT;
import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Optional;
import org.pac4j.core.config.Config;
import org.pac4j.javalin.SecurityHandler;

import de.polipol.analytics.auth.KeycloakConfigFactory;
import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.connect.AnalyticsService;
import de.polipol.analytics.file.FileService;
import de.polipol.analytics.web.controller.AnalyticsController;
import de.polipol.analytics.web.controller.ExtendedAnalyticsController;
import de.polipol.analytics.web.controller.InfoController;
import io.javalin.Javalin;
//import io.javalin.plugin.rendering.vue.VueComponent;

public final class MicroAnalyticsServer {

	public static void start(AnalyticsService analyticsService, int port) {

		AnalyticsController analyticsController = new AnalyticsController(analyticsService);
		InfoController infoController = new InfoController();
		
		Optional<Javalin> app = AppFactory.createApp();
		if (app.isPresent()) {
			app.get().routes(() -> {
				if (Utils.isSecured()) {
					Config config = new KeycloakConfigFactory().build();
					SecurityHandler securityHandler = new SecurityHandler(config, SECURITY_CLIENT, SECURITY_AUTHORIZER);
					before(ENDPOINT_EVALUATE, securityHandler);
				}
				path(ENDPOINT_EVALUATE, () -> {
					post(analyticsController::evaluate);
				});
				path(ENDPOINT_INFOS, () -> {
					get(infoController::getPulse);
				});
				path(ENDPOINT_ENCRYPT, () -> {
					post(infoController::encrypt);
				});
			}).start(port);

//			app.get().get(ENDPOINT_INFOS, new VueComponent("<overview></overview>"));

			app.get().error(404, ctx -> {
				ctx.result(EMPTY);
			});
		}
	}

	public static void startExtendedMode(AnalyticsService analyticsService, FileService fileService, int port) {

		AnalyticsController analyticsController = new AnalyticsController(analyticsService);
		ExtendedAnalyticsController extendedAnalyticsController = new ExtendedAnalyticsController(analyticsService,
				fileService);
		InfoController infoController = new InfoController();

		Optional<Javalin> app = AppFactory.createApp();
		if (app.isPresent()) {
			app.get().routes(() -> {
				if (Utils.isSecured()) {
					Config config = new KeycloakConfigFactory().build();
					SecurityHandler securityHandler = new SecurityHandler(config, SECURITY_CLIENT, SECURITY_AUTHORIZER);
					before(ENDPOINT_EVALUATE, securityHandler);
					before(ENDPOINT_EVALUATE_FILE, securityHandler);
				}
				path(ENDPOINT_EVALUATE, () -> {
					post(analyticsController::evaluate);
				});
				path(ENDPOINT_EVALUATE_FILE, () -> {
					post(extendedAnalyticsController::evaluate);
				});
				path(ENDPOINT_INFOS, () -> {
					get(infoController::getPulse);
				});
				path(ENDPOINT_ENCRYPT, () -> {
					post(infoController::encrypt);
				});
			}).start(port);

//			app.get().get(ENDPOINT_INFOS, new VueComponent("<overview></overview>"));

			app.get().exception(Exception.class, (exception, ctx) -> {
				ctx.status(400);
				ctx.result(Utils.toJson(exception));
			});
		}
	}
}

package de.polipol.analytics.web;

import static de.polipol.analytics.commons.Constants.ENDPOINT_FILE;
import static de.polipol.analytics.commons.Constants.ENDPOINT_FILES;
import static de.polipol.analytics.commons.Constants.ENDPOINT_INFOS;
import static de.polipol.analytics.commons.Constants.SECURITY_AUTHORIZER;
import static de.polipol.analytics.commons.Constants.SECURITY_CLIENT;
import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import java.io.FileNotFoundException;
import java.util.Optional;

import org.pac4j.core.config.Config;
import org.pac4j.javalin.SecurityHandler;

import de.polipol.analytics.auth.KeycloakConfigFactory;
import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.file.FileService;
import de.polipol.analytics.web.controller.FileController;
import de.polipol.analytics.web.controller.InfoController;
import io.javalin.Javalin;

public final class MicroFileServer {

	public static void start(FileService fileService, int port) {

		FileController fileController = new FileController(fileService);
		InfoController infoController = new InfoController();

		Optional<Javalin> app = AppFactory.createApp();
		if (app.isPresent()) {
			app.get().routes(() -> {
				if (Utils.isSecured()) {
					Config config = new KeycloakConfigFactory().build();
					SecurityHandler securityHandler = new SecurityHandler(config, SECURITY_CLIENT, SECURITY_AUTHORIZER);
					before(ENDPOINT_FILES, securityHandler);
				}
				path(ENDPOINT_INFOS, () -> {
					get(infoController::getPulse);
				});
				path(ENDPOINT_FILES, () -> {
					get(fileController::getAll);
					post(fileController::create);
					path(ENDPOINT_FILE, () -> {
						get(fileController::getOne);
						post(fileController::getOneByFilter);
						delete(fileController::delete);
					});
				});
			}).start(port);

			app.get().exception(FileNotFoundException.class, (e, ctx) -> {
				ErrorResponse response = new ErrorResponse("file not exists / supported");
				ctx.status(400).json(response);
			});
		}
	}
}

package de.polipol.analytics.web;

import static de.polipol.analytics.commons.Constants.ENDPOINT_EVALUATE;
import static de.polipol.analytics.commons.Constants.ENDPOINT_FILTERS;
import static de.polipol.analytics.commons.Constants.ENDPOINT_INFOS;
import static de.polipol.analytics.commons.Constants.ENDPOINT_INTERPRETERS;
import static de.polipol.analytics.commons.Constants.ENDPOINT_LOGS;
import static de.polipol.analytics.commons.Constants.ENDPOINT_NOTEBOOKS;
import static de.polipol.analytics.commons.Constants.ENDPOINT_PARAGRAPHS;
import static de.polipol.analytics.commons.Constants.ID;
import static de.polipol.analytics.commons.Constants.SECURITY_AUTHORIZER;
import static de.polipol.analytics.commons.Constants.SECURITY_CLIENT;
import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.List;
import java.util.Optional;

import org.pac4j.core.config.Config;
import org.pac4j.javalin.SecurityHandler;

import de.polipol.analytics.auth.KeycloakConfigFactory;
import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.model.Filter;
import de.polipol.analytics.model.Interpreter;
import de.polipol.analytics.model.ModelService;
import de.polipol.analytics.model.Notebook;
import de.polipol.analytics.model.Paragraph;
import de.polipol.analytics.web.controller.FilterController;
import de.polipol.analytics.web.controller.InfoController;
import de.polipol.analytics.web.controller.InterpreterController;
import de.polipol.analytics.web.controller.NotebookController;
import de.polipol.analytics.web.controller.ParagraphController;
import de.polipol.analytics.web.controller.TaskController;
import io.javalin.Javalin;

public final class MicroApp {

	public static void start(List<Interpreter> kernels, ModelService<Notebook> notebookService,
			ModelService<Paragraph> paragraphService, ModelService<Filter> filterService, int port) {

		InterpreterController interpreterController = new InterpreterController(kernels);
		NotebookController notebookController = new NotebookController(notebookService);
		ParagraphController paragraphController = new ParagraphController(paragraphService);
		FilterController filterController = new FilterController(filterService);
		InfoController infoController = new InfoController();
		TaskController taskController = new TaskController();

		Optional<Javalin> app = AppFactory.createApp();
		if (app.isPresent()) {
			app.get().routes(() -> {
				if (Utils.isSecured()) {
					Config config = new KeycloakConfigFactory().build();
					SecurityHandler securityHandler = new SecurityHandler(config, SECURITY_CLIENT, SECURITY_AUTHORIZER);
					before(ENDPOINT_NOTEBOOKS, securityHandler);
					before(ENDPOINT_PARAGRAPHS, securityHandler);
					before(ENDPOINT_FILTERS, securityHandler);
					before(ENDPOINT_LOGS, securityHandler);
					before(ENDPOINT_EVALUATE, securityHandler);
				}
				path(ENDPOINT_INFOS, () -> {
					get(infoController::getPulse);
				});
				path(ENDPOINT_INTERPRETERS, () -> {
					get(interpreterController::getAll);
				});
				path(ENDPOINT_NOTEBOOKS, () -> {
					get(notebookController::getAll);
					post(notebookController::create);
					path(":" + ID, () -> {
						get(notebookController::getOne);
						patch(notebookController::upsert);
						delete(notebookController::delete);
					});
				});
				path(ENDPOINT_PARAGRAPHS, () -> {
					get(paragraphController::getAll);
					post(paragraphController::create);
					path(":" + ID, () -> {
						get(paragraphController::getOne);
						patch(paragraphController::upsert);
						delete(paragraphController::delete);
					});
				});
				path(ENDPOINT_FILTERS, () -> {
					get(filterController::getAll);
					post(filterController::create);
					path(":" + ID, () -> {
						get(filterController::getOne);
						patch(filterController::upsert);
						delete(filterController::delete);
					});
				});
				path(ENDPOINT_EVALUATE, () -> {
					post(taskController::evalParagraph);
				});
			}).start(port).error(404, ctx -> {
				ctx.result(EMPTY);
			});
		}
//		app.exception(TechnicalException.class, (e, ctx) -> {
//			ctx.result(e.getMessage());
//		});
	}
}
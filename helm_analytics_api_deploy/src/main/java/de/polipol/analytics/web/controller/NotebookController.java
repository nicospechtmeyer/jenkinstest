package de.polipol.analytics.web.controller;

import static de.polipol.analytics.commons.Constants.ENDPOINT_NOTEBOOKS;
import static de.polipol.analytics.commons.Constants.FIELD;
import static de.polipol.analytics.commons.Constants.ID;
import static de.polipol.analytics.commons.Messages.MESSAGE_NOT_FOUND;
import static de.polipol.analytics.commons.Constants.NOTEBOOK;
import static de.polipol.analytics.commons.Constants.NOTEBOOK_ID_DESC;
import static de.polipol.analytics.model.jsondb.JsonDBConstants.CREATOR_IDENTIFIER;
import static io.javalin.plugin.openapi.annotations.HttpMethod.DELETE;
import static io.javalin.plugin.openapi.annotations.HttpMethod.GET;
import static io.javalin.plugin.openapi.annotations.HttpMethod.POST;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.auth.CustomAuthorizer;
import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.model.ModelService;
import de.polipol.analytics.model.ModelUtils;
import de.polipol.analytics.model.Notebook;
import de.polipol.analytics.web.ErrorResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;

public final class NotebookController {

	ModelService<Notebook> modelService;

	public NotebookController(ModelService<Notebook> modelService) {
		this.modelService = modelService;
	}

	@OpenApi(summary = "Create notebook", path = ENDPOINT_NOTEBOOKS, method = POST, tags = {
			NOTEBOOK }, requestBody = @OpenApiRequestBody(content = {
					@OpenApiContent(from = Notebook.class) }), responses = { @OpenApiResponse(status = "204"),
							@OpenApiResponse(status = "400", content = {
									@OpenApiContent(from = ErrorResponse.class) }) })
	public void create(Context ctx) {
		try {
			Notebook notebook = ModelUtils.getNotebookFromJson(ctx.body());
			ctx.json(modelService.upsert(notebook));
		} catch (Exception exception) {
			new ErrorResponse(exception);
		}
	}

	@OpenApi(summary = "Delete notebook by ID", path = ENDPOINT_NOTEBOOKS + "/:" + ID, method = DELETE, pathParams = {
			@OpenApiParam(name = ID, type = String.class, description = NOTEBOOK_ID_DESC) }, tags = {
					NOTEBOOK }, responses = { @OpenApiResponse(status = "204"),
							@OpenApiResponse(status = "400", content = {
									@OpenApiContent(from = ErrorResponse.class) }) })
	public void delete(Context ctx) {
		try {
			modelService.delete(Utils.validPathParamId(ctx));
		} catch (Exception exception) {
			new ErrorResponse(exception);
		}
	}

	@OpenApi(summary = "Get notebooks", path = ENDPOINT_NOTEBOOKS, method = GET, tags = { NOTEBOOK }, responses = {
			@OpenApiResponse(status = "200", content = { @OpenApiContent(from = Notebook[].class) }) })
	public void getAll(Context ctx) {
		if (Utils.isSecured()) {
			ctx.json(modelService.getAll(CREATOR_IDENTIFIER, CustomAuthorizer.getUsername(ctx)));
		} else {
			ctx.json(modelService.getAll());
		}
	}

	@OpenApi(summary = "Get notebook by ID", path = ENDPOINT_NOTEBOOKS + "/:" + ID, method = GET, pathParams = {
			@OpenApiParam(name = ID, type = String.class, description = NOTEBOOK_ID_DESC) }, tags = {
					NOTEBOOK }, responses = {
							@OpenApiResponse(status = "200", content = { @OpenApiContent(from = Notebook.class) }),
							@OpenApiResponse(status = "404", content = {
									@OpenApiContent(from = NotFoundResponse.class) }) })
	public void getOne(Context ctx) {
		Notebook notebook = modelService.find(Utils.validPathParamId(ctx), true)
				.orElseThrow(() -> new NotFoundResponse(MESSAGE_NOT_FOUND));
		ctx.json(notebook);
	}

	@OpenApi(summary = "Update notebook by ID", path = ENDPOINT_NOTEBOOKS + "/:"
			+ ID, method = HttpMethod.PATCH, pathParams = {
					@OpenApiParam(name = ID, type = String.class, description = NOTEBOOK_ID_DESC) }, queryParams = {
							@OpenApiParam(name = FIELD, type = String.class, description = "Attribute name") }, tags = {
									NOTEBOOK }, requestBody = @OpenApiRequestBody(content = {
											@OpenApiContent(from = Notebook.class) }), responses = {
													@OpenApiResponse(status = "204"),
													@OpenApiResponse(status = "400", content = {
															@OpenApiContent(from = ErrorResponse.class) }) })
	public void upsert(Context ctx) {
		try {
			final String id = ctx.pathParam(ID);
			final String field = ctx.queryParam(FIELD);
			final String data = ctx.body();
			if (!StringUtils.isEmpty(id)) {
				if (StringUtils.isEmpty(field)) {
					Notebook notebook = ModelUtils.getNotebookFromJson(id, data);
					modelService.upsert(notebook);
				} else {
					Optional<Notebook> sourceNotebook = modelService.find(id, true);
					if (sourceNotebook.isPresent()) {
						Notebook targetNotebook = ModelUtils.getUpdatedElement(sourceNotebook.get(), field, data);
						modelService.upsert(targetNotebook);
					}
				}
			}
		} catch (Exception exception) {
			new ErrorResponse(exception);
		}
	}
}
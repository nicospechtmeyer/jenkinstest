package de.polipol.analytics.web.controller;

import static de.polipol.analytics.commons.Constants.ENDPOINT_PARAGRAPHS;
import static de.polipol.analytics.commons.Constants.FIELD;
import static de.polipol.analytics.commons.Constants.FIELD_DESC;
import static de.polipol.analytics.commons.Constants.ID;
import static de.polipol.analytics.commons.Messages.MESSAGE_NOT_FOUND;
import static de.polipol.analytics.commons.Constants.NOTEBOOK_ID;
import static de.polipol.analytics.commons.Constants.NOTEBOOK_ID_DESC;
import static de.polipol.analytics.commons.Constants.PARAGRAPH;
import static de.polipol.analytics.commons.Constants.PARAGRAPH_ID_DESC;
import static de.polipol.analytics.model.jsondb.JsonDBConstants.CREATOR_IDENTIFIER;
import static io.javalin.plugin.openapi.annotations.HttpMethod.DELETE;
import static io.javalin.plugin.openapi.annotations.HttpMethod.GET;
import static io.javalin.plugin.openapi.annotations.HttpMethod.PATCH;
import static io.javalin.plugin.openapi.annotations.HttpMethod.POST;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.auth.CustomAuthorizer;
import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.model.ModelService;
import de.polipol.analytics.model.ModelUtils;
import de.polipol.analytics.model.Paragraph;
import de.polipol.analytics.web.ErrorResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;

public final class ParagraphController {

	ModelService<Paragraph> modelService;

	public ParagraphController(ModelService<Paragraph> modelService) {
		this.modelService = modelService;
	}

	@OpenApi(summary = "Create paragraph", path = ENDPOINT_PARAGRAPHS, method = POST, tags = {
			PARAGRAPH }, requestBody = @OpenApiRequestBody(content = {
					@OpenApiContent(from = Paragraph.class) }), responses = { @OpenApiResponse(status = "204"),
							@OpenApiResponse(status = "400", content = {
									@OpenApiContent(from = ErrorResponse.class) }) })
	public void create(Context ctx) {
		try {
			Paragraph paragraph = ModelUtils.getParagraphFromJson(ctx.body());
			ctx.json(modelService.upsert(paragraph));
		} catch (Exception exception) {
			new ErrorResponse(exception);
		}
	}

	@OpenApi(summary = "Delete paragraph by ID", path = ENDPOINT_PARAGRAPHS + "/:" + ID, method = DELETE, pathParams = {
			@OpenApiParam(name = ID, type = String.class, description = PARAGRAPH_ID_DESC) }, tags = {
					PARAGRAPH }, responses = { @OpenApiResponse(status = "204"),
							@OpenApiResponse(status = "400", content = {
									@OpenApiContent(from = ErrorResponse.class) }) })
	public void delete(Context ctx) {
		try {
			modelService.delete(Utils.validPathParamId(ctx));
		} catch (Exception exception) {
			new ErrorResponse(exception);
		}
	}

	@OpenApi(summary = "Get paragraphs", path = ENDPOINT_PARAGRAPHS, method = GET, queryParams = {
			@OpenApiParam(name = NOTEBOOK_ID, allowEmptyValue = true, required = false, type = String.class, description = NOTEBOOK_ID_DESC) }, tags = {
					PARAGRAPH }, responses = {
							@OpenApiResponse(status = "200", content = { @OpenApiContent(from = Paragraph[].class) }) })
	public void getAll(Context ctx) {
		final String notebookId = ctx.queryParam(NOTEBOOK_ID, EMPTY);
		if (StringUtils.isNotEmpty(notebookId)) {
			ctx.json(modelService.getAll(NOTEBOOK_ID, notebookId));
		} else {
			if (Utils.isSecured()) {
				ctx.json(modelService.getAll(CREATOR_IDENTIFIER, CustomAuthorizer.getUsername(ctx)));
			} else {
				ctx.json(modelService.getAll());
			}
		}
	}

	@OpenApi(summary = "Get paragraph by ID", path = ENDPOINT_PARAGRAPHS + "/:" + ID, method = GET, pathParams = {
			@OpenApiParam(name = ID, type = String.class, description = PARAGRAPH_ID_DESC) }, tags = {
					PARAGRAPH }, responses = {
							@OpenApiResponse(status = "200", content = { @OpenApiContent(from = Paragraph.class) }),
							@OpenApiResponse(status = "404", content = {
									@OpenApiContent(from = NotFoundResponse.class) }) })
	public void getOne(Context ctx) {
		Paragraph paragraph = modelService.find(Utils.validPathParamId(ctx), true)
				.orElseThrow(() -> new NotFoundResponse(MESSAGE_NOT_FOUND));
		ctx.json(paragraph);
	}

	@OpenApi(summary = "Update paragraph by ID", path = ENDPOINT_PARAGRAPHS + "/:" + ID, method = PATCH, pathParams = {
			@OpenApiParam(name = ID, type = String.class, description = NOTEBOOK_ID_DESC) }, queryParams = {
					@OpenApiParam(name = FIELD, type = String.class, description = FIELD_DESC) }, tags = {
							PARAGRAPH }, requestBody = @OpenApiRequestBody(content = {
									@OpenApiContent(from = Paragraph.class) }), responses = {
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
					Paragraph paragraph = ModelUtils.getParagraphFromJson(id, data);
					modelService.upsert(paragraph);
				} else {
					Optional<Paragraph> sourceParagraph = modelService.find(id, true);
					if (sourceParagraph.isPresent()) {
						Paragraph targetParagraph = ModelUtils.getUpdatedElement(sourceParagraph.get(), field, data);
						modelService.upsert(targetParagraph);
					}
				}
			}
		} catch (Exception exception) {
			new ErrorResponse();
		}
	}
}
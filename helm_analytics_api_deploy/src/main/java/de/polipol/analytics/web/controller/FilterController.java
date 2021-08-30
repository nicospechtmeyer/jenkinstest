package de.polipol.analytics.web.controller;

import static de.polipol.analytics.commons.Constants.ENDPOINT_FILTERS;
import static de.polipol.analytics.commons.Constants.FIELD;
import static de.polipol.analytics.commons.Constants.FIELD_DESC;
import static de.polipol.analytics.commons.Constants.FILTER;
import static de.polipol.analytics.commons.Constants.FILTER_ID_DESC;
import static de.polipol.analytics.commons.Constants.ID;
import static de.polipol.analytics.commons.Constants.NOTEBOOK_ID;
import static de.polipol.analytics.commons.Constants.NOTEBOOK_ID_DESC;
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
import de.polipol.analytics.model.Filter;
import de.polipol.analytics.model.ModelService;
import de.polipol.analytics.model.ModelUtils;
import de.polipol.analytics.web.ErrorResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;

public final class FilterController {

	ModelService<Filter> modelService;

	public FilterController(ModelService<Filter> modelService) {
		this.modelService = modelService;
	}

	@OpenApi(summary = "Create filter", path = ENDPOINT_FILTERS, method = POST, tags = {
			FILTER }, requestBody = @OpenApiRequestBody(content = {
					@OpenApiContent(from = Filter.class) }), responses = { @OpenApiResponse(status = "200"),
							@OpenApiResponse(status = "400", content = {
									@OpenApiContent(from = ErrorResponse.class) }) })
	public void create(Context ctx) {
		try {
			Filter filter = ModelUtils.getFilterFromJson(ctx.body());
			ctx.json(modelService.upsert(filter));
		} catch (Exception exception) {
			new ErrorResponse(exception);
		}
	}

	@OpenApi(summary = "Delete filter by ID", path = ENDPOINT_FILTERS + "/:" + ID, method = DELETE, pathParams = {
			@OpenApiParam(name = ID, type = String.class, description = FILTER_ID_DESC) }, tags = {
					FILTER }, responses = { @OpenApiResponse(status = "204"),
							@OpenApiResponse(status = "400", content = {
									@OpenApiContent(from = ErrorResponse.class) }) })
	public void delete(Context ctx) {
		try {
			modelService.delete(Utils.validPathParamId(ctx));
		} catch (Exception exception) {
			new ErrorResponse(exception);
		}
	}

	@OpenApi(summary = "Get filters", path = ENDPOINT_FILTERS, method = GET, queryParams = {
			@OpenApiParam(name = NOTEBOOK_ID, allowEmptyValue = true, required = false, type = String.class, description = NOTEBOOK_ID_DESC) }, tags = {
					FILTER }, responses = {
							@OpenApiResponse(status = "200", content = { @OpenApiContent(from = Filter[].class) }) })
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

	@OpenApi(summary = "Get filter by ID", path = ENDPOINT_FILTERS + "/:" + ID, method = GET, pathParams = {
			@OpenApiParam(name = ID, type = String.class, description = FILTER_ID_DESC) }, tags = {
					FILTER }, responses = {
							@OpenApiResponse(status = "200", content = { @OpenApiContent(from = Filter.class) }),
							@OpenApiResponse(status = "400", content = {
									@OpenApiContent(from = NotFoundResponse.class) }) })
	public void getOne(Context ctx) {
		Filter filter = modelService.find(Utils.validPathParamId(ctx), true)
				.orElseThrow(() -> new NotFoundResponse("Document not found"));
		ctx.json(filter);
	}

	@OpenApi(summary = "Update filter by ID", path = ENDPOINT_FILTERS + "/:" + ID, method = PATCH, pathParams = {
			@OpenApiParam(name = ID, type = String.class, description = NOTEBOOK_ID_DESC) }, queryParams = {
					@OpenApiParam(name = FIELD, type = String.class, description = FIELD_DESC) }, tags = {
							FILTER }, requestBody = @OpenApiRequestBody(content = {
									@OpenApiContent(from = Filter.class) }), responses = {
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
					Filter filter = ModelUtils.getFilterFromJson(id, data);
					modelService.upsert(filter);
				} else {
					Optional<Filter> sourceFilter = modelService.find(id, true);
					if (sourceFilter.isPresent()) {
						Filter targetFilter = ModelUtils.getUpdatedElement(sourceFilter.get(), field, data);
						modelService.upsert(targetFilter);
					}
				}
			}
		} catch (Exception exception) {
			new ErrorResponse(exception);
		}
	}
}
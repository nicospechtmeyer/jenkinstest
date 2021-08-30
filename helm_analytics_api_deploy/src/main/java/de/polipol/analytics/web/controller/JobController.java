package de.polipol.analytics.web.controller;

import static de.polipol.analytics.commons.Constants.ENDPOINT_JOBS;
import static de.polipol.analytics.commons.Constants.FIELD;
import static de.polipol.analytics.commons.Constants.FIELD_DESC;
import static de.polipol.analytics.commons.Constants.ID;
import static de.polipol.analytics.commons.Constants.JOB;
import static de.polipol.analytics.commons.Constants.JOB_ID_DESC;
import static de.polipol.analytics.commons.Messages.MESSAGE_NOT_FOUND;
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
import de.polipol.analytics.model.Job;
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

public final class JobController {

	ModelService<Job> modelService;

	public JobController(ModelService<Job> modelService) {
		this.modelService = modelService;
	}

	@OpenApi(summary = "Create job", path = ENDPOINT_JOBS, method = POST, tags = {
			JOB }, requestBody = @OpenApiRequestBody(content = { @OpenApiContent(from = Job.class) }), responses = {
					@OpenApiResponse(status = "204"),
					@OpenApiResponse(status = "400", content = { @OpenApiContent(from = ErrorResponse.class) }) })
	public void create(Context ctx) {
		try {
			Job job = ModelUtils.getJobFromJson(ctx.body());
			ctx.json(modelService.upsert(job));
		} catch (Exception exception) {
			new ErrorResponse(exception);
		}
	}

	@OpenApi(summary = "Delete job by ID", path = ENDPOINT_JOBS + "/:" + ID, method = DELETE, pathParams = {
			@OpenApiParam(name = ID, type = String.class, description = JOB_ID_DESC) }, tags = { JOB }, responses = {
					@OpenApiResponse(status = "204"),
					@OpenApiResponse(status = "400", content = { @OpenApiContent(from = ErrorResponse.class) }) })
	public void delete(Context ctx) {
		try {
			modelService.delete(Utils.validPathParamId(ctx));
		} catch (Exception exception) {
			new ErrorResponse(exception);
		}
	}

	@OpenApi(summary = "Get jobs", path = ENDPOINT_JOBS, method = GET, queryParams = {
			@OpenApiParam(name = NOTEBOOK_ID, allowEmptyValue = true, required = false, type = String.class, description = NOTEBOOK_ID_DESC) }, tags = {
					JOB }, responses = {
							@OpenApiResponse(status = "200", content = { @OpenApiContent(from = Job[].class) }) })
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

	@OpenApi(summary = "Get job by ID", path = ENDPOINT_JOBS + "/:" + ID, method = GET, pathParams = {
			@OpenApiParam(name = ID, type = String.class, description = JOB_ID_DESC) }, tags = { JOB }, responses = {
					@OpenApiResponse(status = "200", content = { @OpenApiContent(from = Job.class) }),
					@OpenApiResponse(status = "404", content = { @OpenApiContent(from = NotFoundResponse.class) }) })
	public void getOne(Context ctx) {
		Job job = modelService.find(Utils.validPathParamId(ctx), true)
				.orElseThrow(() -> new NotFoundResponse(MESSAGE_NOT_FOUND));
		ctx.json(job);
	}

	@OpenApi(summary = "Update job by ID", path = ENDPOINT_JOBS + "/:" + ID, method = PATCH, pathParams = {
			@OpenApiParam(name = ID, type = String.class, description = NOTEBOOK_ID_DESC) }, queryParams = {
					@OpenApiParam(name = FIELD, type = String.class, description = FIELD_DESC) }, tags = {
							JOB }, requestBody = @OpenApiRequestBody(content = {
									@OpenApiContent(from = Job.class) }), responses = {
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
					Job job = ModelUtils.getJobFromJson(id, data);
					modelService.upsert(job);
				} else {
					Optional<Job> sourceJob = modelService.find(id, true);
					if (sourceJob.isPresent()) {
						Job targetJob = ModelUtils.getUpdatedElement(sourceJob.get(), field, data);
						modelService.upsert(targetJob);
					}
				}
			}
		} catch (Exception exception) {
			new ErrorResponse(exception);
		}
	}
}
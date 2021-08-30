package de.polipol.analytics.web.controller;

import static de.polipol.analytics.commons.Constants.ENDPOINT_TASKS;
import static de.polipol.analytics.commons.Constants.KERNEL_ID;
import static de.polipol.analytics.commons.Constants.PARAGRAPH_ID;
import static de.polipol.analytics.commons.Constants.TASK;
import static io.javalin.plugin.openapi.annotations.HttpMethod.POST;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.polipol.analytics.auth.CustomAuthorizer;
import de.polipol.analytics.cache.Cache;
import de.polipol.analytics.task.DefaultTaskScheduler;
import de.polipol.analytics.task.TaskScheduler;
import de.polipol.analytics.web.ErrorResponse;
import de.polipol.analytics.web.ServerFactory;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;

public final class TaskController {

	@OpenApi(summary = "Evaluate paragraph by ID", path = ENDPOINT_TASKS, method = POST, tags = {
			TASK }, requestBody = @OpenApiRequestBody(content = { @OpenApiContent(from = String.class) }), responses = {
					@OpenApiResponse(status = "204"),
					@OpenApiResponse(status = "400", content = { @OpenApiContent(from = ErrorResponse.class) }) })
	public void evalParagraph(Context ctx) {
		try {
			final String paragraphId = ctx.queryParam(PARAGRAPH_ID);
			final String kernelId = ctx.queryParam(KERNEL_ID);
			final String role = CustomAuthorizer.getUsername(ctx);
			Map<String, String> variables = new HashMap<>();
			String data = ctx.body();
			if (StringUtils.isNotEmpty(data)) {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				TypeReference<Map<String, String>> token = new TypeReference<Map<String, String>>() {
				};
				variables = objectMapper.readValue(data, token);
			}
			if (StringUtils.isNotEmpty(kernelId) && StringUtils.isNotEmpty(paragraphId)) {
				Cache cache = ServerFactory.createCache().orElseThrow(IllegalStateException::new);
				TaskScheduler scheduler = new DefaultTaskScheduler(cache);
				scheduler.schedule(kernelId, role, paragraphId, variables);
			} else {
				new ErrorResponse();
			}
		} catch (Exception exception) {
			new ErrorResponse(exception);
		}
	}
}
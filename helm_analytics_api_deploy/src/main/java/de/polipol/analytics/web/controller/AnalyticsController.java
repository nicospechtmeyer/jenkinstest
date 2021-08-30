package de.polipol.analytics.web.controller;

import static de.polipol.analytics.commons.Constants.ANALYTICS;
import static de.polipol.analytics.commons.Constants.DEBUG;
import static de.polipol.analytics.commons.Constants.ENDPOINT_EVALUATE;
import static de.polipol.analytics.commons.Constants.EXPRESSION;
import static de.polipol.analytics.commons.Constants.OUTPUT;
import static io.javalin.plugin.openapi.annotations.HttpMethod.POST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.commons.MimeTypes;
import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.connect.AnalyticsService;
import de.polipol.analytics.file.FileExtension;
import de.polipol.analytics.web.CommonValidator;
import de.polipol.analytics.web.ErrorResponse;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import io.javalin.plugin.openapi.annotations.OpenApiParam;

public final class AnalyticsController {

	public static final String PARAM_TYPE = "type";
	public static final String PARAM_VARIABLE = "variable";
	public static final String PARAM_DEBUG = "debug";

	public static final String PARAM_WIDTH = "width";
	public static final String PARAM_HEIGHT = "height";
	public static final String PARAM_RESOLUTION = "resolution";

	public static final String TYPE_IMAGE = "image";
	public static final String TYPE_DOCUMENT = "document";
	public static final String TYPE_PLAIN = "plain";

	public static final String DEFAULT_WIDTH = "2000";
	public static final String DEFAULT_HEIGHT = "1500";
	public static final String DEFAULT_RESOLUTION = "300";

	AnalyticsService analyticsService;

	public AnalyticsController(AnalyticsService analyticsService) {
		this.analyticsService = analyticsService;
	}

	@OpenApi(summary = "Evaluate expression", path = ENDPOINT_EVALUATE, method = POST, tags = {
			ANALYTICS }, queryParams = {
					@OpenApiParam(name = PARAM_VARIABLE, type = String.class, description = "Target variable, default: output", allowEmptyValue = false, required = false),
					@OpenApiParam(name = PARAM_TYPE, type = String.class, description = "Type description, values: [plain, image, document], default: plain", allowEmptyValue = false, required = false) }, requestBody = @OpenApiRequestBody(content = {
							@OpenApiContent(from = String.class) }, description = "Evaluate Expression", required = true), responses = {
									@OpenApiResponse(status = "200"), @OpenApiResponse(status = "204"),
									@OpenApiResponse(status = "400", content = {
											@OpenApiContent(from = ErrorResponse.class) }) })
	public void evaluate(Context ctx) {
		Map<String, List<String>> errors = CommonValidator.validate(ctx);
		if (MapUtils.isEmpty(errors)) {
			final boolean debug = BooleanUtils.toBoolean(ctx.queryParam(DEBUG));
			final String expression = ctx.body();
			final String type = StringUtils.defaultIfEmpty(ctx.queryParam(PARAM_TYPE), TYPE_PLAIN);
			final String variable = StringUtils.defaultIfEmpty(ctx.queryParam(PARAM_VARIABLE), OUTPUT);

			if (Utils.isDevMode() && debug) {
				Map<String, String> debugExpression = new HashMap<>();
				debugExpression.put(PARAM_TYPE, type);
				debugExpression.put(PARAM_VARIABLE, variable);
				debugExpression.put(EXPRESSION, expression);
				ctx.json(debugExpression);
			} else {
				if (StringUtils.isNotEmpty(expression)) {
					Map<String, String> preParameters = new HashMap<>();
					Map<String, String> postParameters = new HashMap<>();
					if (StringUtils.equals(type, TYPE_IMAGE)) {
						final int width = Integer
								.valueOf(StringUtils.defaultIfEmpty(ctx.queryParam(PARAM_WIDTH), DEFAULT_WIDTH));
						final int height = Integer
								.valueOf(StringUtils.defaultIfEmpty(ctx.queryParam(PARAM_HEIGHT), DEFAULT_HEIGHT));
						final int resolution = Integer.valueOf(
								StringUtils.defaultIfEmpty(ctx.queryParam(PARAM_RESOLUTION), DEFAULT_RESOLUTION));
						byte[] bytes = analyticsService.getImage(FileExtension.PNG, expression, preParameters, width,
								height, resolution);
						String prefix = MimeTypes.getMimeType(FileExtension.PNG);
						ctx.contentType(prefix);
						ctx.status(200);
						ctx.result(bytes);
					} else if (StringUtils.equals(type, TYPE_DOCUMENT)) {
						byte[] bytes = analyticsService.getDocument(FileExtension.RNW, FileExtension.PDF, expression,
								preParameters);
						String prefix = MimeTypes.getMimeType(FileExtension.PDF);
						ctx.contentType(prefix);
						ctx.status(200);
						ctx.result(bytes);
					} else {
						byte[] bytes = analyticsService.getText(FileExtension.JSON, expression, variable, preParameters,
								postParameters);
						String output = new String(bytes);
						if (StringUtils.isNotEmpty(output)) {
							ctx.status(200);
							ctx.result(output);
						} else {
							ctx.status(204);
						}
					}
				} else {
					ctx.status(204);
				}
			}
		} else {
			ctx.status(400);
			ctx.json(errors);
		}
	}
}
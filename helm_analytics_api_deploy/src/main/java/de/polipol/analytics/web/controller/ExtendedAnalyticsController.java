package de.polipol.analytics.web.controller;

import static de.polipol.analytics.commons.Constants.ANALYTICS;
import static de.polipol.analytics.commons.Constants.DEBUG;
import static de.polipol.analytics.commons.Constants.DIRECTORY;
import static de.polipol.analytics.commons.Constants.ENDPOINT_EVALUATE;
import static de.polipol.analytics.commons.Constants.EXPRESSION;
import static de.polipol.analytics.commons.Constants.FILENAME;
import static de.polipol.analytics.commons.Constants.INPUT;
import static de.polipol.analytics.commons.Constants.OUTPUT;
import static de.polipol.analytics.commons.Constants.ROLE;
import static de.polipol.analytics.commons.Constants.TYPE_DOCUMENT;
import static de.polipol.analytics.commons.Constants.TYPE_IMAGE;
import static de.polipol.analytics.commons.Constants.TYPE_PLAIN;
import static io.javalin.plugin.openapi.annotations.HttpMethod.POST;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.auth.CustomAuthorizer;
import de.polipol.analytics.commons.MimeTypes;
import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.connect.AnalyticsService;
import de.polipol.analytics.file.FileExtension;
import de.polipol.analytics.file.FileService;
import de.polipol.analytics.model.File;
import de.polipol.analytics.web.CommonValidator;
import de.polipol.analytics.web.ErrorResponse;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;

public final class ExtendedAnalyticsController {

	public static final String PARAM_TYPE = "type";
	public static final String PARAM_VARIABLE = "variable";
	public static final String PARAM_DEBUG = "debug";

	public static final String PARAM_WIDTH = "width";
	public static final String PARAM_HEIGHT = "height";
	public static final String PARAM_RESOLUTION = "resolution";

	public static final String DEFAULT_WIDTH = "2000";
	public static final String DEFAULT_HEIGHT = "1500";
	public static final String DEFAULT_RESOLUTION = "300";

	AnalyticsService analyticsService;
	FileService fileService;

	public ExtendedAnalyticsController(AnalyticsService analyticsService, FileService fileService) {
		this.analyticsService = analyticsService;
		this.fileService = fileService;
	}

	@OpenApi(summary = "Evaluate expression", path = ENDPOINT_EVALUATE, method = POST, tags = {
			ANALYTICS }, requestBody = @OpenApiRequestBody(content = {
					@OpenApiContent(from = String.class) }), responses = { @OpenApiResponse(status = "204"),
							@OpenApiResponse(status = "400", content = {
									@OpenApiContent(from = ErrorResponse.class) }) })
	public void evaluate(Context ctx) {
		Map<String, List<String>> errors = CommonValidator.validate(ctx);
		if (MapUtils.isEmpty(errors)) {
			final boolean debug = BooleanUtils.toBoolean(ctx.queryParam(DEBUG));
			final String role = CustomAuthorizer.getUsername(ctx);
			final String filename = ctx.queryParam(FILENAME, EMPTY);
			final String directory = StringUtils.defaultIfEmpty(ctx.queryParam(DIRECTORY), EMPTY);
			final String type = StringUtils.defaultIfEmpty(ctx.queryParam(PARAM_TYPE), TYPE_PLAIN);
			final String variable = StringUtils.defaultIfEmpty(ctx.queryParam(PARAM_VARIABLE), OUTPUT);
			final String input = ctx.body();

			Optional<File> file = this.fileService.find(directory, role, filename, false);

			if (Utils.isDevMode() && debug) {
				Map<String, String> debugExpression = new HashMap<>();
				debugExpression.put(PARAM_TYPE, type);
				debugExpression.put(PARAM_VARIABLE, variable);
				debugExpression.put(DIRECTORY, directory);
				debugExpression.put(FILENAME, filename);
				debugExpression.put(INPUT, input);
				if (file.isPresent()) {
					String expression = new String(file.get().getContent(), StandardCharsets.UTF_8);
					debugExpression.put(EXPRESSION, expression);
				} else {
				}
				debugExpression.put(ROLE, role);
				ctx.status(200);
				ctx.json(debugExpression);
			} else {
				if (file.isPresent()) {
					String expression = new String(file.get().getContent(), StandardCharsets.UTF_8);
					if (StringUtils.isNotEmpty(expression)) {
						Map<String, String> preParameters = new HashMap<>();
						Map<String, String> postParameters = new HashMap<>();
						preParameters.put(INPUT, input);

						if (StringUtils.equals(type, TYPE_IMAGE)) {
							final int width = Integer
									.valueOf(StringUtils.defaultIfEmpty(ctx.queryParam(PARAM_WIDTH), DEFAULT_WIDTH));
							final int height = Integer
									.valueOf(StringUtils.defaultIfEmpty(ctx.queryParam(PARAM_HEIGHT), DEFAULT_HEIGHT));
							final int resolution = Integer.valueOf(
									StringUtils.defaultIfEmpty(ctx.queryParam(PARAM_RESOLUTION), DEFAULT_RESOLUTION));
							byte[] bytes = analyticsService.getImage(FileExtension.PNG, expression, preParameters,
									width, height, resolution);
							String prefix = MimeTypes.getMimeType(FileExtension.PNG);
							ctx.contentType(prefix);
							ctx.status(200);
							ctx.result(bytes);
						} else if (StringUtils.equals(type, TYPE_DOCUMENT)) {
							byte[] bytes = analyticsService.getDocument(FileExtension.RNW, FileExtension.PDF,
									expression, preParameters);
							String prefix = MimeTypes.getMimeType(FileExtension.PDF);
							ctx.contentType(prefix);
							ctx.status(200);
							ctx.result(bytes);
						} else {
							byte[] bytes = analyticsService.getText(FileExtension.JSON, expression, variable,
									preParameters, postParameters);
							String output = new String(bytes);
							if (StringUtils.isEmpty(output)) {
								ctx.status(204);
							} else {
								ctx.status(200);
								ctx.result(output);
							}
						}
					}
				}
			}
		}
	}
}

package de.polipol.analytics.web.controller;

import static de.polipol.analytics.commons.Constants.ENDPOINT_INTERPRETERS;
import static de.polipol.analytics.commons.Constants.INTERPRETER;
import static io.javalin.plugin.openapi.annotations.HttpMethod.GET;

import java.util.List;

import de.polipol.analytics.model.Interpreter;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;

public final class InterpreterController {

	List<Interpreter> interpreters;

	public InterpreterController(List<Interpreter> interpreters) {
		this.interpreters = interpreters;
	}

	@OpenApi(summary = "Get interpreters", path = ENDPOINT_INTERPRETERS, method = GET, tags = {
			INTERPRETER }, responses = {
					@OpenApiResponse(status = "200", content = { @OpenApiContent(from = Interpreter[].class) }) })
	public void getAll(Context ctx) {
		ctx.status(200);
		ctx.json(interpreters);
	}
}
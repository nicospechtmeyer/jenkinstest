package de.polipol.analytics.web.controller;

import static de.polipol.analytics.commons.Constants.ENDPOINT_ENCRYPT;
import static de.polipol.analytics.commons.Constants.ENDPOINT_INFOS;
import static de.polipol.analytics.commons.Constants.INFO;
import static de.polipol.analytics.commons.Messages.MESSAGE_CURRENT_TIME_MS;
//import static de.polipol.analytics.commons.Messages.MESSAGE_ENCRYPTED_DATA;
import static io.javalin.plugin.openapi.annotations.HttpMethod.GET;
import static io.javalin.plugin.openapi.annotations.HttpMethod.POST;

import java.util.HashMap;
import java.util.Map;

import de.polipol.analytics.commons.props.PropertiesEncryptorFactory;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;

public final class InfoController {

	@OpenApi(summary = "Check pulse", path = ENDPOINT_INFOS, method = GET, tags = { INFO }, responses = {
			@OpenApiResponse(status = "200") })
	public void getPulse(Context ctx) {
		ctx.status(200);
		Map<String, Long> output = new HashMap<>();
		output.put(MESSAGE_CURRENT_TIME_MS, System.currentTimeMillis());
		ctx.json(output);
	}

	@OpenApi(summary = "Encrypt data", path = ENDPOINT_ENCRYPT, method = POST, tags = { INFO }, responses = {
			@OpenApiResponse(status = "200") })
	public void encrypt(Context ctx) {
		String data = ctx.body();
		ctx.status(200);
		String encryptedData = PropertiesEncryptorFactory.getEncryptor().encrypt(data);
//		Map<String, String> output = new HashMap<>();
//		output.put(MESSAGE_ENCRYPTED_DATA, PropertiesEncryptorFactory.getEncryptor().encrypt(data));
		ctx.result(encryptedData);
//		ctx.json(output);
	}

//	@OpenApi(summary = "Get security infos", path = ENDPOINT_INFOS, method = GET, tags = { INFO }, responses = {
//			@OpenApiResponse(status = "200") })
//	public void getSecurity(Context ctx) {
//		HttpResponse<JsonNode> jsonResponse;
//		try {
//			ctx.status(200);
//			jsonResponse = Unirest.get(System.getenv(AUTH_HOST) + REALMS + System.getenv(AUTH_REALM)).asJson();
//			ctx.json(jsonResponse.getBody().toString());
//		} catch (UnirestException exception) {
//			exception.printStackTrace();
//		}
//	}

//	@OpenApi(summary = "Get security infos", path = ENDPOINT_INFOS, method = GET, tags = { INFO }, responses = {
//			@OpenApiResponse(status = "200") })
//	public void test(Context ctx) {
//		HttpResponse<JsonNode> jsonResponse;
//		try {
//			ctx.status(200);
//			jsonResponse = Unirest.get(System.getenv(AUTH_HOST) + REALMS + System.getenv(AUTH_REALM)).asJson();
//			ctx.json(jsonResponse.getBody().toString());
//		} catch (UnirestException exception) {
//			exception.printStackTrace();
//		}
//	}
}
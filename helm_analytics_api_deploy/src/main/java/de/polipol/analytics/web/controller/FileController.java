package de.polipol.analytics.web.controller;

import static de.polipol.analytics.commons.Constants.DIRECTORY;
import static de.polipol.analytics.commons.Constants.DIRECTORY_DESC;
import static de.polipol.analytics.commons.Constants.ENDPOINT_FILES;
import static de.polipol.analytics.commons.Constants.FILE;
import static de.polipol.analytics.commons.Constants.FILENAME;
import static de.polipol.analytics.commons.Constants.FILENAME_DESC;
import static de.polipol.analytics.commons.Constants.UPLOADED_FILES_KEY;
import static de.polipol.analytics.commons.Constants.UPLOADED_FILE_KEY;
import static io.javalin.plugin.openapi.annotations.HttpMethod.DELETE;
import static io.javalin.plugin.openapi.annotations.HttpMethod.GET;
import static io.javalin.plugin.openapi.annotations.HttpMethod.POST;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.polipol.analytics.auth.CustomAuthorizer;
import de.polipol.analytics.commons.MimeTypes;
import de.polipol.analytics.file.FileService;
import de.polipol.analytics.model.File;
import de.polipol.analytics.web.ErrorResponse;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiFileUpload;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;

public final class FileController {

	FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	@OpenApi(summary = "Upload files", path = ENDPOINT_FILES, method = POST, tags = { FILE }, fileUploads = {
			@OpenApiFileUpload(name = UPLOADED_FILE_KEY),
			@OpenApiFileUpload(name = UPLOADED_FILES_KEY, isArray = true) }, responses = {
					@OpenApiResponse(status = "200"),
					@OpenApiResponse(status = "400", content = { @OpenApiContent(from = ErrorResponse.class) }) })
	public void create(Context ctx) throws IOException {
		final String role = CustomAuthorizer.getUsername(ctx);
		final String directory = ctx.queryParam(DIRECTORY);
		try {
			UploadedFile uploadedFile = ctx.uploadedFile(UPLOADED_FILE_KEY);
			if (Optional.of(uploadedFile).isPresent()) {
				fileService.upsert(directory, role, uploadedFile.getFilename(), uploadedFile.getContent());
			}
		} catch (IOException exception) {
			ctx.status(400);
		}
		ctx.uploadedFiles(UPLOADED_FILES_KEY).forEach(file -> {
			try {
				fileService.upsert(directory, file.getFilename(), role, file.getContent());
			} catch (IOException exception) {
				ctx.status(400);
			}
		});
	}

	@OpenApi(summary = "Delete file by name", path = ENDPOINT_FILES + "/:" + FILENAME, method = DELETE, pathParams = {
			@OpenApiParam(name = FILENAME, type = String.class, description = FILENAME_DESC) }, queryParams = {
					@OpenApiParam(name = DIRECTORY, allowEmptyValue = true, required = false, type = String.class, description = DIRECTORY_DESC) }, tags = {
							FILE }, responses = { @OpenApiResponse(status = "200"),
									@OpenApiResponse(status = "400", content = {
											@OpenApiContent(from = ErrorResponse.class) }) })
	public void delete(Context ctx) {
		final String filename = ctx.queryParam(FILENAME);
		final String directory = ctx.queryParam(DIRECTORY);
		final String role = CustomAuthorizer.getUsername(ctx);
		try {
			fileService.delete(directory, role, filename);
		} catch (Exception exception) {
			ctx.status(404);
		}
	}

	@OpenApi(summary = "Get files", path = ENDPOINT_FILES, method = GET, queryParams = {
			@OpenApiParam(name = DIRECTORY, allowEmptyValue = true, required = false, type = String.class, description = DIRECTORY_DESC) }, tags = {
					FILE }, responses = {
							@OpenApiResponse(status = "200", content = { @OpenApiContent(from = File[].class) }) })
	public void getAll(Context ctx) {
		final String role = CustomAuthorizer.getUsername(ctx);
		final String directory = ctx.queryParam(DIRECTORY);
		try {
			ctx.status(200);
			ctx.json(fileService.getAll(directory, role));
		} catch (IOException exception) {
			ctx.status(404);
		}
	}

	@OpenApi(summary = "Get file by name", path = ENDPOINT_FILES + "/:" + FILENAME, method = GET, pathParams = {
			@OpenApiParam(name = FILENAME, type = String.class, description = FILENAME_DESC) }, queryParams = {
					@OpenApiParam(name = DIRECTORY, allowEmptyValue = true, required = false, type = String.class, description = DIRECTORY_DESC) }, tags = {
							FILE }, responses = { @OpenApiResponse(status = "200"),
									@OpenApiResponse(status = "400", content = {
											@OpenApiContent(from = ErrorResponse.class) }) })
	public void getOne(Context ctx) throws IOException {
		final String role = CustomAuthorizer.getUsername(ctx);
		final String filename = ctx.queryParam(FILENAME);
		final String directory = ctx.queryParam(DIRECTORY);
		Optional<File> file = fileService.find(directory, role, filename, true);
		if (file.isPresent()) {
//			ctx.header(CONTENT_DISPOSITION, "attachment; filename=" + filename);
			ctx.contentType(MimeTypes.getMimeType(filename));
			ctx.status(200);
			ctx.result(file.get().getContent());
		} else {
			ctx.status(404);
		}
	}

	@OpenApi(summary = "Get file by name and filter description", path = ENDPOINT_FILES, method = POST, tags = {
			FILE }, requestBody = @OpenApiRequestBody(content = { @OpenApiContent(from = String.class) }), responses = {
					@OpenApiResponse(status = "204"),
					@OpenApiResponse(status = "400", content = { @OpenApiContent(from = ErrorResponse.class) }) })
	public void getOneByFilter(Context ctx) throws IOException {
		final String filename = ctx.queryParam(FILENAME);
		final String directory = ctx.queryParam(DIRECTORY);
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

		Optional<File> file = fileService.find(FileService.getDirectoryString(directory, variables), role, filename,
				true);
		if (file.isPresent()) {
//			ctx.header(CONTENT_DISPOSITION, "attachment; filename=" + filename);
			ctx.contentType(MimeTypes.getMimeType(filename));
			ctx.status(200);
			ctx.result(file.get().getContent());
		} else {
			ctx.status(404);
		}
	}
}
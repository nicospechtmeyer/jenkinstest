package de.polipol.analytics.web;

import static de.polipol.analytics.commons.Constants.ENDPOINT_REDOC;
import static de.polipol.analytics.commons.Constants.ENDPOINT_SWAGGER_JSON;
import static de.polipol.analytics.commons.Constants.ENDPOINT_SWAGGER_UI;
import static de.polipol.analytics.commons.Constants.SWAGGER_ANNOTATION_SCANNING_FOLDER;
import static de.polipol.analytics.commons.Constants.SWAGGER_DESC;
import static de.polipol.analytics.commons.Constants.SWAGGER_FILE_TITLE;
import static de.polipol.analytics.commons.Constants.SWAGGER_VERSION;

import java.util.List;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.javalin.JavalinWebContext;

import io.javalin.http.Context;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

public abstract class AbstractMicroServer {

	public static OpenApiPlugin getConfiguredOpenApiPlugin() {
		Info info = new Info().version(SWAGGER_VERSION).title(SWAGGER_FILE_TITLE).description(SWAGGER_DESC);
		OpenApiOptions options = new OpenApiOptions(info)
				.activateAnnotationScanningFor(SWAGGER_ANNOTATION_SCANNING_FOLDER).path(ENDPOINT_SWAGGER_JSON)
				.swagger(new SwaggerOptions(ENDPOINT_SWAGGER_UI)).reDoc(new ReDocOptions(ENDPOINT_REDOC))
				.defaultDocumentation(doc -> {
					doc.json("500", ErrorResponse.class);
					doc.json("503", ErrorResponse.class);
				});
		return new OpenApiPlugin(options);
	}

	public static List<CommonProfile> getProfiles(Context ctx) {
		return new ProfileManager<CommonProfile>(new JavalinWebContext(ctx)).getAll(true);
	}
}

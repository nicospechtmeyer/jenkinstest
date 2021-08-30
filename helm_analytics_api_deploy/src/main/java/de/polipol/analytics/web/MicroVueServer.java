package de.polipol.analytics.web;

import static de.polipol.analytics.commons.Constants.ENDPOINT_INFOS;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Optional;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.vue.VueComponent;

public final class MicroVueServer {

	public static void start(int port) {

		Optional<Javalin> app = AppFactory.createVueApp();
		if (app.isPresent()) {
			app.get().get(ENDPOINT_INFOS, new VueComponent("<overview></overview>"));
			app.get().start(port).error(404, ctx -> {
				ctx.result(EMPTY);
			});
		}
	}
}
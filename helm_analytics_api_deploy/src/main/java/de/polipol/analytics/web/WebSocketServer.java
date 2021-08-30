package de.polipol.analytics.web;

import static de.polipol.analytics.commons.Constants.ENDPOINT_WEBSOCKET;

import java.util.Optional;

import io.javalin.Javalin;

public final class WebSocketServer {

	public static void start(int port) {

		Optional<Javalin> app = AppFactory.createApp();
		if (app.isPresent()) {
			app.get().ws(ENDPOINT_WEBSOCKET, ws -> {
				// ws.onConnect(ctx -> ...);
			}).start(port);
		}
	}
}
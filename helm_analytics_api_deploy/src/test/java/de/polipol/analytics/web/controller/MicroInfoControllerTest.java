package de.polipol.analytics.web.controller;

//import static de.polipol.analytics.commons.Messages.MESSAGE_CURRENT_TIME_MS;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
//import org.mockito.ArgumentMatchers;

import io.javalin.http.Context;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MicroInfoControllerTest {

	private Context ctx = mock(Context.class);

	@Test
	public void GET_PULSE() {
		new InfoController().getPulse(ctx);
//		verify(ctx).json(ArgumentMatchers.startsWith(MESSAGE_CURRENT_TIME_MS));
		verify(ctx).status(200);
	}
}
package de.polipol.analytics.web;

import static de.polipol.analytics.commons.Constants.DEBUG;
import static de.polipol.analytics.commons.Constants.FILENAME;
import static de.polipol.analytics.commons.Messages.MESSAGE_FILENAME_NOT_VALID;
import static de.polipol.analytics.commons.Messages.MESSAGE_INVALID_VALUE;
import static de.polipol.analytics.commons.Messages.MESSAGE_TYPE_NOT_SUPPORTED;
import static de.polipol.analytics.commons.Constants.TYPE;
import static de.polipol.analytics.commons.Constants.TYPE_DOCUMENT;
import static de.polipol.analytics.commons.Constants.TYPE_IMAGE;
import static de.polipol.analytics.commons.Constants.TYPE_PLAIN;

import java.util.List;
import java.util.Map;

import io.javalin.core.validation.Validator;
import io.javalin.http.Context;

public class CommonValidator {

	public static Map<String, List<String>> validate(Context ctx) {

		Validator<String> fileNameValidator = ctx.queryParam(FILENAME, String.class)
				.check(n -> n.contains(".") && n.length() > 3, MESSAGE_FILENAME_NOT_VALID);

		Validator<String> typeValidator = ctx.queryParam(TYPE, String.class).check(
				n -> n.equals(TYPE_PLAIN) || n.equals(TYPE_IMAGE) || n.equals(TYPE_DOCUMENT),
				MESSAGE_TYPE_NOT_SUPPORTED);
		
		Validator<String> debugValidator = ctx.queryParam(DEBUG, String.class).check(
				n -> n.equals(Boolean.toString(true)) || n.equals(Boolean.toString(false)), MESSAGE_INVALID_VALUE);

		Map<String, List<String>> errors = Validator.collectErrors(fileNameValidator, typeValidator, debugValidator);
		return errors;
	}
}
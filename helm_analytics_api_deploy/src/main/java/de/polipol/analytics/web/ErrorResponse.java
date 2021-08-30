package de.polipol.analytics.web;

import static de.polipol.analytics.commons.Messages.MESSAGE_DEFAULT;

public final class ErrorResponse {

	String message;
	int status;

	public ErrorResponse() {
		this.message = MESSAGE_DEFAULT;
		this.status = 400;
	}

	public ErrorResponse(final Exception exception) {
		this.message = exception.getCause().getLocalizedMessage();
		this.status = 400;
	}

	public ErrorResponse(final String message) {
		this.message = message;
		this.status = 400;
	}

	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
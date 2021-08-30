package de.polipol.analytics.exception;

@SuppressWarnings("serial")
public final class ContainerNotDefinedException extends Exception {

	public ContainerNotDefinedException() {
		super();
	}

	public ContainerNotDefinedException(final String e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

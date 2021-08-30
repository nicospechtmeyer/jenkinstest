package de.polipol.analytics.connect.data.semantic;

import java.lang.reflect.InvocationTargetException;

import de.polipol.analytics.exception.InvalidReasonerException;

public abstract class ReasonerFactory {

	@SuppressWarnings("unchecked")
	public static Reasoner createDefaultReasoner() throws InvalidReasonerException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<Reasoner> reasonerClass;
		Reasoner reasoner;
		try {
			reasonerClass = (Class<Reasoner>) Class.forName(DefaultDictionary.DEFAULT_REASONER_CLASS);
			reasoner = reasonerClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException exception) {
			throw new InvalidReasonerException(exception.toString());
		}
		return reasoner;
	}
}

package de.polipol.analytics.connect.data.semantic;

public interface Reasoner {

	boolean isEquivalent(Label supLabel, Label subLabel);

	boolean isSubsumedBy(Label suplabel, Label sublabel);
}

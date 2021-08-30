package de.polipol.analytics.connect.data.semantic;

import org.apache.commons.lang3.StringUtils;

public final class SimpleReasoner implements Reasoner {

	@Override
	public boolean isEquivalent(final Label supLabel, final Label subLabel) {
		return isSubsumedBy(supLabel, subLabel) && isSubsumedBy(subLabel, supLabel) ? true : false;
	}

	@Override
	public boolean isSubsumedBy(final Label supLabel, final Label subLabel) {
		if (StringUtils.equalsIgnoreCase(supLabel.getMeaning(), subLabel.getMeaning())) {
			for (String contextKey : subLabel.getContext().keySet()) {
				if (!supLabel.getContext().containsKey(contextKey)) {
					return false;
				} else {
					if (!StringUtils.equalsIgnoreCase(supLabel.getContext().get(contextKey),
							subLabel.getContext().get(contextKey))) {
						return false;
					}
				}
			}
		} else {
			return false;
		}
		return true;
	}
}

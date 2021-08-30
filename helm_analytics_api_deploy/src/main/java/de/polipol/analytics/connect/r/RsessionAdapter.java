package de.polipol.analytics.connect.r;

import static de.polipol.analytics.connect.r.RKeywords.UNDEFINED_RESULT;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.apache.commons.lang3.StringUtils;
import org.math.R.RserveSession;
import org.math.R.Rsession;

import de.polipol.analytics.exception.AnalyticsException;
import de.polipol.analytics.exception.EmptyResultException;
import de.polipol.analytics.exception.NoActiveSessionException;

public final class RsessionAdapter extends AbstractRAdapter {

	private Rsession rsession;

	public RsessionAdapter(final Rsession rsession) {
		this.rsession = rsession;
	}

	@Override
	public void clearWorkspace() throws AnalyticsException {
		this.rsession.rmAll();
	}

	@Override
	public String[] eval(final String expression) throws AnalyticsException {
		String[] output = {};
		if (this.isActive()) {
			if (StringUtils.isNotEmpty(expression) || !StringUtils.equalsIgnoreCase(expression, UNDEFINED_RESULT)) {
				if (!isActive())
					throw new NoActiveSessionException();
				try {
					Object result = rsession.eval(expression, false);
					output = new String[] { result.toString() };
				} catch (Exception exception) {
					throw new EmptyResultException(getLastError());
				}
			}
		}
		return output;
	}

	public Rsession getConnection() {
		return rsession;
	}

	@Override
	public String getLastError() {
		String errorMessage = rsession.getLastError();
		char char1 = (char) 94;
		if (errorMessage.contains("Fehler in source")) {
			errorMessage = errorMessage.substring(StringUtils.ordinalIndexOf(errorMessage, ":", 4) + 1)
					.replaceAll("1: output <- NULL", EMPTY).replaceAll("\n", EMPTY)
					.replace(Character.toString(char1), EMPTY).trim();
		}
		return errorMessage;
	}

	private boolean isActive() {
		if (rsession != null) {
			if (((RserveSession) rsession).connected) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void loadPackage(final String[] rPackage) throws AnalyticsException {
		this.eval("library(" + rPackage[0] + ")");
		// rsession.loadPackage(rPackage[0]);
	}
}
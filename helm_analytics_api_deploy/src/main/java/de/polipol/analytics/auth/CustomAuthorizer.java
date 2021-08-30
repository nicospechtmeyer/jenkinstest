package de.polipol.analytics.auth;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.List;

import org.pac4j.core.authorization.authorizer.ProfileAuthorizer;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.oidc.profile.OidcProfile;

import de.polipol.analytics.commons.Utils;
import de.polipol.analytics.web.AbstractMicroServer;
import io.javalin.http.Context;

public final class CustomAuthorizer extends ProfileAuthorizer<CommonProfile> {

	public static String getUsername(final Context ctx) {
		String username = EMPTY;
		if (Utils.isSecured()) {
			try {
				OidcProfile profile = (OidcProfile) AbstractMicroServer.getProfiles(ctx).get(0);
				username = profile.getUsername();
			} catch (Exception exception) {
			}
		}
		return username;
	}

	@Override
	public boolean isAuthorized(final WebContext context, final List<CommonProfile> profiles) throws HttpAction {
		return isAnyAuthorized(context, profiles);
	}

	@Override
	public boolean isProfileAuthorized(final WebContext context, final CommonProfile profile) {
		if (profile == null) {
			return false;
		}
		return true;
	}
}

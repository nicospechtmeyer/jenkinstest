package de.polipol.analytics.auth;

import static de.polipol.analytics.commons.Constants.AUTH_CLIENT_ID;
import static de.polipol.analytics.commons.Constants.AUTH_HOST;
import static de.polipol.analytics.commons.Constants.AUTH_REALM;
import static de.polipol.analytics.commons.Constants.AUTH_SECRET;
import static de.polipol.analytics.commons.Constants.SECURITY_AUTHORIZER;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.http.client.direct.HeaderClient;
import org.pac4j.oidc.config.KeycloakOidcConfiguration;
import org.pac4j.oidc.credentials.authenticator.UserInfoOidcAuthenticator;

import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod;

import de.polipol.analytics.commons.Utils;

public final class KeycloakConfigFactory implements ConfigFactory {

	private static final String BEARER = "Bearer";
	private static final String AUTHORIZATION = "Authorization";

	@Override
	public Config build(Object... parameters) {

		String host = Utils.getEnv(AUTH_HOST, EMPTY);
		String clientId = Utils.getEnv(AUTH_CLIENT_ID, EMPTY);
		String realm = Utils.getEnv(AUTH_REALM, EMPTY);
		String secret = Utils.getEnv(AUTH_SECRET, EMPTY);
		// String callback = Utils.getEnv(AUTH_CALLBACK, EMPTY);

		if (StringUtils.isNotEmpty(host)) {
			KeycloakOidcConfiguration oidcConfig = new KeycloakOidcConfiguration();
			oidcConfig.setClientId(clientId);
			oidcConfig.setRealm(realm);
			oidcConfig.setBaseUri(host);
			// secret cannot be blank
			if (StringUtils.isNotEmpty(secret)) {
				oidcConfig.setSecret(secret);
			} else {
				oidcConfig.setSecret(AUTH_SECRET.toString());
			}
			oidcConfig.setClientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT);

			UserInfoOidcAuthenticator authenticator = new UserInfoOidcAuthenticator(oidcConfig);
			HeaderClient headerClient = new HeaderClient(AUTHORIZATION, BEARER, authenticator);
			Config config = new Config(headerClient);
			config.addAuthorizer(SECURITY_AUTHORIZER, new CustomAuthorizer());
			// config.addMatcher(ALLOW_AJAX_REQUESTS, new CorsMatcher());
			return config;
		}
		return null;
	}
}
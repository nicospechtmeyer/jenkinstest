package de.polipol.analytics.commons.props;

import static de.polipol.analytics.commons.Constants.PLATFORM_ENCRYPTION_KEY;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

public abstract class PropertiesEncryptorFactory {

	public static StringEncryptor getEncryptor() {
		EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setConfig(config);
		config.setPasswordEnvName(PLATFORM_ENCRYPTION_KEY);
		return encryptor;
	}
}
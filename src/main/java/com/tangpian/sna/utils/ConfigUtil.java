package com.tangpian.sna.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigUtil {
	private static final Logger log = Logger.getLogger(ConfigUtil.class
			.getName());
	private static final String CONFIG_PROPERTIES = "/config.properties";
	public static final Properties config = getConfig();

	/**
	 * Load the configuration file for this application.
	 * 
	 * @return application configuration properties
	 */
	private static Properties getConfig() {
		InputStream input = ConfigUtil.class.getResourceAsStream(CONFIG_PROPERTIES);
		Properties config = new Properties();
		try {
			config.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Unable to load config file: "
					+ CONFIG_PROPERTIES);
		}
		return config;
	}

	/**
	 * A simple static helper method that fetches a configuration
	 * 
	 * @param key
	 *            The name of the property for which you would like the
	 *            configured value
	 * @return A String representation of the configured property value
	 * @throws RuntimeException
	 *             if request property can not be found
	 */
	public static String getProperty(String key) {
		if (!config.containsKey(key)
				|| config.getProperty(key).trim().isEmpty()) {
			log.severe("Could not find property " + key);
			throw new RuntimeException("Could not find property " + key);
		}
		return config.getProperty(key).trim();
	}

	public static void main(String[] args) {
		System.out.println(ConfigUtil.getProperty("oauth_client_id"));
	}
}

package com.tangpian.sna.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
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
		InputStream input = ConfigUtil.class
				.getResourceAsStream(CONFIG_PROPERTIES);
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

	private static Map<String, Double> trendencyWordMap;

	public static Map<String, Double> initTrendencyWordMap() {
		if (null == trendencyWordMap) {
			trendencyWordMap = new HashMap<String, Double>();

			try {
				InputStream input = ConfigUtil.class.getClassLoader()
						.getResourceAsStream("trendency.map");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(input, "UTF-8"));
				String line;
				while ((line = reader.readLine()) != null) {
					String[] entry = line.split("\\|");
					log.info("trendencyWordMap entry:" + entry[0] + "|"
							+ entry[1]);
					trendencyWordMap
							.put(entry[0], Double.parseDouble(entry[1]));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return trendencyWordMap;
	}
}

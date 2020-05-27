package com.lekkimworld.javawebapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Environment {
    public static final String WAF_ENV = "waf.env";

    private Properties properties = new Properties();
	private String name;
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public Properties getProperties() {
		return properties;
	}

	public Environment() {
		try {
			String fileName = "/META-INF/env/common.properties";
			InputStream inStream = getClass().getResourceAsStream(fileName);
			if (inStream == null) {
				System.err.println(fileName + " not found");
			} else {
				properties.load(inStream);
				name = System.getProperty(WAF_ENV);
				if (name == null) {
					System.err.println("No environment (-Dwaf.env=...) configured");
				} else {	
					properties.load(getClass().getResourceAsStream("/META-INF/env/" + name + ".properties"));
				}
                properties.putAll(System.getProperties());
                
				// sync environment and system properties.
				System.getProperties().putAll(properties);
			}
		} catch (IOException e) {
			System.err.println("Unable to load JITWU environment");
		}
	}

	public String getName() {
		return name;
	}

	public Boolean getBoolean(String key) {
		if (isDefined(key)) {
			return Boolean.valueOf(getProperty(key));
		}
		return null;
	}

	public boolean getBoolean(String key, boolean defaultVal) {
		Boolean val = getBoolean(key);
		return val == null ? defaultVal : val;
	}

	public Integer getInteger(String key) {
		if (isDefined(key)) {
			try {
				return Integer.valueOf(getProperty(key));
			} catch (NumberFormatException e) {
				System.err.println("Environment key: " + key + " is not an integer: " + getProperty(key));
				e.printStackTrace();
			}
		}
		return null;
	}

	public int getInteger(String key, int defaultVal) {
		Integer val = getInteger(key);
		return val == null ? defaultVal : val;
	}

	public boolean isDefined(String key) {
		return properties.containsKey(key);
	}

	public boolean isDevelopment() {
		return name.equals("dev");
	}
}

package com.ecommerce.utilities;

import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Logger log = LoggerUtil.getLogger(ConfigReader.class);
    private static Properties properties;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            FileInputStream fis = new FileInputStream(
                    "src/test/resources/config.properties"
            );
            properties = new Properties();
            properties.load(fis);
            fis.close();
            log.info("Config file loaded successfully.");
        } catch (IOException e) {
            log.error("Failed to load config.properties: {}", e.getMessage());
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            log.error("Property '{}' not found in config.properties", key);
            throw new RuntimeException("Property '" + key + "' not found.");
        }
        log.debug("Config read: {} = {}", key, value);
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
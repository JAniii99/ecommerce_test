package com.ecommerce.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Reads values from config.properties file.
 * This is a SINGLETON pattern - only one instance exists in the entire framework.
 * Why? Because we only need to read the file ONCE, not every time a test runs.
 */
public class ConfigReader {

    // The Properties object that holds all key=value pairs from config.properties
    private static Properties properties;

    // Static block runs ONCE when the class is first loaded by JVM
    // Perfect place to load the properties file
    static {
        loadProperties();
    }

    /**
     * Reads the config.properties file and loads it into memory.
     * Called automatically when class loads - you never call this manually.
     */
    private static void loadProperties() {
        try {
            // FileInputStream opens the file for reading
            // The path is relative to your project root
            FileInputStream fis = new FileInputStream(
                    "src/test/resources/config.properties"
            );

            // Initialize the Properties object
            properties = new Properties();

            // Load all key=value pairs from the file into memory
            properties.load(fis);

            // Always close streams after use - prevents memory leaks
            fis.close();

            System.out.println("✅ Config file loaded successfully.");

        } catch (IOException e) {
            // If file not found or unreadable, stop everything
            // We cannot run tests without configuration
            throw new RuntimeException(
                    "❌ Failed to load config.properties file. " +
                            "Check the file path: src/test/resources/config.properties", e
            );
        }
    }

    /**
     * Gets a value from config.properties by its key.
     *
     * Usage example:
     *   ConfigReader.get("base.url")  → returns "https://automationexercise.com"
     *   ConfigReader.get("browser")   → returns "chrome"
     *
     * @param key - the property name (left side of = in config file)
     * @return    - the property value (right side of = in config file)
     */
    public static String get(String key) {
        String value = properties.getProperty(key);

        // If key doesn't exist, fail fast with a clear message
        // Better than a NullPointerException later
        if (value == null) {
            throw new RuntimeException(
                    "❌ Property '" + key + "' not found in config.properties. " +
                            "Please add it to the file."
            );
        }

        return value.trim(); // trim() removes accidental spaces
    }

    /**
     * Gets a value as integer.
     * Useful for timeout values like implicit.wait=10
     *
     * @param key - the property name
     * @return    - the value as an int
     */
    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    /**
     * Gets a value as boolean.
     * Useful for headless=false
     *
     * @param key - the property name
     * @return    - the value as a boolean
     */
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
package utils;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Configuration manager for handling environment variables and application configuration
 * Supports loading from .env files and system properties
 * Priority order: System properties > .env file > default values
 */
public class ConfigManager {
    private static final Dotenv dotenv;
    
    static {
        // Load .env file from project root
        dotenv = Dotenv.configure()
                .directory(".")
                .ignoreIfMissing()
                .load();
    }
    
    /**
     * Gets a configuration value by key
     * Priority: System property > .env file
     * @param key The configuration key
     * @return The configuration value, or null if not found
     */
    public static String get(String key) {
        // Priority: System property > .env file
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        return dotenv.get(key);
    }
    
    /**
     * Gets a configuration value by key with a default fallback
     * @param key The configuration key
     * @param defVal The default value to return if key is not found
     * @return The configuration value, or defVal if not found
     */
    public static String get(String key, String defVal) {
        String value = get(key);
        return value != null ? value : defVal;
    }
    
    /**
     * Gets the base URL for the application under test
     * @return The base URL from configuration, or default if not set
     */
    public static String getBaseUrl() {
        return get("BASE_URL", "https://www.automationexercise.com");
    }
}

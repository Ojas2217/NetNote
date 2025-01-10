package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Deals with the config
 */
public class MyStorage {
    private final Properties config;

    /**
     * Constructor
     */
    public MyStorage() {
        this.config = new Properties();
        loadConfig("src", "main", "resources", "client", "client/userConfig.properties");
    }

    /**
     * Return the path given multiple directories
     * @param parts the directories
     * @return the path
     */
    private String getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return new File(path).getAbsolutePath();
    }


    /**
     * Load the configuration from config file
     * @param parts the directories
     */
    public void loadConfig(String... parts) {
        try {
            this.config.load(new FileInputStream(getLocation(parts)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get config property by key
     * @param key the key
     * @return the value for the given key
     */
    public String getConfigValue(String key) {
        return this.config.getProperty(key);
    }

    /**
     * Set new entry or update existent entry in user configuration
     * @param key the key
     * @param value the value
     */
    public void setItemConfig(String key, String value) {
        this.config.setProperty(key, value);
        saveConfig();
    }

    /**
     * Save user configuration
     */
    private void saveConfig() {
        try (FileOutputStream fileOut = new FileOutputStream(
                getLocation("src", "main", "resources", "client", "client/userConfig.properties")
        )) {
            this.config.store(fileOut, "User Configuration");
        } catch (IOException e) {
            throw new RuntimeException("Error saving user configuration", e);
        }
    }

    /**
     * Get the language
     * @return the language
     */
    public String getLanguage() {
        return getConfigValue("language");
    }

    /**
     * Set the language
     * @param language the language
     */
    public void setLanguage(String language) {
        setItemConfig("language", language);
    }

    /**
     * Get the language
     * @return the language
     */
    public String getTheme() {
        return getConfigValue("theme");
    }

    /**
     * Set the language
     * @param isLight the language
     */
    public void setTheme(boolean isLight) {
        setItemConfig("theme", String.valueOf(isLight));
    }
}

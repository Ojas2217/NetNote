package client;

import java.io.*;
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
        loadConfig("client", "src", "main", "resources", "client", "userConfig.properties");
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

    private int timesLoad = 0;

    /**
     * Load the configuration from config file
     * @param parts the directories
     */
    public void loadConfig(String... parts) {
        try {
            this.config.load(new FileInputStream(getLocation(parts)));
        } catch (IOException e) {
            if (timesLoad == 1) throw new RuntimeException();
            timesLoad++;
            loadConfig("client", "src", "main", "resources", "client", "userConfig.properties");
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
                getLocation("client", "src", "main", "resources", "client", "userConfig.properties")
        )) {
            this.config.store(fileOut, "User Configuration");
        } catch (IOException e) {
            saveConfigClient();
        }
    }

    /**
     * Save user configuration for people with different file directories
     */
    private void saveConfigClient() {
        try {
            FileOutputStream fileOut = new FileOutputStream(
                    getLocation("client", "src", "main", "resources", "client", "userConfig.properties"));
            this.config.store(fileOut, "User Configuration");
        } catch (IOException e) {
            throw new RuntimeException(e);
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
     * @param theme the language
     */
    public void setTheme(String theme) {
        setItemConfig("theme", theme);
    }
}

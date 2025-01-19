package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Collection;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

/**
 * Deals with the config
 */
public class MyStorage {
    private final Properties config;
    private final Path pathDefault = Path.of("src", "main", "resources", "client", "userConfig.properties");
    private final Path pathClient = Path.of("client", "src", "main", "resources", "client", "userConfig.properties");
    private boolean useClient = false;
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor
     */
    public MyStorage() {
        this.config = new Properties();
        loadConfig(getPath());

    }

    /**
     * Return the path given multiple directories
     * @return the path
     */
    private String getLocation(Path path) {
        return new File(path.toString()).getAbsolutePath();
    }

    public Path getPath() {
        if (useClient) return pathClient;
        else return pathDefault;
    }

    /**
     * Load the configuration from config file
     * @param path the directories
     */
    public void loadConfig(Path path) {
        try {
            this.config.load(new FileInputStream(getLocation(path)));
        } catch (IOException e) {
            if (useClient) throw new RuntimeException(e);
            useClient = true;
            loadConfig(pathClient);
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
        try {
            FileOutputStream fileOut = new FileOutputStream(getLocation(getPath()));
            this.config.store(fileOut, "User Configuration");
        } catch (Exception e) {
            if (useClient) throw new RuntimeException(e);
            useClient = true;
            saveConfig();
        }
    }

    /**
     * Save user configuration for people with different file directories
     */
    @Deprecated
    private void saveConfigClient() {
        try {
            FileOutputStream fileOut = new FileOutputStream(
                    getLocation(pathClient));
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

    /**
     * Get the collections
     * @return the collections
     */
    public List<Collection> getCollections() {
        try {
            return objectMapper.readValue(getConfigValue("collections"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Collection.class));
        } catch (Exception e) {
            System.err.println("error with reading collection output");
            return List.of();
        }
    }

    /**
     * Set the collections
     * @param collections
     */
    public void setCollections(List<Collection> collections) {
        try {
            String jsonString = objectMapper.writeValueAsString(collections);
            System.out.println(jsonString);
            setItemConfig("collections", jsonString);
        } catch (Exception e) {
            System.err.println("Error with writing collection to config");
        }
    }
}

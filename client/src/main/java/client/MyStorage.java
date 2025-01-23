package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Collection;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Deals with the config
 */
public class MyStorage {
    private final Properties config;
    private final Path pathDefault = Path.of("config", "userConfig.properties");
    private final Path pathClient = Path.of("config", "userConfig.properties");
    private final String pathDefaultCollection = "config/collections";
    private final String pathClientCollection = "config/collections";
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
        String path = pathDefaultCollection;
        if (useClient) path = pathClientCollection;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String jsonString = reader.readLine();
            if (jsonString != null) return objectMapper.readValue(jsonString,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Collection.class));
            else return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("error reading collection file");
            return new ArrayList<>();
        }
    }

    /**
     * Set the collections
     * @param collections
     */
    public void setCollections(List<Collection> collections) {
        String path = pathDefaultCollection;
        if (useClient) path = pathClientCollection;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            String jsonString = objectMapper.writeValueAsString(collections);
            writer.write(jsonString);
        } catch (IOException e) {
            System.err.println("error reading collection file");
        }
    }
}

package client.state;

import java.util.ResourceBundle;

/**
 * A class to hold {@link ResourceBundle} state.resourceBundle = ResourceBundle.getBundle("language", locale);
 */
public class ResourceBundleHolder {
    private ResourceBundle resourceBundle;

    public ResourceBundleHolder() { }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}

package client.utils;

import client.state.ResourceBundleHolder;
import com.google.inject.Inject;
import commons.exceptions.InternationalizationKeys;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

import java.util.ResourceBundle;

/**
 * Utility class for showing alerts
 */
public class AlertUtils {

    private final ResourceBundleHolder resourceBundleHolder;

    @Inject
    public AlertUtils(ResourceBundleHolder resourceBundleHolder) {
        this.resourceBundleHolder = resourceBundleHolder;
    }

    public void showInfo(String title, String header, String content) {
        showError(Alert.AlertType.INFORMATION, title, header, content);
    }

    public void showWarning(String title, String header, String content) {
        showError(Alert.AlertType.WARNING, title, header, content);
    }

    public void showError(String title, String header, String content) {
        showError(Alert.AlertType.ERROR, title, header, content);
    }

    /**
     * Shows an error with internationalized text
     * @param title title key
     * @param header header key
     * @param content content key
     */
    public void showError(InternationalizationKeys title, InternationalizationKeys header, InternationalizationKeys content) {
        ResourceBundle resourceBundle = resourceBundleHolder.getResourceBundle();
        showError(Alert.AlertType.ERROR,
                resourceBundle.getString(title.getKey()),
                resourceBundle.getString(header.getKey()),
                resourceBundle.getString(content.getKey())
        );
    }

    public void showConfirmation(String title, String header, String content) {
        showError(Alert.AlertType.CONFIRMATION, title, header, content);
    }

    /**
     * Creates and shows an alert composed of the given parameters
     *
     * @param type    The type of alert, use {@link javafx.scene.control.Alert.AlertType} to see the options
     * @param title   Title of the alert window
     * @param header  Header of the alert text area
     * @param content Content of the alert text area
     */
    public void showError(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showError(InternationalizationKeys type, String  content) {
        ResourceBundle resourceBundle = resourceBundleHolder.getResourceBundle();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle(resourceBundle.getString(type.getKey()));
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showError(InternationalizationKeys type, InternationalizationKeys content) {
        ResourceBundle resourceBundle = resourceBundleHolder.getResourceBundle();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle(resourceBundle.getString(type.getKey()));
        alert.setContentText(resourceBundle.getString(content.getKey()));
        alert.showAndWait();
    }

}

package client.utils;

import client.state.ResourceBundleHolder;
import com.google.inject.Inject;
import commons.exceptions.ErrorKeys;
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
        showAlert(Alert.AlertType.INFORMATION, title, header, content);
    }

    public void showWarning(String title, String header, String content) {
        showAlert(Alert.AlertType.WARNING, title, header, content);
    }

    public void showError(String title, String header, String content) {
        showAlert(Alert.AlertType.ERROR, title, header, content);
    }

    public void showError(ErrorKeys title, ErrorKeys header, ErrorKeys content) {
        ResourceBundle resourceBundle = resourceBundleHolder.getResourceBundle();
        showAlert(Alert.AlertType.ERROR,
                resourceBundle.getString(title.getKey()),
                resourceBundle.getString(header.getKey()),
                resourceBundle.getString(content.getKey())
        );
    }

    public void showConfirmation(String title, String header, String content) {
        showAlert(Alert.AlertType.CONFIRMATION, title, header, content);
    }

    /**
     * Creates and shows an alert composed of the given parameters
     *
     * @param type    The type of alert, use {@link javafx.scene.control.Alert.AlertType} to see the options
     * @param title   Title of the alert window
     * @param header  Header of the alert text area
     * @param content Content of the alert text area
     */
    public void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

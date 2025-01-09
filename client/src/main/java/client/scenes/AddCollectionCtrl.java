package client.scenes;

import client.business.AddCollectionService;
import client.utils.AlertUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class AddCollectionCtrl {
    private final MainCtrl mainCtrl;
    @FXML
    public TextField collectionTitle;
    @FXML
    private Button cancel;
    private AlertUtils alertUtils;
    private AddCollectionService addCollectionService;

    @Inject
    public AddCollectionCtrl(AddCollectionService addCollectionService, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.alertUtils = new AlertUtils();
        this.addCollectionService = addCollectionService;

    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public void ok() {
        if (collectionTitle.getText().isEmpty()) {
            alertUtils.showAlert(Alert.AlertType.INFORMATION, "Please add a title.");
            return;
        }
        if (!addCollectionService.isUnique(collectionTitle.getText())) {
            alertUtils.showAlert(Alert.AlertType.ERROR, "Collection with this title already exists");
            return;
        }
        try {
            String title = collectionTitle.getText();
            addCollectionService.addCollection(title);
            clearFields();
            collectionTitle.setFocusTraversable(false);
            cancel.requestFocus();
            mainCtrl.logRegular("Added new Collection: '" + title + "'");
            mainCtrl.showOverview();
        } catch (WebApplicationException e) {
            alertUtils.showAlert(Alert.AlertType.ERROR, e.getMessage());
        }

    }

    public void clearFields() {
        collectionTitle.clear();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }
}

package client.scenes;

import client.business.AddCollectionService;
import client.utils.AlertUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import static commons.exceptions.InternationalizationKeys.*;

/**
 * Controller class for adding a new collection, uses addCollectionService for business logic
 */
public class AddCollectionCtrl {
    private final MainCtrl mainCtrl;
    @FXML
    public TextField titleTextField;
    @FXML
    private Button cancelButton;
    private final AlertUtils alertUtils;
    private final AddCollectionService addCollectionService;

    @Inject
    public AddCollectionCtrl(AddCollectionService addCollectionService, MainCtrl mainCtrl, AlertUtils alertUtils) {
        this.mainCtrl = mainCtrl;
        this.alertUtils = alertUtils;
        this.addCollectionService = addCollectionService;
    }

    public void cancel() {
        clearFields();
        mainCtrl.closeAddCollection();
    }

    /**
     * Checks whether the title contains a proper title and attempts to add a new collection onto the server.
     * If the title is not deemed proper, the user will be informed using AlertUtils
     *
     * @throws WebApplicationException if an error occurs when adding the new collection to the server
     */
    public void ok() {
        if (titleTextField.getText().isEmpty()) {
            alertUtils.showError(
                    INFORMATION,
                    EMPTY_TITLE,
                    ENTER_VALID_COLLECTION_TITLE
            );
            return;
        }
        if (!addCollectionService.isUnique(titleTextField.getText())) {
            alertUtils.showError(
                    ERROR,
                    NOTE_WITH_TITLE_EXISTS,
                    ENTER_VALID_COLLECTION_TITLE
            );
            return;
        }
        try {
            String title = titleTextField.getText();
            addCollectionService.addCollection(title);
            clearFields();

            titleTextField.setFocusTraversable(false);
            cancelButton.requestFocus();
            mainCtrl.logRegular("Added new Collection: '" + title + "'");
            mainCtrl.closeAddCollection();
        } catch (WebApplicationException e) {
            alertUtils.showError(
                    ERROR,
                    e.getMessage()
            );
        }

    }

    public void clearFields() {
        titleTextField.clear();
    }

    /**
     * Handle shortcuts
     * Enter: attempts to add a new collection with the specified title
     * Escape: cancels the creation of a new collection
     *
     * @param e the information about the pressed key
     */
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

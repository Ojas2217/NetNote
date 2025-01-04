package client.scenes;

import client.business.AddNoteService;
import client.utils.AlertUtils;
import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Note;
import commons.NotePreview;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.util.List;

/**
 * Controller class for adding a new note.
 * <p>
 * The {@code AddNoteControl} class handles user interactions for adding a new note in the application.
 * It is responsible for validating input, interacting with the server to create a new note, and updating
 * the view to show the list of notes after the operation.
 * </p>
 * <p>
 * The class contains methods to:
 * <ul>
 *     <li>Cancel the note creation process and return to the overview page ({@link #cancel()}).</li>
 *     <li>Confirm the creation of the new note, sending the data to the server, and handle potential errors
 *     ({@link #ok()}).</li>
 *     <li>Clear the input fields ({@link #clearFields()}).</li>
 *     <li>Handle key events for the {@link TextField}, allowing user interaction through the ENTER and ESCAPE keys
 *     ({@link #keyPressed(KeyEvent)}).</li>
 * </ul>
 * </p>
 * <p>
 * This controller uses {@link NoteUtils} for communication with the backend and {@link MainCtrl}
 * for navigating between the different scenes in the application.
 * </p>
 */
public class AddNoteControl {
    private final MainCtrl mainCtrl;
    @FXML
    public TextField noteTitle;
    @FXML
    private Button cancel;
    private AlertUtils alertUtils;
    private AddNoteService addNoteService;
    @FXML
    private Label characterWarning;
    private final int maxNumOfCharacters = 50;

    @Inject
    public AddNoteControl(AddNoteService addNoteService, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.addNoteService = addNoteService;
        this.alertUtils = new AlertUtils();

    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public TextField getNoteTitle() {
        return noteTitle;
    }

    /**
     * Adds a note on the server.
     * In case of exceptions, shows an alert.
     **/
    public void ok() {
        if (noteTitle.getText().isEmpty()) {
            alertUtils.showAlert(Alert.AlertType.INFORMATION, "Please add a note title.");
            return;
        }
        if (!addNoteService.isUnique(noteTitle.getText())) {
            alertUtils.showAlert(Alert.AlertType.ERROR, "Note with this title already exists");
            return;
        }
        try {
            String title = noteTitle.getText();
            addNoteService.addNote(title);
            clearFields();
            noteTitle.setFocusTraversable(false);
            cancel.requestFocus();
            mainCtrl.logRegular("Added new note: '" + title + "'");
            mainCtrl.showOverview();
        } catch (WebApplicationException e) {
            alertUtils.showAlert(Alert.AlertType.ERROR, e.getMessage());
        }

    }

    public void clearFields() {
        noteTitle.clear();
        characterWarning.setVisible(false);
    }


    /**
     * Handles keyboard input
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

    /**
     * Ensures that the title is not longer then a certain amount of characters
     */
    public void ensureMaxCharacters() {
        if (noteTitle.getText().length() > maxNumOfCharacters) {
            characterWarning.setVisible(true);
            String goodString = noteTitle.getText().substring(0, maxNumOfCharacters);
            noteTitle.replaceText(maxNumOfCharacters, noteTitle.getText().length(), "");
        } else {
            characterWarning.setVisible(false);
        }
    }
}

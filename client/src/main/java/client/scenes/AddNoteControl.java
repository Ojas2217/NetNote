package client.scenes;

import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Note;
import commons.ProcessOperationException;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

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
    private final NoteUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField noteTitle;

    @Inject
    public AddNoteControl(NoteUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    /**
     * Adds a note on the server.
     * In case of exceptions, shows an alert.
     **/
    public void ok() {
        try {
            server.createNote(new Note(noteTitle.getText(), "empty 123 testing 123 format"));
        } catch (WebApplicationException | ProcessOperationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        mainCtrl.showOverview();
    }

    private void clearFields() {
        noteTitle.clear();
    }


    /**
     * Handles keyboard input
     * */
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

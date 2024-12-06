package client.scenes;

import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Note;
import commons.NotePreview;
import commons.ProcessOperationException;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    private final NoteUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField noteTitle;
    @FXML
    private Button cancel;

    @Inject
    public AddNoteControl(NoteUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public void setNoteTitle(TextField noteTitle) {
        this.noteTitle = noteTitle;
    }

    public TextField getNoteTitle() {
        return noteTitle;
    }

    /**
     * Adds a note on the server.
     * In case of exceptions, shows an alert.
     **/
    public void ok() {
        try {
            if (!noteTitle.getText().isEmpty()) {
                if (isUnique(noteTitle)) {
                    server.createNote(new Note(noteTitle.getText(), "empty 123 testing 123 format"));
                    clearFields();
                    noteTitle.setFocusTraversable(false);
                    cancel.requestFocus();
                    mainCtrl.showOverview();
                } else {
                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText("Note with this title already exists");
                    alert.showAndWait();
                }
            } else {
                var alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("Please add a note title.");
                alert.showAndWait();
            }
        } catch (WebApplicationException | ProcessOperationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void clearFields() {
        noteTitle.clear();
    }

    public boolean isUnique(TextField noteTitle) {
        List<NotePreview> Notes = mainCtrl.getOverviewCtrl().getNotes();
        if (Notes == null) {
            return true;
        }
        for (NotePreview note:Notes) {
            if (note.getTitle().equals(noteTitle.getText())) {
                return false;
            }
        }
        return true;
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
}

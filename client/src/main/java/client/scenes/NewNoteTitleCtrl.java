package client.scenes;


import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Note;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

/**
 * Controller class for editing the title of an existing note.
 * <p>
 * The {@code NewNoteTitleCtrl} class manages user interactions for changing the title of a selected note.
 * It handles updating the note's title, clearing input fields, and navigating between the relevant scenes
 * in the application.
 * </p>
 * <p>
 * The class contains methods to:
 * <ul>
 *     <li>Cancel the title change process and return to the overview page ({@link #cancel()}).</li>
 *     <li>Confirm the new title, update the selected note, and transition
 *     back to the overview page ({@link #ok()}).</li>
 *     <li>Update the title of the given note, handling potential errors ({@link #newTitle(Note)}).</li>
 *     <li>Clear the input fields ({@link #clearFields()}).</li>
 *     <li>Handle key events for the {@link TextField}, supporting keyboard shortcuts for confirming (ENTER)
 *     and canceling (ESCAPE) the operation ({@link #keyPressed(KeyEvent)}).</li>
 * </ul>
 * </p>
 * <p>
 * This controller uses {@link NoteUtils} to interact with the backend for editing note data and {@link MainCtrl}
 * for navigating between different scenes in the application.
 * </p>
 */
public class NewNoteTitleCtrl {
    private final MainCtrl mainCtrl;
    private final NoteUtils noteUtils;
    @FXML
    private TextField newNoteTitle;

    @Inject
    public NewNoteTitleCtrl(MainCtrl mainCtrl, NoteUtils noteUtils) {
        this.mainCtrl = mainCtrl;
        this.noteUtils = noteUtils;
    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    /**
     *Creates a new note with the user inputted title.
     *Shows a message if user tries to press ok/enter without
     *entering a note title
     */
    public void ok() {
        if (newNoteTitle.getText().isEmpty()) {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Please enter a new note title");
            alert.showAndWait();
        } else {
            Note note = mainCtrl.getOverviewCtrl().getNote();
            newTitle(note);
            mainCtrl.showOverview();
        }
    }

    /**
     * it shows the newTitle scene and changes the title of the selected note
     *
     * @param note the note of which the title will be changed
     */
    public void newTitle(Note note) {
        try {
            mainCtrl.showNewTitle();
            if (note != null) {
                String newTitle = newNoteTitle.getText();
                if (!newTitle.equals("")) {
                    note.setTitle(newTitle);
                    noteUtils.editNote(note);
                    clearFields();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        newNoteTitle.clear();
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

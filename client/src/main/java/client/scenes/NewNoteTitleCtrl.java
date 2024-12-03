package client.scenes;


import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Note;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.Optional;

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
     * Changes title only when note is selected.
     */
    public void ok() {
        Optional<Note> note = mainCtrl.getOverviewCtrl().fetchSelectedNote();
        if (note.isPresent()) {
            newTitle(note.get());
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
                note.setTitle(newTitle);
                noteUtils.editNote(note);
                clearFields();
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
                Optional<Note> note = mainCtrl.getOverviewCtrl().fetchSelectedNote();
                note.ifPresent(this::newTitle);
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

}

package client.scenes;


import client.business.NewNoteTitleService;
import client.utils.AlertUtils;
import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Note;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import static commons.exceptions.InternationalizationKeys.*;

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
 *     <li>Update the title of the given note, handling potential errors ().</li>
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
    @FXML
    private TextField newNoteTitle;
    private AlertUtils alertUtils;
    private NewNoteTitleService newNoteTitleService;

    @Inject
    public NewNoteTitleCtrl(MainCtrl mainCtrl, NewNoteTitleService newNoteTitleService, AlertUtils alertUtils) {
        this.mainCtrl = mainCtrl;
        this.newNoteTitleService = newNoteTitleService;
        this.alertUtils = alertUtils;
    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public NewNoteTitleService getNewNoteTitleService() {
        return newNoteTitleService;
    }

    public void setNewNoteTitle(TextField newNoteTitle) {
        this.newNoteTitle = newNoteTitle;
    }

    public TextField getNewNoteTitle() {
        return newNoteTitle;
    }

    /**
     * Creates a new note with the user inputted title.
     * Shows a message if user tries to press ok/enter without
     * entering a note title
     */
    public void ok() {
        if (newNoteTitle.getText().isEmpty()) {
            alertUtils.showError(INFORMATION, ENTER_VALID_NOTE_TITLE);
            return;
        }

        // Fetch the selected Note if it exists on the server
        Optional<Note> note = mainCtrl.getOverviewCtrl().fetchSelected();
        if (note.isEmpty()) {
            alertUtils.showError(INFORMATION, NOTE_MAY_BE_DELETED);
            return;
        }
        if (!newNoteTitleService.isUnique(newNoteTitle.getText())) {
            alertUtils.showError(
                    ERROR,
                    NOTE_WITH_TITLE_EXISTS,
                    ENTER_VALID_NOTE_TITLE
            );
            return;
        }
        String oldTitle = note.get().getTitle();
        try {
            if (mainCtrl.getOverviewCtrl().getSelectedCollection() != null) {
                mainCtrl.getOverviewCtrl().getSelectedCollection().getNotes().remove(note.get());
                String title = newNoteTitle.getText();
                newNoteTitleService.newTitle(note.get(), title);
                clearFields();
                mainCtrl.logRegular("Changed the title of note '" + oldTitle + "' to '" + title + "'");
                mainCtrl.getOverviewCtrl().getSelectedCollection().getNotes().add(note.get());
                mainCtrl.getCollectionOverviewCtrl().selectCollection(mainCtrl.getOverviewCtrl().getSelectedCollection());
                mainCtrl.showOverview();
            } else {
                mainCtrl.getCollectionOverviewCtrl().getDefaultCollection().getNotes().remove(note.get());
                String title = newNoteTitle.getText();
                newNoteTitleService.newTitle(note.get(), title);
                clearFields();
                mainCtrl.logRegular("Changed the title of note '" + oldTitle + "' to '" + title + "'");
                mainCtrl.getCollectionOverviewCtrl().getDefaultCollection().getNotes().add(note.get());
                mainCtrl.getCollectionOverviewCtrl().selectCollection(mainCtrl.getCollectionOverviewCtrl().getDefaultCollection());
                mainCtrl.showOverview();
            }
        } catch (Exception e) {
            mainCtrl.logError("Error changing title of note " + note.get().getTitle() + ": " + e.getMessage());
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

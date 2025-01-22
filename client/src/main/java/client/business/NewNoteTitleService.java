package client.business;

import client.scenes.MainCtrl;
import client.utils.NoteUtils;
import commons.Note;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Service class for handling the business logic of editing the title of a note.
 */
public class NewNoteTitleService {
    private final NoteUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public NewNoteTitleService(NoteUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void newTitle(Note note, String newTitle) throws Exception {
        if (note == null || newTitle == null || newTitle.isEmpty()) {
            throw new IllegalArgumentException("Note or title is invalid.");
        }
        String oldTitle = note.getTitle();
        note.setTitle(newTitle);
        try {
            server.send("/app/title", note);
        } catch (Exception e) {
            note.setTitle(oldTitle); // Revert changes on failure
            throw new Exception("Failed to update the note title. " + e.getMessage(), e);
        }
    }

    /**
     * Checks if a note with the given title is unique.
     *
     * @param title the title to check.
     * @return true if the title is unique, false otherwise.
     */
    public boolean isUnique(String title) {
        List<Note> notes;
        if (mainCtrl.getOverviewCtrl().getSelectedCollection() != null) {
            notes = mainCtrl.getOverviewCtrl().getSelectedCollection().getNotes();
        } else {
            notes = mainCtrl.getCollectionOverviewCtrl().getDefaultCollection().getNotes();
        }

        if (notes == null) {
            return true;
        }
        for (Note note : notes) {
            if (note.getTitle().equals(title)) {
                return false;
            }
        }
        return true;
    }
}

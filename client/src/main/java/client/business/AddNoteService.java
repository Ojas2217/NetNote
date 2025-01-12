package client.business;

import client.scenes.MainCtrl;
import client.utils.NoteUtils;
import commons.Note;
import commons.NotePreview;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Service class for handling the business logic of adding a note.
 */
public class AddNoteService {

    private final NoteUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public AddNoteService(NoteUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Sends a new note to the server.
     *
     * @param title the title of the note.
     * @throws Exception if there is an error during the operation.
     */
    public void addNote(String title) {
        server.send("/app/add", new Note(title, "empty 123 testing 123 format"));
    }

    /**
     * Checks if a note with the given title is unique.
     *
     * @param title the title to check.
     * @return true if the title is unique, false otherwise.
     */
    public boolean isUnique(String title) {
        List<NotePreview> notes = mainCtrl.getOverviewCtrl().getNotes();
        if (notes == null) {
            return true;
        }
        for (NotePreview note: notes) {
            if (note.getTitle().equals(title)) {
                return false;
            }
        }
        return true;
    }
}

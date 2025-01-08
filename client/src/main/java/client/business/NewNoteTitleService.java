package client.business;

import client.utils.NoteUtils;
import commons.Note;
import jakarta.inject.Inject;

/**
 * Service class for handling the business logic of editing the title of a note.
 */
public class NewNoteTitleService {
    private final NoteUtils server;

    @Inject
    public NewNoteTitleService(NoteUtils server) {
        this.server = server;
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
}

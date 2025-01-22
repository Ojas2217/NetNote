package client.business;

import client.scenes.MainCtrl;
import client.utils.NoteUtils;
import commons.Collection;
import commons.Note;
import commons.NoteCollectionPair;
import jakarta.inject.Inject;

import java.util.ArrayList;
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
        var note = new Note(title, "");
        Collection collection;
        if(mainCtrl.getOverviewCtrl().getSelectedCollection()!=null){
            collection = mainCtrl.getOverviewCtrl().getSelectedCollection();
        }else{
            collection = mainCtrl.getCollectionOverviewCtrl().getDefaultCollection();
        }
        var pair = NoteCollectionPair.of(note, collection);
        server.send("/app/add", pair);
    }

    /**
     * Checks if a note with the given title is unique.
     *
     * @param title the title to check.
     * @return true if the title is unique, false otherwise.
     */
    public boolean isUnique(String title) {
        List<Note> notes;
        if(mainCtrl.getOverviewCtrl().getSelectedCollection()!=null) {
            notes = mainCtrl.getOverviewCtrl().getSelectedCollection().getNotes();
        }else{
            notes = mainCtrl.getCollectionOverviewCtrl().getDefaultCollection().getNotes();
        }

        if (notes == null) {
            return true;
        }
        for (Note note: notes) {
            if (note.getTitle().equals(title)) {
                return false;
            }
        }
        return true;
    }
}

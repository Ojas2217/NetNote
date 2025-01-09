package client.services;

import client.state.ResourceBundleHolder;
import com.google.inject.Inject;
import commons.Note;
import commons.NotePreview;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.util.List;
import java.util.ResourceBundle;

import static commons.exceptions.InternationalizationKeys.*;

/**
 * Service class that aids the NoteOverviewCtrl
 */
public class NoteOverviewService {
    private final ResourceBundleHolder resourceBundleHolder;

    @Inject
    public NoteOverviewService(ResourceBundleHolder resourceBundleHolder) {
        this.resourceBundleHolder = resourceBundleHolder;
    }

    public void initializeServerAdd(ObservableList<NotePreview> data, Note note) {
        data.add(new NotePreview(note.id, note.title));
    }

    /**
     * Initializes the title editing of notes
     *
     * @param data - all the viewable notes
     * @param note - the selected note
     * @param notes - all notes
     */
    public void initializeServerTitle(ObservableList<NotePreview> data, NotePreview note, List<NotePreview> notes) {
        int indexData = data.indexOf(note);
        int indexList = notes.indexOf(note);
        NotePreview newNote = new NotePreview(note.getId(), note.getTitle());
        data.set(indexData, newNote);
        notes.set(indexList, newNote);
    }

    /**
     * Initializes the deleting of notes
     *
     * @param data - all the viewable notes
     * @param note - the note to be deleted
     * @param notes - all notes
     */
    public void initializeServerDelete(ObservableList<NotePreview> data, Note note, List<NotePreview> notes) {
        NotePreview theNote = data.stream().filter(n -> n.getId() == note.id).toList().getFirst();
        int indexData = data.indexOf(theNote);
        int indexList = notes.indexOf(theNote);
        data.remove(indexData);
        notes.remove(indexList);
    }

    /**
     * Prompts the user whether they really want to delete a note
     *
     * @return the users selection
     */
    public int promptDeleteNote() {
        ResourceBundle resourceBundle = resourceBundleHolder.getResourceBundle();
        String message = resourceBundle.getString(DELETE_MESSAGE.getKey());
        String title = resourceBundle.getString(DELETE_CONFIRM.getKey());

        return JOptionPane.showConfirmDialog(
                null,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
    }
}

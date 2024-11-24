package client.scenes;

import com.google.inject.Inject;
import commons.Note;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class NoteSelectionCtrl {

    @FXML
    private TextArea textAreaNote;

    @FXML
    private ListView<Note> listViewNoteSelection;

    private List<Note> notes;

    @Inject
    public NoteSelectionCtrl() {
        this.notes = new ArrayList<>();
    }

    /**
     * Adds a note to listViewNoteSelection which content is equal to the content of the textAreaNote
     */
    public void noteAddButton() {
        Note newNote = new Note("", textAreaNote.getText()); //The title should still be implemented here
        listViewNoteSelection.getItems().add(newNote);

        // Change the display text of the listView to the title of the Note
        listViewNoteSelection.setCellFactory(param -> new TextFieldListCell<>(new StringConverter<Note>() {
            @Override
            public String toString(Note note) {
                String title = note.getTitle();
                return title.isEmpty() ? "*Empty title*" : title;
            }

            @Override
            public Note fromString(String string) {
                return null;
            }
        }));

        notes.add(newNote);
    }

    /**
     * Gets the selected Note in the listViewNoteSelection and removes that Note from the list
     */
    public void noteDeleteButton() {
        Object selectedListViewItem = listViewNoteSelection.getSelectionModel().getSelectedItem();

        if (selectedListViewItem != null) {
            listViewNoteSelection.getItems().remove(selectedListViewItem);
        }
    }
}

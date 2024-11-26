//package client.scenes;
//
//import com.google.inject.Inject;
//import commons.Note;
//import jakarta.ws.rs.WebApplicationException;
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ListView;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.control.cell.TextFieldListCell;
//import javafx.scene.input.KeyEvent;
//import javafx.stage.Modality;
//import javafx.util.StringConverter;
//import java.util.ArrayList;
//import java.util.List;
//
//public class NoteSelectionCtrl {

//      ********************************************************************
//      |   This class was refactored into the AddNoteControl Class        |
//      |   This was done to enable communication with the server.         |
//      |   As, previously the notes were being stored locally in a List.  |
//      ********************************************************************

//    private final MainCtrl mainCtrl;
//    @FXML
//    private TextArea currentNote;
//    @FXML
//    private ListView<Note> availableNotes;
//    @FXML
//    private TextField noteTitle;
//    private List<Note> notes;
//
//    @Inject
//    public NoteSelectionCtrl() {
//        this.notes = new ArrayList<>();
//        this.mainCtrl = new MainCtrl();
//    }
//
//    /**
//     * Adds a note to listViewNoteSelection which content is equal to the content of the currentNote
//     */
//    public void noteAddButton() {
//        Note newNote = new Note(noteTitle.getText(), currentNote.getText()); //The title should still be implemented here
//        availableNotes.getItems().add(newNote);
//
//        // Change the display text of the listView to the title of the Note
//        availableNotes.setCellFactory(param -> new TextFieldListCell<>(new StringConverter<Note>() {
//            @Override
//            public String toString(Note note) {
//                String title = note.getTitle();
//                return title.isEmpty() ? "empty" : title;
//            }
//
//            @Override
//            public Note fromString(String string) {
//                return null;
//            }
//        }));
//
//        notes.add(newNote);
//    }
//
//    /**
//     * Gets the selected Note in the listViewNoteSelection and removes that Note from the list
//     */
//    public void noteDeleteButton() {
//        Object selectedListViewItem = availableNotes.getSelectionModel().getSelectedItem();
//
//        if (selectedListViewItem != null) {
//            availableNotes.getItems().remove(selectedListViewItem);
//        }
//    }
//
//}

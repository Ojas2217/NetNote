package client.scenes;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Note;
import commons.ProcessOperationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

/**
 * Controller for the Note Overview view.
 * <p>
 * The {@code NoteOverviewCtrl} is responsible for managing the view that displays all the notes in a table.
 * It interacts with the {@link NoteUtils} service to fetch, delete, and refresh notes.
 * </p>
 * <p>
 * The controller binds data to the table view, handles user actions like adding or deleting notes,
 * and updates the view accordingly.
 * </p>
 * <p>
 * Key features:
 * <ul>
 *     <li>Displays a table of notes, with the title of each note.</li>
 *     <li>Allows for the deletion of selected notes.</li>
 *     <li>Supports refreshing the table with the latest list of notes from the server.</li>
 * </ul>
 * </p>
 */
public class NoteOverviewCtrl implements Initializable {
    private final NoteUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TableView<Note> table;
    @FXML
    private TableColumn<Note, String> noteTitle;
    @FXML
    private Label selectedNoteTitle;
    @FXML
    private TextArea selectedNoteContent;


    @Inject
    public NoteOverviewCtrl(NoteUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        noteTitle.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().title));
    }

    public void addNote() {
        mainCtrl.showAdd();
    }

    /**
     * Deletes the selected note from the table and refreshes the overview.
     *
     * @throws ProcessOperationException if there is an issue during the deletion process
     */
    public void deleteNote() throws ProcessOperationException {
        Note note = table.getSelectionModel().getSelectedItem();
        if (note != null) {
            server.deleteNote(note.id);
        }
        refresh();
    }

    /**
     * Responsible for refreshing all content in the overview screen.
     * */
    public void refresh() throws ProcessOperationException {
        var notes = server.getAllNotes();
        ObservableList<Note> data = FXCollections.observableList(notes);
        table.setItems(data);
        displaySelectedNote();
    }

    public Optional<Note> selectAndUpdate() throws ProcessOperationException {
        if (table.getSelectionModel().getSelectedItem() != null)
            return Optional.of(server.getNote(table.getSelectionModel().getSelectedItem().id));
        return Optional.empty();
    }

    /**
     * If a note is selected, updates the overview to show the title and content.
     * */
    public void displaySelectedNote() throws ProcessOperationException {
        Optional<Note> note = selectAndUpdate();
        if (note.isPresent()) {
            selectedNoteTitle.setText(note.get().title);
            selectedNoteContent.setText(note.get().content);
        }
    }
}

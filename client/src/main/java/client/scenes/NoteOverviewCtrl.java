package client.scenes;

import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
    private ObservableList<Note> data;
    @FXML
    private TableView<Note> table;
    @FXML
    private TableColumn<Note, String> noteTitle;

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
     * Deletes the selected note from the table and refreshes the table view.
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

    public void refresh() throws ProcessOperationException {
        var notes = server.getAllNotes();
        data = FXCollections.observableList(notes);
        table.setItems(data);
    }
}

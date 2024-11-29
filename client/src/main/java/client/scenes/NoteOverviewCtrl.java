package client.scenes;

import java.net.URL;
import java.util.List;
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
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javax.swing.*;

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
    @FXML
    private TextField searchText;

    private List<Note> notes;
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
    public void refresh() {
        try {
            notes = server.getAllNotes();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            String errorMessage = "Error retrieving data from the server, unable to refresh notes";
            JOptionPane.showMessageDialog(null, errorMessage, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        search();
    }

    /**
     * If a note is selected, updates the overview to show the title and content.
     * */
    public void displaySelectedNote() {
        Optional<Note> note = selectAndUpdate();
        if (note.isPresent()) {
            selectedNoteTitle.setText(note.get().title);
            selectedNoteContent.setText(note.get().content);
        }
    }

    /**
     * @return {@code Optional<Note>} with the {@code Note} if one is selected.
     *         {@code Optional.empty()} if a note isn't selected or doesn't exist on the server.
     * */
    public Optional<Note> selectAndUpdate() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            try {
                return Optional.of(server.getNote(table.getSelectionModel().getSelectedItem().id));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    /**
     * If there is text in the search bar, displays notes whose title contains the text.
     */
    public void search() {
        String text = searchText.getText();
        List<Note> filteredNotes = notes
                .stream()
                .filter(x -> x.getTitle().contains(text))
                .toList();
        data = FXCollections.observableList(filteredNotes);
        table.setItems(data);
        displaySelectedNote();
    }

    /**
     * Currently only has a keyboard shortcut for refreshing/searching
     * more shortcuts can be added in the future.
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                refresh();
                break;
            default:
                break;
        }
    }

    public void empty() {
        searchText.setText("");
        refresh();
    }
}

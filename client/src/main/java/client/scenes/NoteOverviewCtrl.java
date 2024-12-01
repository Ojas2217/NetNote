package client.scenes;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.ResourceBundle;
import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.ExceptionType;
import commons.Note;
import commons.NoteDTO;
import commons.ProcessOperationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import org.springframework.http.HttpStatus;
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
    private ObservableList<NoteDTO> data;
    @FXML
    private TableView<Note> table;
    private TableView<NoteDTO> table;
    @FXML
    private TableColumn<Note, String> noteTitle;
    private TableColumn<NoteDTO, String> noteTitle;
    @FXML
    private TextField searchText;

    private List<Note> notes;
    private List<NoteDTO> notes;
    @FXML
    private Label selectedNoteTitle;
    @FXML
    private TextArea selectedNoteContent;
    private String selectedNoteContentBuffer;

    private long selectedNoteId;

    @Inject
    public NoteOverviewCtrl(NoteUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        noteTitle.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getTitle()));
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
        updateSelection();
        Optional<Note> note = fetchSelectedNote();

        if (getSelectedNoteId().isEmpty()) return;

        if (note.isPresent()) {
            server.deleteNote(getSelectedNoteId().getAsLong());
        }

        refresh();
    }

    /**
     * Responsible for refreshing all content in the overview screen.
     * */
    public void refresh() {
        sendNoteContentToServer();
        try {
            notes = server.getIdsAndTitles();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            String errorMessage = "Error retrieving data from the server, unable to refresh notes";
            JOptionPane.showMessageDialog(null, errorMessage, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        search();
    }

    /**
     * Updates locally stored {@code selectedNoteId}
     * <p>
     * Since the id is used in many parts of the code, this method seeks to make the code more readable by
     * locally storing the id.
     * </p>
     */
    public void updateSelection() {
        int index = table.getSelectionModel().getSelectedIndex();

        if (index == -1) {                           // from what I understand, -1 is the default
            selectedNoteId = -1;   // for when nothing is selected
        } else {
            selectedNoteId = table.getSelectionModel().getSelectedItem().getId();
        }
    }

    /**
     * If a note is selected, updates the overview to show the title and content.
     * */
    public void displaySelectedNote() {
        updateSelection();
        if (getSelectedNoteId().isEmpty()) return;

        Optional<Note> note = fetchSelectedNote();
        if (note.isEmpty()) return;

        selectedNoteTitle.setText(note.get().title);
        selectedNoteContent.setText(note.get().content);
    }

    /**
     * @return {@code Optional<Note>} with the {@code Note} if one is selected.
     * {@code Optional.empty()} if a note isn't selected or doesn't exist on the server.
     */
    public Optional<Note> fetchSelectedNote() {
        if (getSelectedNoteId().isEmpty())
            return Optional.empty();

        try {
            return Optional.of(server.getNote(getSelectedNoteId().getAsLong()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Sends {@link Note} content to the server.
     * <p>
     *     This method verifies whether the currently selected {@link Note} exists on the server
     *     and if yes, sends any changes back to the server. If the note is not present,
     *     shows an error message using an {@link Alert}.
     * </p>
     * */
    public void sendNoteContentToServer() {
        Optional<Note> note = fetchSelectedNote();
        try {
            if (note.isEmpty())
                throw new ProcessOperationException(
                        "The note you're trying to edit is not on the server",
                        HttpStatus.NOT_FOUND.value(),
                        ExceptionType.SERVER_ERROR
                );

            updateContentBuffer();
            note.get().content = selectedNoteContentBuffer;
            server.editNote(note.get());

        } catch (ProcessOperationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void updateContentBuffer() {
        selectedNoteContentBuffer = selectedNoteContent.getText();
    }

    /**
     * If there is text in the search bar, displays notes whose title contains the text.
     */
    public void search() {
        String text = searchText.getText();
        try {
            if (notes == null) throw new NullPointerException();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            String errorMessage = "Error: notes was null. Something went wrong in search()";
            JOptionPane.showMessageDialog(null, errorMessage, "ERROR", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<NoteDTO> filteredNotes = notes
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

    public OptionalLong getSelectedNoteId() {
        if (selectedNoteId < 0) return OptionalLong.empty();
        return OptionalLong.of(selectedNoteId);
    }
}

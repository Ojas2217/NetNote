package client.scenes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.ResourceBundle;

import javafx.scene.input.MouseButton;
import client.handlers.NoteSearchResult;
import client.services.Markdown;
import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Note;
import commons.NotePreview;
import commons.ProcessOperationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.scene.input.KeyEvent;

import javax.swing.*;

import static java.util.Objects.isNull;

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
    private ObservableList<NotePreview> data;
    @FXML
    private TableView<NotePreview> table;
    @FXML
    private TableColumn<NotePreview, String> noteTitle;
    @FXML
    private TextField searchText;
    @FXML
    private WebView webView;
    private final Markdown markdown = new Markdown();

    @FXML
    private WebView webViewLogger;

    private List<NotePreview> notes;
    @FXML
    private Label selectedNoteTitle;
    @FXML
    private TextArea selectedNoteContent;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem changeTitle;
    @FXML
    private MenuItem refreshNote;
    @FXML
    private MenuItem deleteNote;
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
        table.getSelectionModel().selectedItemProperty()
                .addListener((_, _, newValue) -> {
                    // null check needed so that Mouse Up doesn't set it to default.
                    if (isNull(newValue)) return;

                    sendSelectedNoteContentToServer();
                    updateSelection();
                    showSelectedNote();
                });

        // NEED TO ADD DELAY
        selectedNoteContent.textProperty().addListener((_, _, _) -> {
            sendSelectedNoteContentToServer();
        });

        selectedNoteContent.textProperty().addListener((_, _, newValue) -> {
            markdownView(newValue);
        });

        table.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                showContextMenu(event);
            }
        });

        server.registerForMessages("/topic/add", q -> {
            data.add(new NotePreview(q.id, q.title));
        });
        server.registerForMessages("/topic/title", q -> {
            NotePreview theNote = data.stream().filter(n -> n.getId() == q.id).toList().get(0);
            int indexData = data.indexOf(theNote);
            int indexList = notes.indexOf(theNote);
            NotePreview newNote = new NotePreview(q.id,  q.title);
            data.set(indexData, newNote);
            notes.set(indexList, newNote);

            if (selectedNoteTitle.equals(theNote.getTitle())) selectedNoteTitle.setText(q.title);
        });
        server.registerForMessages("/topic/delete", q -> {
            NotePreview theNote = data.stream().filter(n -> n.getId() == q.id).toList().get(0);
            int indexData = data.indexOf(theNote);
            int indexList = notes.indexOf(theNote);
            data.remove(indexData);
            notes.remove(indexList);
        });
        server.registerForMessages("/topic/update", q -> {
            if (selectedNoteId == q.id) selectedNoteContent.setText(q.content);
        });
    }

    public void log(String logString) {
        String html = markdown.render(logString);
        webViewLogger.getEngine().loadContent(html);
    }

    /**
     * Loads the rendered {@link Markdown} version of the note to the WebView part of the NoteOverview Scene.
     *
     * @param commonmark HTML to be printed
     */
    private void markdownView(String commonmark) {
        String html = markdown.render(commonmark);
        webView.getEngine().loadContent(html);
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
        Optional<Note> note = fetchSelected();
        if (note.isEmpty()) return;

        String message = "Are you sure you want to delete this note?";
        String title = "Confirm deletion";
        String noteTitle = note.get().getTitle();

        int choice = JOptionPane.showConfirmDialog(
                null,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            server.send("/app/delete", note.get().getId());
            clear();
            enableContent(false);
            mainCtrl.logRegular("Deleted note '" + noteTitle + "'");
        }
    }

    private void enableContent(boolean b) {
        selectedNoteContent.setDisable(b);
    }

    private void clear() {
        selectedNoteTitle.setText(null);
        selectedNoteContent.setText(null);
    }

    public void emptySearchText() {
        searchText.setText("");
    }

    /**
     * Responsible for refreshing all content in the overview screen.
     */
    public void refresh() {
        enableContent(fetchSelected().isEmpty());

        sendSelectedNoteContentToServer();
        fetchNotes();

        if (wantsToSearch()) search();
        setViewableNotes(notes);
    }

    /**
     * Fetches all notes from the server and stores them locally.
     */
    public void fetchNotes() {
        try {
            notes = server.getIdsAndTitles();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            String errorMessage = "Error retrieving data from the server, unable to refresh notes";
            JOptionPane.showMessageDialog(null, errorMessage, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Updates locally stored {@code selectedNoteId}
     * <p>
     * Since the id is used in many parts of the code, this method seeks to make the code more readable by
     * locally storing the id.
     * </p>
     */
    public void updateSelection() {
        int indexInTable = table.getSelectionModel().getSelectedIndex();
        if (indexInTable == -1) {
            selectedNoteId = -1; // for when nothing is selected
        } else {
            selectedNoteId = table.getSelectionModel().getSelectedItem().getId();
        }
    }

    /**
     * Displays the provided note by filling the title and content in the overview
     *
     * @param note the note to display
     */
    public void show(Note note) {
        selectedNoteTitle.setText(note.getTitle());
        selectedNoteContent.setText(note.getContent());
    }

    /**
     * If a note is selected, shows the title and content in the overview.
     */
    public void showSelectedNote() {
        updateSelection();
        enableContent(getSelectedNoteId().isEmpty());

        if (getSelectedNoteId().isEmpty()) {
            clear();
            return;
        }

        Optional<Note> note = fetchSelected();
        if (note.isEmpty()) return;

        show(note.get());
    }

    /**
     * Displays the provided searchResult by selecting its note and text position
     *
     * @param searchResult the searchResult to display
     */
    public void show(NoteSearchResult searchResult) {
        NotePreview notePreview = searchResult.getNotePreview();

        Optional<Note> note = fetch(notePreview);
        if (note.isEmpty()) return;

        show(note.get());
        select(notePreview);

        // This should be moved to a service class
        int startIndex = searchResult.getStartIndex();
        int endIndex = searchResult.getEndIndex();
        selectedNoteContent.selectRange(startIndex, endIndex);
    }

    /**
     * Select a {@link Note} in the table by its NotePreview
     */
    public void select(NotePreview note) {
        select(note.getId());
    }

    /**
     * Select a {@link Note} in the table by its ID
     */
    public void select(long id) {
        List<Long> ids = table.getItems().stream().map(NotePreview::getId).toList();
        if (!ids.contains(id)) return;

        // Select Note if Note of that ID exists
        table.getSelectionModel().select(ids.indexOf(id));
    }

    /**
     * Select a {@link Note} directly by its index in the table
     */
    public void select(int index) {
        table.getSelectionModel().select(index);
    }

    /**
     * Fetches the {@link Note} corresponding to the {@link NotePreview}
     *
     * @return {@code Optional<Note>} if note is on the server.
     * {@code Optional.empty()} if note isn't on the server.
     */
    public Optional<Note> fetch(NotePreview note) {
        try {
            return Optional.of(server.getNote(note.getId()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * @return {@code Optional<Note>} with the {@code Note} if one is selected.
     * {@code Optional.empty()} if a note isn't selected or doesn't exist on the server.
     */
    public Optional<Note> fetchSelected() {
        if (getSelectedNoteId().isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(server.getNote(getSelectedNoteId().getAsLong()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Sends {@link Note} content to the server.
     * <p>
     * This method verifies whether the currently selected {@link Note} exists on the server
     * and if yes, sends any changes back to the server. If the note is not present,
     * shows an error message using an {@link Alert}.
     * </p>
     */
    public void sendSelectedNoteContentToServer() {
        Optional<Note> note = fetchSelected();
        if (note.isPresent()) {
            updateContentBuffer();
            note.get().content = selectedNoteContentBuffer;
            server.send("/app/update", note.get());
        }
    }

    public void updateContentBuffer() {
        selectedNoteContentBuffer = selectedNoteContent.getText();
    }

    public boolean wantsToSearch() {
        return !searchText.getText().isEmpty();
    }

    /**
     * If there is text in the search bar, displays notes whose title contains the text.
     */
    public void search() {
        String text = searchText.getText();

        if (text.startsWith("#")) {
            List<NoteSearchResult> foundInNotes = searchNoteContent(text.replaceFirst("#", ""));
            setViewableNotes(foundInNotes.stream().map(NoteSearchResult::getNotePreview).distinct().toList());
            mainCtrl.showSearchContent(foundInNotes);
        } else {
            searchAllNotes(text);
        }
    }

    /**
     * Search all notes for occurrences of queryString
     *
     * @param queryString the string to search for inside the content of each note
     * @return a list of NoteSearchResult that contains the note and an index where the searchValue was found
     */
    private List<NoteSearchResult> searchNoteContent(String queryString) {
        List<NoteSearchResult> foundInNotes = new ArrayList<>();
        if (queryString.isEmpty()) return foundInNotes;

        notes.forEach(n -> {
            try {
                Note note = server.getNote(n.getId());
                List<Integer> foundIndices = note.contentSearchQueryString(queryString);
                if (!foundIndices.isEmpty()) {
                    // Indexes should be recalculated when a user clicks on the note
                    // since the note gets updated and could change!!!
                    foundIndices.forEach(i -> foundInNotes.add(new NoteSearchResult(n, i, queryString.length())));
                }
            } catch (ProcessOperationException e) {
                System.out.println(e.getMessage());
                String errorMessage = "Error retrieving data from the server, unable to get note " + n.getTitle();
                JOptionPane.showMessageDialog(null, errorMessage, "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        });

        int amount = foundInNotes.size();
        int noteCount = (int) foundInNotes.stream().map(n -> n.getNotePreview().getId()).distinct().count();
        mainCtrl.logRegular("Found string '" + queryString + "', " + amount + " times, across " + noteCount + " notes");

        return foundInNotes;
    }

    /**
     * Filters the table with all available notes on the provided text
     *
     * @param text the text to search for
     */
    private void searchAllNotes(String text) {
        List<NotePreview> filteredNotes = notes
                .stream()
                .filter(x -> x.getTitle().contains(text))
                .toList();
        setViewableNotes(filteredNotes);
    }

    /**
     * Lists supplied {@link List} of {@link NotePreview} in the {@link TableView}.
     */
    private void setViewableNotes(List<NotePreview> notes) {
        data = FXCollections.observableList(notes);
        table.setItems(data);
    }

    public void showContextMenu(MouseEvent mouseEvent) {
        contextMenu.show(table, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }

    /**
     * Pressed key
     * ENTER:- refresh
     * ESCAPE:- sets focus on to the search bar
     * A:- Shows the window to add a note.
     * Other shortcuts:
     * CTRL+T: edits title of a selected note
     * RIGHT MOUSE CLICK: shows a menu which allows user to delete/refresh/edit a note
     * currently only works when a user right-clicks on the table and not the individual cells.
     *
     * @param e Pressed key
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                refresh();
                break;
            case ESCAPE:
                searchText.requestFocus();
                break;
            case A:
                addNote();
                break;
            default:
                if (e.getCode() == KeyCode.T && e.isControlDown()) {
                    setTitle();
                    break;
                } else {
                    break;
                }
        }
    }

    /**
     * Allows user to change the title if note is selected and exists on the server.
     */
    public void setTitle() {
        Optional<Note> note = fetchSelected();
        if (note.isEmpty()) return;

        try {
            mainCtrl.getNewCtrl().newTitle(
                    server.getNote(note.get().getId())
            );
        } catch (ProcessOperationException e) {
            System.out.println(e.getMessage());
            String errorMessage = "Error retrieving data from the server, unable to fetch note selected note.";
            JOptionPane.showMessageDialog(null, errorMessage, "ERROR", JOptionPane.WARNING_MESSAGE);
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

    public void changeTheme() {
        mainCtrl.changeTheme();
    }

    public List<NotePreview> getNotes() {
        return notes;
    }
}

package client.scenes;

import java.io.InputStream;
import java.net.URL;

import client.Helpers.LanguageHelper;
import client.handlers.ThemeViewHandler;
import client.services.NoteOverviewService;
import client.Helpers.NoteSearchHelper;
import java.util.*;

import client.model.LanguageOption;
import client.utils.AlertUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import client.handlers.NoteSearchResult;
import client.services.Markdown;
import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Note;
import commons.NotePreview;
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

import static commons.exceptions.InternationalizationKeys.*;
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
    private final NoteOverviewService noteOverviewService;
    private final ThemeViewHandler themeViewHandler;
    private final LanguageHelper languageHelper;
    private final NoteSearchHelper noteSearchHelper;
    private final AlertUtils alertUtils;

    private ObservableList<NotePreview> data;
    private List<NotePreview> notes;

    @FXML
    private TableView<NotePreview> table;
    @FXML
    private TableColumn<NotePreview, String> noteTitle;
    @FXML
    private TextField searchText;

    @FXML
    private WebView webView;
    @FXML
    private WebView webViewLogger;
    private final Markdown markdown;

    @FXML
    private Label selectedNoteTitle;
    @FXML
    private TextArea selectedNoteContent;
    @FXML
    private ContextMenu contextMenu;
    private String selectedNoteContentBuffer;
    private long selectedNoteId;
    @FXML
    private ComboBox<LanguageOption> languageComboBox;
    @FXML
    private Button searchButton;
    @FXML
    private Button noteAddButton;
    @FXML
    private Button noteDeleteButton;
    @FXML
    private Button noteRefreshButton;

    /**
     * Instatiate the class using injected parameters
     *
     * @param server   the injected server
     * @param mainCtrl the injected scene
     */
    @Inject
    public NoteOverviewCtrl(NoteUtils server,
                            MainCtrl mainCtrl,
                            NoteOverviewService noteOverviewService,
                            ThemeViewHandler themeViewHandler,
                            LanguageHelper languageHelper,
                            NoteSearchHelper noteSearchHelper,
                            AlertUtils alertUtils,
                            Markdown markdown) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.noteOverviewService = noteOverviewService;
        this.themeViewHandler = themeViewHandler;
        this.languageHelper = languageHelper;
        this.noteSearchHelper = noteSearchHelper;
        this.alertUtils = alertUtils;
        this.markdown = markdown;
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
        selectedNoteContent.textProperty().addListener((_, _, _) -> sendSelectedNoteContentToServer());

        selectedNoteContent.textProperty().addListener((_, _, newValue) -> markdownView(newValue));

        table.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                showContextMenu(event);
            }
        });

        server.registerForMessages("/topic/add", q -> noteOverviewService.initializeServerAdd(data, q));
        server.registerForMessages("/topic/title", q -> {
            NotePreview note = data.stream().filter(n -> n.getId() == q.id).toList().getFirst();
            noteOverviewService.initializeServerTitle(data, note, notes);
            if (selectedNoteTitle.getText().equals(note.getTitle())) selectedNoteTitle.setText(q.title);
        });
        server.registerForMessages("/topic/delete", q -> noteOverviewService.initializeServerDelete(data, q, notes));
        server.registerForMessages("/topic/update", q -> {
            if (selectedNoteId == q.id) selectedNoteContent.setText(q.content);
        });

        languageHelper.initializeLanguageComboBox(languageComboBox);
//        if (mainCtrl.isDarkMode()) changeTheme();
//        System.err.println(mainCtrl.isDarkMode());
//        System.err.println(mainCtrl.getStorage().getTheme());
    }

    public void initIcons(boolean isLightMode) {
        InputStream searchLight = getClass().getResourceAsStream("/icons/search-black.png");
        InputStream searchDark = getClass().getResourceAsStream("/icons/search-white.png");
        InputStream refreshLight = getClass().getResourceAsStream("/icons/refresh-black.png");
        InputStream refreshDark = getClass().getResourceAsStream("/icons/refresh-white.png");
        int size = Integer.parseInt("20");

        if (searchLight != null && searchDark != null && refreshLight != null && refreshDark != null) {
            if (isLightMode) {
                Image imageS = new Image(searchLight);
                ImageView imageViewS = new ImageView(imageS);
                imageViewS.setFitHeight(size); // Set the height
                imageViewS.setFitWidth(size);
                searchButton.setGraphic(imageViewS);
                Image imageR = new Image(refreshLight);
                ImageView imageViewR = new ImageView(imageR);
                imageViewR.setFitHeight(size); // Set the height
                imageViewR.setFitWidth(size);
                noteRefreshButton.setGraphic(imageViewR);
            } else {
                Image imageS = new Image(searchDark);
                ImageView imageViewS = new ImageView(imageS);
                imageViewS.setFitHeight(size); // Set the height
                imageViewS.setFitWidth(size);
                searchButton.setGraphic(imageViewS);
                Image imageR = new Image(refreshDark);
                ImageView imageViewR = new ImageView(imageR);
                imageViewR.setFitHeight(size); // Set the height
                imageViewR.setFitWidth(size);
                noteRefreshButton.setGraphic(imageViewR);
            }
        } else {
            // Handle the error (e.g., log or show a default image)
            System.err.println("Image not found in resources");
        }
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
     */
    public void deleteNote() {
        Optional<Note> note = fetchSelected();
        if (note.isEmpty()) return;

        int choice = noteOverviewService.promptDeleteNote();
        if (choice == JOptionPane.YES_OPTION) {
            server.send("/app/delete", note.get().getId());
            clear();
            enableContent(false);
            mainCtrl.logRegular("Deleted note '" + note.get().getTitle() + "'");

            refresh(); // This is needed to prevent two notes from being removed from the table
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
     * Responsible for refreshing all content in the overview screen without refreshing list.
     */
    public void refreshListPress() {
        enableContent(fetchSelected().isEmpty());

        sendSelectedNoteContentToServer();
        fetchNotes();

        if (wantsToSearch()) search();
    }

    public void showCollectionMenu() {
        mainCtrl.showCollections();
    }

    /**
     * Fetches all notes from the server and stores them locally.
     */
    public void fetchNotes() {
        try {
            notes = server.getIdsAndTitles();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            alertUtils.showError(
                    ERROR,
                    UNABLE_TO_RETRIEVE_DATA,
                    SERVER_ERROR
            );
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
        selectedNoteId = (indexInTable == -1) ? -1 : table.getSelectionModel().getSelectedItem().getId();
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

        selectedNoteContent.selectRange(searchResult.getStartIndex(), searchResult.getEndIndex());
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
        String input = searchText.getText();

        if (input.startsWith("#")) {
            String queryString = input.replaceFirst("#", "");
            List<NoteSearchResult> foundInNotes = noteSearchHelper.searchNoteContent(queryString, notes, server);

            setViewableNotes(foundInNotes.stream().map(NoteSearchResult::getNotePreview).distinct().toList());
            mainCtrl.logRegular(noteSearchHelper.getSearchLogString(foundInNotes, queryString));
            mainCtrl.showSearchContent(foundInNotes);
        } else {
            searchAllNotes(input);
        }
    }

    private void searchAllNotes(String text) {
        setViewableNotes(noteSearchHelper.filterNotes(notes, text));
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
            mainCtrl.showNewTitle();
            if (!mainCtrl.getNewCtrl().getNewNoteTitle().getText().isEmpty()) {
                mainCtrl.getNewCtrl().getNewNoteTitleService().newTitle(
                        note.get(), mainCtrl.getNewCtrl().getNewNoteTitle().getText()
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            alertUtils.showError(
                    ERROR,
                    UNABLE_TO_RETRIEVE_DATA,
                    NOTE_MAY_BE_DELETED
            );
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
        String theme = mainCtrl.changeTheme() ? themeViewHandler.getDarkWebview() : themeViewHandler.getLightWebView();
        webViewLogger.getEngine().executeScript(theme);
        webView.getEngine().executeScript(theme);
        if (!mainCtrl.isDarkMode()) {
            initIcons(true);
            noteAddButton.setStyle("-fx-background-color: lightgreen;");
            noteDeleteButton.setStyle("-fx-background-color:  #FFCCCB;");
        } else {
            initIcons(false);
            noteAddButton.setStyle("-fx-background-color: green;");
            noteDeleteButton.setStyle("-fx-background-color: red;");
        }
    }

    public List<NotePreview> getNotes() {
        return notes;
    }

    public NoteOverviewService getNoteOverviewService() {
        return noteOverviewService;
    }
}

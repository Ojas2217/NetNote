package client.scenes;

import client.handlers.NoteSearchResult;
import com.google.inject.Inject;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Controller class for handling note content searches
 */
public class SearchNoteContentCtrl {
    MainCtrl mainCtrl;

    @FXML
    private TableView<NoteSearchResult> tableViewResult;
    @FXML
    private TableColumn<NoteSearchResult, String> titleColumn;
    @FXML
    private TableColumn<NoteSearchResult, Integer> indexColumn;

    private List<NoteSearchResult> searchResult;

    @Inject
    public SearchNoteContentCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initialize whenever the SearchNoteContent is started
     * Initializes the tableViewResult so it has an eventHandler whenever a SearchValueResult is selected
     */
    public void init() {
        tableViewResult.getSelectionModel().selectedIndexProperty()
                .addListener((_, _, newSelectionIndex) -> {
                    if (newSelectionIndex != null) {
                        int newIndex = (Integer) newSelectionIndex;
                        if (newIndex != -1) handleRowSelection(newIndex);
                    }
                });
    }

    /**
     * Sets the tableViewResult based on the provided searchResults
     *
     * @param searchResults the list of NoteSearchResult where a certain queryString was found
     */
    public void setSearchResult(List<NoteSearchResult> searchResults) {
        this.searchResult = searchResults;

        titleColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getNotePreview().getTitle()));
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("startIndex"));

        ObservableList<NoteSearchResult> data = FXCollections.observableArrayList(searchResults);
        tableViewResult.setItems(data);
    }

    private void handleRowSelection(int selectionIndex) {
        NoteSearchResult selectedNote = searchResult.get(selectionIndex);
        mainCtrl.showOverview(selectedNote);
    }
}

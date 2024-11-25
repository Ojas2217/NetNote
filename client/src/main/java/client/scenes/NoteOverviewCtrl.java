package client.scenes;

import java.net.URL;
import java.util.ResourceBundle;

import client.utils.NoteUtils;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Note;
import commons.ProcessOperationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

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

    public void addNote(){
        mainCtrl.showAdd();
    }
    public void deleteNote() throws ProcessOperationException {
        Note note = table.getSelectionModel().getSelectedItem();
        if(note != null) {
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


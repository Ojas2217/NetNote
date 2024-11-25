package client.scenes;

import client.utils.NoteUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Note;
import commons.ProcessOperationException;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.util.List;

public class AddNoteControl {
    private final NoteUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextArea currentNote;
    @FXML
    private ListView<Note> availableNotes;
    @FXML
    private TextField noteTitle;
    private List<Note> notes;

    @Inject
    public AddNoteControl(NoteUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public void ok() {
        try {
            server.createNote(new Note(noteTitle.getText(),"empty"));
        } catch (WebApplicationException | ProcessOperationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        mainCtrl.showOverview();
    }

    private void clearFields() {
        noteTitle.clear();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }
}

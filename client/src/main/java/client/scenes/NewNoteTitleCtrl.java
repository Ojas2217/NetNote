package client.scenes;


import com.google.inject.Inject;
import commons.Note;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class NewNoteTitleCtrl {
    private final MainCtrl mainCtrl;
    @FXML
    private TextField newNoteTitle;
    @FXML
    private TableView<Note> table;

    @Inject
    public NewNoteTitleCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public void ok(Note note) {
        if (note != null) {
            String newTitle = newNoteTitle.getText();
            note.setTitle(newTitle);
        }
        mainCtrl.showOverview();
        clearFields();
    }

    private void clearFields() {
        newNoteTitle.clear();
    }

    public void keyPressed(KeyEvent e, Note note) {
        switch (e.getCode()) {
            case ENTER:
                ok(note);
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

}

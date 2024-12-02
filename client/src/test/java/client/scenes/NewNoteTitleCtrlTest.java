package client.scenes;

import client.utils.NoteUtils;
import commons.Note;
import commons.ProcessOperationException;
import javafx.application.Platform;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewNoteTitleCtrlTest {
    private NewNoteTitleCtrl newNoteTitleCtrl;
    private NoteUtils server;
    private MainCtrl mainCtrl;
    private NoteOverviewCtrl noteOverviewCtrl;
    private Note note;
    private TextField newNoteTitle;
    private TableView<Note> noteTable;
    private TableView.TableViewSelectionModel<Note> selectionModel;

    @BeforeEach
    public void setUp() throws InterruptedException {
        server = mock(NoteUtils.class);
        mainCtrl = mock(MainCtrl.class);
        noteOverviewCtrl = mock(NoteOverviewCtrl.class);
        noteTable = mock(TableView.class);
        selectionModel = mock(TableView.TableViewSelectionModel.class);

        when(noteOverviewCtrl.getNote()).thenReturn(note);
        when(noteTable.getSelectionModel()).thenReturn(selectionModel);

        newNoteTitleCtrl = new NewNoteTitleCtrl(mainCtrl, server);

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            newNoteTitle = new TextField("new title");
            newNoteTitleCtrl.setNewNoteTitle(newNoteTitle);
            note = new Note("old title","empty");
            latch.countDown();
        });
        latch.await();
        try {
            when(server.createNote(note)).thenReturn(note);
            when(server.getAllNotes()).thenReturn(List.of(note));
        } catch (ProcessOperationException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void newTitleTest(){
        newNoteTitleCtrl.newTitle(note);
        try {
            assertEquals(server.getAllNotes().get(0).getTitle(),"new title");
        } catch (ProcessOperationException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void keyPressedTest(){
        when(mainCtrl.getOverviewCtrl()).thenReturn(noteOverviewCtrl);
        when(noteTable.getSelectionModel()).thenReturn(selectionModel);
        when(noteOverviewCtrl.getNote()).thenReturn(note);
        newNoteTitle.setText("new title");
        KeyEvent test = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "",
                "",
                KeyCode.ENTER,
                false, false, false, false
        );
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() ->{
            newNoteTitleCtrl.keyPressed(test);
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals("new title", note.getTitle());
    }
}

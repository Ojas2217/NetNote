package client.scenes;

import client.utils.NoteUtils;
import commons.Note;
import commons.ProcessOperationException;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import javafx.scene.control.TextField;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class AddNoteControlTest extends BaseTest {
    private AddNoteControl addNoteControl;
    private NoteUtils server;
    private MainCtrl mainCtrl;
    private TextField title;

    @BeforeEach
    public void setUp() throws InterruptedException {
        server = mock(NoteUtils.class);
        mainCtrl = mock(MainCtrl.class);
        addNoteControl = new AddNoteControl(server, mainCtrl);
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            title = new TextField();
            addNoteControl.setNoteTitle(title);
            latch.countDown();
        });
        latch.await();
    }

    @Test
    public void okTestNoteTitleNotNull() throws ProcessOperationException {
        title=new TextField("hello");
        Note note = new Note(title.getText(),"empty");
        when(server.createNote(note)).thenReturn(note);
        when(server.getAllNotes()).thenReturn(List.of(note));
        Platform.runLater(() -> addNoteControl.ok());
        assertEquals(server.getAllNotes().getFirst(),note);
    }
    @Disabled
    @Test
    public void okTestNoteTitleNull() throws ProcessOperationException {
        Note note = new Note("","empty");
        when(server.createNote(note)).thenReturn(note);
        when(server.getAllNotes()).thenReturn(List.of(note));
        Platform.runLater(() -> addNoteControl.ok());
        assertTrue(server.getAllNotes().get(0).title.isEmpty());
    }

    @Test
    public void keyPressTest() throws ProcessOperationException {
        title.setText("Test Note");
        Note note = new Note(title.getText(), "empty 123 testing 123 format");
        when(server.createNote(note)).thenReturn(note);
        when(server.getAllNotes()).thenReturn(List.of(note));
        KeyEvent test = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "",
                "",
                KeyCode.ENTER,
                false, false, false, false
        );
        Platform.runLater(() -> addNoteControl.keyPressed(test));
        assertEquals(server.getAllNotes().get(0), note);
    }


}

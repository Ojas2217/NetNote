package client.business;

import client.scenes.MainCtrl;
import client.scenes.NoteOverviewCtrl;
import client.utils.NoteUtils;
import commons.Note;
import commons.NotePreview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class AddNoteServiceTest {
    private NoteUtils server;
    private MainCtrl mainCtrl;
    private AddNoteService service;

    @BeforeEach
    void setUp() {
        server = mock(NoteUtils.class);
        mainCtrl = mock(MainCtrl.class);
        NoteOverviewCtrl overviewCtrl = mock(NoteOverviewCtrl.class);
        when(mainCtrl.getOverviewCtrl()).thenReturn(overviewCtrl);
        service = new AddNoteService(server, mainCtrl);
    }


    @Disabled
    @Test
    public void addNoteTest() {
        service.addNote("Test Note");

        ArgumentCaptor<Note> noteCaptor = ArgumentCaptor.forClass(Note.class);
        verify(server).send(eq("/app/add"), noteCaptor.capture());

        Note capturedNote = noteCaptor.getValue();
        assertEquals("Test Note", capturedNote.getTitle());
        assertEquals("empty 123 testing 123 format", capturedNote.getContent());
    }

    @Test
    void isUniqueEmptyListTest() {
        when(mainCtrl.getOverviewCtrl().getNotes()).thenReturn(null);

        boolean result = service.isUnique("Test Title");

        assertTrue(result, "Expected isUnique to return true when the note list is empty.");
    }

    @Test
    public void isUniqueFalseTest() {
        List<NotePreview> notes = List.of(
                new NotePreview(1L, "Existing Note"),
                new NotePreview(2L, "Another Note")
        );
        when(mainCtrl.getOverviewCtrl().getNotes()).thenReturn(notes);

        boolean result = service.isUnique("Existing Note");

        assertFalse(result);
    }

    @Test
    public void isUniqueTrueTest() {
        List<NotePreview> notes = List.of(
                new NotePreview(1L, "Existing Note"),
                new NotePreview(2L, "Another Note")
        );
        when(mainCtrl.getOverviewCtrl().getNotes()).thenReturn(notes);

        boolean result = service.isUnique("Unique Note");

        assertTrue(result);
    }
}

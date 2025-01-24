package client.business;

import client.scenes.CollectionOverviewCtrl;
import client.scenes.MainCtrl;
import client.scenes.NoteOverviewCtrl;
import client.utils.NoteUtils;
import commons.Collection;
import commons.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NewNoteTitleServiceTest {

    private NewNoteTitleService service;

    @BeforeEach
    public void setUp() {
        NoteUtils server = mock(NoteUtils.class);  // Create the mock for NoteUtils
        MainCtrl mainCtrl = mock(MainCtrl.class);
        NoteOverviewCtrl overviewCtrl = mock(NoteOverviewCtrl.class);
        CollectionOverviewCtrl collectionOverviewCtrl = mock(CollectionOverviewCtrl.class);
        when(mainCtrl.getCollectionOverviewCtrl()).thenReturn(collectionOverviewCtrl);
        when(mainCtrl.getOverviewCtrl()).thenReturn(overviewCtrl);
        Collection collection = mock(Collection.class);
        when(overviewCtrl.getSelectedCollection()).thenReturn(collection);
        service = new NewNoteTitleService(server, mainCtrl);  // Instantiate the service with the mock
    }

    @Test
    public void newTitleTrue() throws Exception {
        Note note = new Note("Old Title", "Content");
        String newTitle = "New Title";


        service.newTitle(note, newTitle);

        assertEquals(newTitle, note.getTitle(), "The note's title should be updated.");
    }

    @Test
    public void newTitleNotNull() {
        Note note = new Note("Old Title", "Content");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.newTitle(note, null));
        assertEquals("Note or title is invalid.", exception.getMessage(), "Should throw exception when title is null.");
    }

    @Test
    public void newTitleNotEmpty() {
        Note note = new Note("Old Title", "Content");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.newTitle(note, ""));
        assertEquals("Note or title is invalid.", exception.getMessage(), "Should throw exception when title is empty.");
    }

    @Test
    public void noNewTitleServerFail() throws Exception {
        NoteUtils mockNoteUtils = mock(NoteUtils.class);
        MainCtrl mainCtrl = mock(MainCtrl.class);
        NoteOverviewCtrl overviewCtrl = mock(NoteOverviewCtrl.class);
        CollectionOverviewCtrl collectionOverviewCtrl = mock(CollectionOverviewCtrl.class);
        when(mainCtrl.getCollectionOverviewCtrl()).thenReturn(collectionOverviewCtrl);
        when(mainCtrl.getOverviewCtrl()).thenReturn(overviewCtrl);
        Collection collection = mock(Collection.class);
        when(overviewCtrl.getSelectedCollection()).thenReturn(collection);
        Note note = new Note("Old Title", "Some content");


        doThrow(new RuntimeException("Server error")).when(mockNoteUtils).send(eq("/app/title"), eq(note));
        NewNoteTitleService service = new NewNoteTitleService(mockNoteUtils, mainCtrl);
        Exception exception = assertThrows(Exception.class, () -> {
            service.newTitle(note, "New Title");
        });

        assertEquals("Failed to update the note title. Server error", exception.getMessage());
        assertEquals("Old Title", note.getTitle());
    }








}

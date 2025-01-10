package client.business;

import client.utils.NoteUtils;
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
        service = new NewNoteTitleService(server);  // Instantiate the service with the mock
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
        Note note = new Note("Old Title", "Some content");
        NoteUtils mockNoteUtils = mock(NoteUtils.class);

        doThrow(new RuntimeException("Server error")).when(mockNoteUtils).send(eq("/app/title"), eq(note));
        NewNoteTitleService service = new NewNoteTitleService(mockNoteUtils);
        Exception exception = assertThrows(Exception.class, () -> {
            service.newTitle(note, "New Title");
        });

        assertEquals("Failed to update the note title. Server error", exception.getMessage());
        assertEquals("Old Title", note.getTitle());
    }








}

package server.api;

import commons.Note;
import commons.ProcessOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.service.NoteService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NoteControllerTest {
    private NoteService noteService;
    private NoteController noteController;

    @BeforeEach
    public void setUp() {
        noteService = mock(NoteService.class);
        noteController = new NoteController(noteService);
    }

    @Test
    public void testGetAll() {
        Note note = new Note("titel", "content");
        when(noteService.getAllNotes()).thenReturn(List.of(note));
        List<Note> result = noteController.getAll();
        assertEquals(List.of(note), result);
    }

    @Test
    public void testGetByIdNegative() throws ProcessOperationException {
        when(noteService.getNoteById(Long.valueOf(-1))).thenThrow(ProcessOperationException.class);
        ResponseEntity<Note> result = noteController.getById(-1);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testGetByIdNonExistent() throws ProcessOperationException {
        long id = 1L;
        when(noteService.getNoteById(id)).thenThrow(ProcessOperationException.class);
        ResponseEntity<Note> result = noteController.getById(id);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testGetByIdCorrect() throws ProcessOperationException {
        Note note = new Note("titel", "content");
        Long id = 1L;
        when(noteService.noteExistsById(id)).thenReturn(true);
        when(noteService.getNoteById(id)).thenReturn(note);
        ResponseEntity<Note> result = noteController.getById(id);
        assertEquals(note, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testAddNullTitle() {
        Note note = new Note(null, "content");
        when(noteController.add(note)).thenThrow(ProcessOperationException.class);
        ResponseEntity<Note> result = noteController.add(note);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testAddNullContent() throws ProcessOperationException {
        Note note = new Note("title", null);
        when(noteService.createNote(note)).thenReturn(note);
        ResponseEntity<Note> result = noteController.add(note);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testAddCorrect() throws ProcessOperationException {
        Note note = new Note("title", "content");
        when(noteService.createNote(note)).thenReturn(note);
        ResponseEntity<Note> result = noteController.add(note);
        assertEquals(note, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testDeleteNodeIdNull() {
        Long id = null;
        ResponseEntity<Note> result = noteController.deleteNoteById(id);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testDeleteNodeNoteNonExistent() throws ProcessOperationException {
        Note note = new Note("title", "content");
        Long id = 1L;
        when(noteService.deleteNoteById(id)).thenReturn(note);
        ResponseEntity<Note> result = noteController.deleteNoteById(id);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testDeleteNodeCorrect() throws ProcessOperationException {
        Note note = new Note("title", "content");
        Long id = 1L;
        when(noteService.deleteNoteById(id)).thenReturn(note);
        ResponseEntity<Note> result = noteController.deleteNoteById(id);
        assertEquals(note, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testUpdateNoteCorrect() throws ProcessOperationException {
        Note note = new Note("title", "content");
        when(noteService.updateNote(note)).thenReturn(note);
        ResponseEntity<Note> result = noteController.update(note);
        assertEquals(note, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testUpdateNoteNonExistent() throws ProcessOperationException {
        Note note = new Note("title", "content");
        when(noteService.updateNote(note)).thenThrow(ProcessOperationException.class);
        ResponseEntity<Note> result = noteController.update(note);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testUpdateNoteInvalidTitle() throws ProcessOperationException {
        Note note1 = new Note(null, "content");
        Note note2 = new Note("", "content");
        when(noteService.updateNote(note1)).thenThrow(ProcessOperationException.class);
        when(noteService.updateNote(note2)).thenThrow(ProcessOperationException.class);
        ResponseEntity<Note> result1 = noteController.update(note1);
        ResponseEntity<Note> result2 = noteController.update(note2);
        assertEquals(HttpStatus.BAD_REQUEST, result1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, result2.getStatusCode());
    }
}
package server.api;

import commons.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.NoteRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NoteControllerTest {
    private NoteRepository noteRepository;
    private NoteController noteController;

    @BeforeEach
    public void setUp() {
        noteRepository = mock(NoteRepository.class);
        noteController = new NoteController(noteRepository);
    }

    @Test
    public void testGetAll() {
        Note note = new Note("titel", "content");
        when(noteRepository.findAll()).thenReturn(List.of(note));
        List<Note> result = noteController.getAll();
        assertEquals(List.of(note), result);
    }

    @Test
    public void testGetByIdNegative() {
        ResponseEntity<Note> result = noteController.getById(-1);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testGetByIdNonExistent() {
        long id = 1L;
        when(noteRepository.existsById(id)).thenReturn(false);
        ResponseEntity<Note> result = noteController.getById(id);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testGetByIdCorrect() {
        Note note = new Note("titel", "content");
        Long id = 1L;
        when(noteRepository.existsById(id)).thenReturn(true);
        when(noteRepository.findById(id)).thenReturn(Optional.of(note));
        ResponseEntity<Note> result = noteController.getById(id);
        assertEquals(note, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testAddNullTitle() {
        Note note = new Note(null, "content");
        ResponseEntity<Note> result = noteController.add(note);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testAddNullContent() {
        Note note = new Note("title", null);
        ResponseEntity<Note> result = noteController.add(note);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testAddCorrect() {
        Note note = new Note("title", "content");
        when(noteRepository.save(note)).thenReturn(note);
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
    public void testDeleteNodeNoteNonExistent() {
        Note note = new Note("title", "content");
        Long id = 1L;
        ResponseEntity<Note> result = noteController.deleteNoteById(id);
        when(noteRepository.findById(id)).thenReturn(Optional.empty());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testDeleteNodeCorrect() {
        Note note = new Note("title", "content");
        Long id = 1L;
        when(noteRepository.findById(id)).thenReturn(Optional.of(note));
        ResponseEntity<Note> result = noteController.deleteNoteById(id);
        assertEquals(note, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
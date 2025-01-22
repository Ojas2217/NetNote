package server.api;

import commons.Collection;
import commons.Note;
import commons.NoteCollectionPair;
import commons.NotePreview;
import commons.exceptions.ProcessOperationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.CollectionRepository;
import server.database.NoteRepository;
import server.service.NoteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NoteServiceTest {
    private NoteService noteService;
    private NoteRepository repo;
    private CollectionRepository collectionRepo;

    @BeforeEach
    public void initBeforeEach() {
        this.repo = mock(NoteRepository.class);
        this.collectionRepo = mock(CollectionRepository.class);
        this.noteService = new NoteService(repo, collectionRepo);
    }

    @Test
    public void testGetAllNotesEmpty() {
        List<Note> result = noteService.getAllNotes();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllNotesCorrect() {
        List<Note> expected = List.of(new Note("1", "test"), new Note("2", "test"));

        when(repo.findAll()).thenReturn(List.of(new Note("1", "test"), new Note("2", "test")));
        List<Note> result = noteService.getAllNotes();
        Assertions.assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testGetNoteByIdIncorrect() {
        when(repo.existsById(0L)).thenReturn(false);
        Assertions.assertThrows(ProcessOperationException.class, () -> {
            noteService.getNoteById(0L);
        });
    }

    @Test
    public void testGetNoteByIdCorrect() {
        Note expected = new Note("1", "test");

        when(repo.existsById(0L)).thenReturn(true);
        when(repo.findById(0L)).thenReturn(Optional.of(new Note("1", "test")));

        Assertions.assertDoesNotThrow(() -> {
            Note result = noteService.getNoteById(0L);
            Assertions.assertEquals(expected, result);
        });
    }

    @Test
    public void testCreateNoteTitleNull() {
        Note newNote = new Note(null, "test");

        when(repo.save(newNote)).thenReturn(newNote);
        Assertions.assertThrows(ProcessOperationException.class, () -> {
            noteService.createNote(newNote);
        });
    }

    @Test
    public void testCreateNoteEmptyTitle() {
        Note newNote = new Note("", "test");

        when(repo.save(newNote)).thenReturn(newNote);
        Assertions.assertThrows(ProcessOperationException.class, () -> {
            noteService.createNote(newNote);
        });
    }

    @Test
    public void testCreateNoteCorrect() {
        Note newNote = new Note("1", "test");

        when(repo.save(newNote)).thenReturn(newNote);
        Note result = repo.save(newNote);
        Assertions.assertEquals(newNote, result);
    }

    @Test
    public void testUpdateNoteTitleNull() {
        Note updateNote = new Note(null, "test");

        when(repo.existsById(0L)).thenReturn(false);
        Assertions.assertThrows(ProcessOperationException.class, () -> {
            noteService.updateNote(updateNote);
        });
    }

    @Test
    public void testUpdateIllegalTitle() {
        Note oldNote = new Note("1", "old");
        Note expected = new Note(null, "new");

        when(repo.existsById(0L)).thenReturn(true);
        when(repo.findById(0L)).thenReturn(Optional.of(oldNote));
        when(repo.save(oldNote)).thenReturn(oldNote);

        Assertions.assertThrows(ProcessOperationException.class, () -> {
            noteService.updateNote(expected);
        });
    }

    @Test
    public void testUpdateNoteCorrect() {
        Note oldNote = new Note("1", "old");
        Note expected = new Note("2", "new");

        when(repo.existsById(0L)).thenReturn(true);
        when(repo.findById(0L)).thenReturn(Optional.of(oldNote));
        when(repo.save(oldNote)).thenReturn(oldNote);

        Assertions.assertDoesNotThrow(() -> {
            Note result = noteService.updateNote(expected);
            Assertions.assertEquals(expected, result);
        });
    }

    @Test
    public void testDeleteNoteByIdDoesNotExist() {
        Note noteToSearch = new Note("1", "test");

        when(repo.findById(0L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProcessOperationException.class, () -> {
            noteService.deleteNoteById(0L);
        });
    }

    @Test
    public void testDeleteNoteByIdCorrect() {
        Note expected = new Note("1", "test");

        when(repo.findById(0L)).thenReturn(Optional.of(expected));

        Assertions.assertDoesNotThrow(() -> {
            Note result = noteService.deleteNoteById(0L);
            Assertions.assertEquals(expected, result);
        });
    }

    @Test
    public void testGetIdsAndTitlesNoNotes() {
        when(repo.findIdAndTitle()).thenReturn(new ArrayList<>());
        List<NotePreview> result = noteService.getIdsAndTitles();
        Assertions.assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void testGetIdsAndTitlesCorrect() {
        List<NotePreview> expected = List.of(new NotePreview(0L, "1"), new NotePreview(1L, "2"));

        Object[] notePreview1Array = Stream.of(0L, "1").map(c -> (Object)c).toArray();
        Object[] notePreview2Array = Stream.of(1L, "2").map(c -> (Object)c).toArray();
        List<Object[]> returnList = List.of(notePreview1Array, notePreview2Array);

        when(repo.findIdAndTitle()).thenReturn(returnList);
        List<NotePreview> result = noteService.getIdsAndTitles();
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testAssignNoteToCollectionCollectionDoesNotExist() {
        Note note = new Note("1", "test");
        Collection collectionExpected = new Collection("collection", new ArrayList<>());
        NoteCollectionPair collectionPairExpected = new NoteCollectionPair(note, collectionExpected);

        when(collectionRepo.findById(0L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProcessOperationException.class, () -> {
            noteService.assignNoteToCollection(collectionPairExpected);
        });
    }

    @Test
    public void testAssignNoteToCollectionNoteDoesNotExist() {
        Note note = new Note("1", "test");
        Collection collectionExpected = new Collection("collection", new ArrayList<>());
        NoteCollectionPair collectionPairExpected = new NoteCollectionPair(note, collectionExpected);

        when(collectionRepo.findById(0L)).thenReturn(Optional.of(collectionExpected));
        when(repo.findById(0L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProcessOperationException.class, () -> {
            noteService.assignNoteToCollection(collectionPairExpected);
        });
    }

    @Test
    public void testAssignNoteToCollectionCorrect() {
        Note note = new Note("1", "test");
        Collection collectionExpected = new Collection("collection", new ArrayList<>());
        NoteCollectionPair collectionPairExpected = new NoteCollectionPair(note, collectionExpected);

        when(collectionRepo.findById(0L)).thenReturn(Optional.of(collectionExpected));
        when(repo.findById(0L)).thenReturn(Optional.of(note));

        Assertions.assertDoesNotThrow(() -> {
            NoteCollectionPair result = noteService.assignNoteToCollection(collectionPairExpected);
            Assertions.assertEquals(collectionExpected, result.getNote().getCollection());
        });
    }
}

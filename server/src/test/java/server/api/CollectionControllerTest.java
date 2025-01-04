package server.api;

import commons.Collection;
import commons.Note;
import commons.exceptions.ProcessOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.CollectionRepository;
import server.service.CollectionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CollectionControllerTest {
    private CollectionRepository collectionRepository;
    private CollectionService collectionService;

    @BeforeEach
    public void setUp() {
        collectionRepository = mock(CollectionRepository.class);
        collectionService = new CollectionService(collectionRepository);
    }

    @Test
    public void createCollection() throws ProcessOperationException {
        Note note = new Note("title1", "content1");
        Collection collection = new Collection("Collection 1", List.of(note));

        when(collectionRepository.save(collection)).thenReturn(collection);

        Collection result = collectionService.createCollection(collection);

        assertEquals("Collection 1", result.getName());
        assertEquals("title1", result.getNotes().getFirst().getTitle());
    }

    @Test
    public void getAllCollections() {
        Note note1 = new Note("title1", "content1");
        Note note2 = new Note("title2", "content2");
        Collection collection1 = new Collection("Collection 1", List.of(note1));
        Collection collection2 = new Collection("Collection 2", List.of(note2));

        when(collectionRepository.findAll()).thenReturn(List.of(collection1, collection2));

        List<Collection> result = collectionService.getAllCollections();

        assertEquals(2, result.size());
        assertEquals("Collection 1", result.getFirst().getName());
        assertEquals("title1", result.getFirst().getNotes().getFirst().getTitle());
    }

    @Test
    public void getCollectionById() throws ProcessOperationException {
        Note note = new Note("title1", "content1");
        Collection collection = new Collection("Collection 1", List.of(note));

        Long collectionId = collection.getId();  // Capture the generated ID

        when(collectionRepository.existsById(collectionId)).thenReturn(true);
        when(collectionRepository.findById(collectionId)).thenReturn(Optional.of(collection));

        Collection result = collectionService.getCollectionById(collectionId);

        assertEquals("Collection 1", result.getName());
        assertEquals("title1", result.getNotes().getFirst().getTitle());
    }

    @Test
    public void getCollectionInvalidId() {
        Note note = new Note("title1", "content1");
        Collection collection = new Collection("Collection 1", List.of(note));
        Long collectionId = collection.getId();

        when(collectionRepository.existsById(collectionId)).thenReturn(false);

        ProcessOperationException exception = assertThrows(
                ProcessOperationException.class,
                () -> collectionService.getCollectionById(collectionId)
        );
        assertEquals("Invalid Collection ID", exception.getMessage());
    }

    @Test
    public void collectionEmptyName() {
        Collection collection = new Collection("", List.of());

        ProcessOperationException exception = assertThrows(
                ProcessOperationException.class,
                () -> collectionService.createCollection(collection)
        );
        assertEquals("Collection name cannot be empty", exception.getMessage());
    }

    @Test
    public void deleteCollectionById() throws ProcessOperationException {
        Note note1 = new Note("title1", "content1");
        Note note2 = new Note("title2", "content2");
        Collection collection1 = new Collection("Collection 1", List.of(note1));
        Collection collection2 = new Collection("Collection 2", List.of(note2));

        Long collectionId1 = collection1.getId();

        List<Collection> collections = List.of(collection1, collection2);

        when(collectionRepository.findAll()).thenReturn(collections);
        when(collectionRepository.findById(collectionId1)).thenReturn(Optional.of(collection1));

        List<Collection> initialResult = collectionService.getAllCollections();
        assertEquals(2, initialResult.size());

        collectionService.deleteCollectionById(collectionId1);

        when(collectionRepository.findAll()).thenReturn(List.of(collection2));

        List<Collection> finalResult = collectionService.getAllCollections();
        assertEquals(1, finalResult.size());
    }


    @Test
    public void deleteCollectionInvalidId() {
        when(collectionRepository.findById(1L)).thenReturn(Optional.empty());

        ProcessOperationException exception = assertThrows(
                ProcessOperationException.class,
                () -> collectionService.deleteCollectionById(1L)
        );
        assertEquals("Collection not found", exception.getMessage());
    }

    @Test
    public void collectionNullNotes() {
        Collection collection = new Collection("Valid Name", null);

        ProcessOperationException exception = assertThrows(
                ProcessOperationException.class,
                () -> collectionService.createCollection(collection)
        );
        assertEquals("Notes list cannot be null", exception.getMessage());
    }

    @Test
    public void collectionEmptyNotes() throws ProcessOperationException {
        Collection collection = new Collection("Empty Notes Collection", List.of());

        when(collectionRepository.save(collection)).thenReturn(collection);

        Collection result = collectionService.createCollection(collection);

        assertEquals("Empty Notes Collection", result.getName());
        assertTrue(result.getNotes().isEmpty());
    }

    @Test
    public void UpdateCollectionInvalidId() {
        Note note = new Note("updateTitle", "updateContent");
        Collection collection = new Collection("Updated Collection", List.of(note));

        when(collectionRepository.existsById(collection.getId())).thenReturn(false);

        ProcessOperationException exception = assertThrows(
                ProcessOperationException.class,
                () -> collectionService.updateCollection(collection)
        );
        assertEquals("Invalid collection ID or missing name", exception.getMessage());
    }

    @Test
    public void deleteCollectionWithNotes() throws ProcessOperationException {
        Note note = new Note("noteTitle", "noteContent");
        Collection collection = new Collection("Collection with Notes", List.of(note));

        Long collectionId = collection.getId();

        when(collectionRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collectionRepository.existsById(collectionId)).thenReturn(true);

        collectionService.deleteCollectionById(collectionId);

        when(collectionRepository.findById(collectionId)).thenReturn(Optional.empty());

        ProcessOperationException exception = assertThrows(
                ProcessOperationException.class,
                () -> collectionService.deleteCollectionById(collectionId)
        );
        assertEquals("Collection not found", exception.getMessage());
    }

    @Test
    public void createCollectionWithManyNotes() throws ProcessOperationException {
        List<Note> notes = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            notes.add(new Note("Title " + i, "Content " + i));
        }
        Collection collection = new Collection("Large Notes Collection", notes);

        when(collectionRepository.save(collection)).thenReturn(collection);
        Collection result = collectionService.createCollection(collection);
        assertEquals(1000, result.getNotes().size());
    }


}
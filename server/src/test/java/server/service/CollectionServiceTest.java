package server.service;

import commons.Collection;
import commons.Note;
import commons.exceptions.ProcessOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.CollectionRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollectionServiceTest {

    private CollectionService collectionService;
    private Note note;
    private Note note1;
    private Collection collection;

    @Mock
    private CollectionRepository db;

    @BeforeEach
    void before() {
        MockitoAnnotations.openMocks(this);
        collectionService = new CollectionService(db);
        note = new Note("r2", "d2");
        note1 = new Note("c3", "po");
        collection = new Collection("bb", List.of(note, note1));
    }

    @Test
    void testGetAllCollections() {
        List<Collection> collectionList = List.of(
                new Collection("A New Hope"),
                new Collection("Empire Strikes Back"),
                new Collection("Return of the Jedi")
        );
        when(db.findAll()).thenReturn(collectionList);
        List<Collection> result = collectionService.getAllCollections();
        assertEquals(3, result.size());
        assertEquals("A New Hope", result.get(0).getName());
        assertEquals("Empire Strikes Back", result.get(1).getName());
        assertEquals("Return of the Jedi", result.get(2).getName());
        verify(db, times(1)).findAll();
    }

    @Test
    void byIdTest() throws ProcessOperationException {
        Collection collection = new Collection("empire strikes back");
        when(db.existsById(1L)).thenReturn(true);
        when(db.findById(1L)).thenReturn(Optional.of(collection));
        Collection finals = collectionService.getCollectionById(1L);
        assertEquals("empire strikes back", finals.getName());
    }

    @Test
    void createCollectionTest() throws ProcessOperationException {
        Collection collection = new Collection("mandalorian");
        when(db.save(collection)).thenReturn(collection);
        Collection result = collectionService.createCollection(collection);
        assertEquals("mandalorian", result.getName());
        verify(db, times(1)).save(collection);
    }

    @Test
    void deleteCollectionTest() {
        Long collectionId = collection.getId();
        when(db.findById(collectionId)).thenReturn(Optional.of(collection));
        when(db.existsById(collectionId)).thenReturn(true);
        when(db.findById(collectionId)).thenReturn(Optional.empty());

        ProcessOperationException throwing = assertThrows(
                ProcessOperationException.class, () -> collectionService.deleteCollectionById(collectionId)
        );
        assertEquals("Collection not found", throwing.getMessage());
    }
}
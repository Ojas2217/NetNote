package client.business;

import client.scenes.CollectionOverviewCtrl;
import client.scenes.MainCtrl;
import client.utils.CollectionUtils;
import commons.Collection;
import commons.exceptions.ProcessOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddCollectionServiceTest {
    private CollectionUtils server;
    private MainCtrl mainCtrl;
    public AddCollectionService service;

    @BeforeEach
    void setUp() {
        server = mock(CollectionUtils.class);
        mainCtrl = mock(MainCtrl.class);
        CollectionOverviewCtrl collectionOverviewCtrl = mock(CollectionOverviewCtrl.class);
        when(mainCtrl.getCollectionOverviewCtrl()).thenReturn(collectionOverviewCtrl);
        service = new AddCollectionService(server,mainCtrl);

    }

    @Test
    public void addCollectionTest() throws ProcessOperationException {
        Collection collection = new Collection("Test Collection", new ArrayList<>());
        service.addCollection(collection.getName());
        List<Collection> collections = new ArrayList<>();
        collections.add(collection);
        when(server.getAllCollections()).thenReturn(collections);
        assertEquals(server.getAllCollections().get(0).getName(), collection.getName());
    }

    @Test
    public void isUniqueTest() {
        List<Collection> collections = new ArrayList<>();
        collections.add(new Collection("Test Collection", new ArrayList<>()));
        when(mainCtrl.getCollectionOverviewCtrl().fetchCollections()).thenReturn(collections);
        assertEquals(service.isUnique("Test Collection"), false);
    }

    @Test
    public void addCollectionThrowsExceptionTest() throws ProcessOperationException {
        when(server.createCollection(any())).thenThrow(ProcessOperationException.class);

        assertThrows(RuntimeException.class, () -> {
            service.addCollection("Test Collection");
        });
    }

    @Test
    public void isUniqueThrowsExceptionTest() {
        when(mainCtrl.getCollectionOverviewCtrl().fetchCollections()).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(RuntimeException.class, () -> {
            service.isUnique("Test Collection");
        });
    }

    @Test
    public void addCollectionNullServerTest() {
        service = new AddCollectionService(null, mainCtrl);

        assertThrows(NullPointerException.class, () -> {
            service.addCollection("Test Collection");
        });
    }

    @Test
    public void isUniqueNullCollectionListTest() {
        when(mainCtrl.getCollectionOverviewCtrl().fetchCollections()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            service.isUnique("Test Collection");
        });
    }

    @Test
    public void addCollectionEmptyTitleTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addCollection("");
        });
    }

    @Test
    public void addCollectionNullTitleTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addCollection(null);
        });
    }

    @Test
    public void addCollectionThrowsProcessOperationExceptionTest() throws ProcessOperationException {
        ProcessOperationException mockException = mock(ProcessOperationException.class);
        when(server.createCollection(any(Collection.class))).thenThrow(mockException);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            service.addCollection("Test Collection");
        });

        assertTrue(thrownException.getCause() instanceof ProcessOperationException,
                "The cause of the exception should be a ProcessOperationException.");
    }

    @Test
    public void isUniqueEmptyCollectionsTest() {
        List<Collection> collections = new ArrayList<>();
        when(mainCtrl.getCollectionOverviewCtrl().fetchCollections()).thenReturn(collections);

        assertTrue(service.isUnique("New Collection"), "Collection should be unique.");
    }

}

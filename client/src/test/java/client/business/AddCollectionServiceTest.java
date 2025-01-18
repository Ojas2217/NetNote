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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}

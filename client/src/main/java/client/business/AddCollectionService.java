package client.business;

import client.scenes.MainCtrl;
import client.utils.CollectionUtils;
import commons.Collection;
import commons.CollectionPreview;
import commons.exceptions.ProcessOperationException;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling the business logic of adding a collection.
 */
public class AddCollectionService {

    private final CollectionUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public AddCollectionService(CollectionUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Sends a new note to the server.
     *
     * @param title the title of the note.
     * @throws Exception if there is an error during the operation.
     */
    public void addCollection(String title) {
        try {
            server.createCollection(new Collection(title, new ArrayList<>()));
        } catch (ProcessOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if a note with the given title is unique.
     *
     * @param title the title to check.
     * @return true if the title is unique, false otherwise.
     */
    public boolean isUnique(String title) {
        List<CollectionPreview> Collections = mainCtrl.getCollectionOverviewCtrl().getCollections();
        if (Collections == null) {
            return true;
        }
        for (CollectionPreview collection: Collections) {
            if (collection.getName().equals(title)) {
                return false;
            }
        }
        return true;
    }

}

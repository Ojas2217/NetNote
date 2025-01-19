package client.utils;

import commons.Collection;
import commons.exceptions.ProcessOperationException;
import jakarta.ws.rs.core.GenericType;

import java.util.List;

/**
 * Utils class for collections which uses server utils to communicate with the server
 */
public class CollectionUtils extends ServerUtils{
    /**
     * Gets all the available collections from the server
     *
     * @return a list with all the found collections
     * @throws ProcessOperationException when the server is unreachable or throws an error
     */
    public List<Collection> getAllCollections() throws ProcessOperationException {
        try {
            return super.get("/api/collections",
                    new GenericType<>() {
                    });
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

    /**
     * Attempts to create a new collection on the server
     *
     * @param collection the new collection
     * @throws ProcessOperationException when the server is unreachable,
     *                                      throws an error or the collectionId already exists
     */
    public Collection createCollection(Collection collection) throws ProcessOperationException {
        try {
            return super.post("/api/collections/", collection,
                    new GenericType<>() {
                    });
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

    /**
     * Attempts to delete a collection using its collectionId
     *
     * @param collectionId the id of the collection that needs to be deleted
     * @throws ProcessOperationException when the server is unreachable or throws an error
     */
    public void deleteCollection(long collectionId) throws ProcessOperationException {
        try {
            super.delete("/api/collections/" + collectionId,
                    new GenericType<>() {
                    });
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

    /**
     * Attempts to update a collection on the server
     *
     * @param collection the updated collection
     * @throws ProcessOperationException when the server is unreachable, throws an error
     *                                      or when the collectionId cannot be found on the server
     */
    public void updateCollection(Collection collection) throws ProcessOperationException {
        try {
            super.put("/api/collections/", collection,
                    new GenericType<>() { });
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

//    public List<CollectionPreview> getIdsAndTitles() throws ProcessOperationException {
//        try {
//            return super.get("/api/collections", "idsAndNames", "", new GenericType<>() {
//            });
//        } catch (Exception e) {
//            if (e instanceof ProcessOperationException)
//                throw (ProcessOperationException) e;
//            throw e;
//        }
//    }
}

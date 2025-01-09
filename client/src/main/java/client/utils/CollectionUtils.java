package client.utils;

import commons.Collection;
import commons.CollectionPreview;
import commons.NotePreview;
import commons.exceptions.ProcessOperationException;
import jakarta.ws.rs.core.GenericType;
import java.util.List;

public class CollectionUtils extends ServerUtils{

    public Collection getCollection(long id) throws ProcessOperationException {
        try {
            return super.get("/api/collections" + id,
                    new GenericType<>() {
                    });
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }
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

    public List<CollectionPreview> getIdsAndTitles() throws ProcessOperationException {
        try {
            return super.get("/api/collections", "idsAndNames", "", new GenericType<>() {
            });
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }
}

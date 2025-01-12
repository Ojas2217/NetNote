package server.service;

import commons.Collection;
import commons.CollectionPreview;
import commons.exceptions.ExceptionType;
import commons.exceptions.ProcessOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import server.database.CollectionRepository;

import java.util.List;
import java.util.Optional;

/**
 * {@link Service} class that's responsible for interacting with {@link CollectionRepository}.
 */
@Service
public class CollectionService {
    private final CollectionRepository repo;

    public CollectionService(CollectionRepository collectionRepository) {
        this.repo = collectionRepository;
    }

    public List<Collection> getAllCollections() {
        return repo.findAll();
    }

    /**
     * Retrieves a collection by its ID.
     *
     * @param id the ID of the collection to retrieve
     * @return the collection with the specified ID
     * @throws ProcessOperationException if the ID is invalid or the collection does not exist
     */
    public Collection getCollectionById(Long id) throws ProcessOperationException {
        if (id < 0 || !repo.existsById(id)) {
            throw new ProcessOperationException(
                    "Invalid Collection ID", HttpStatus.BAD_REQUEST.value(), ExceptionType.INVALID_REQUEST);
        }
        return repo.findById(id).orElseThrow();
    }

    /**
     * Creates a new collection and saves it to the repository.
     *
     * @param collection the collection to be created
     * @return the saved collection
     * @throws ProcessOperationException if the collection name is empty
     */
    public Collection createCollection(Collection collection) throws ProcessOperationException {
        if (isNullOrEmpty(collection.getName())) {
            throw new ProcessOperationException(
                    "Collection name cannot be empty",
                    HttpStatus.BAD_REQUEST.value(),
                    ExceptionType.INVALID_REQUEST);
        }
        return repo.save(collection);
    }

    /**
     * Updates an existing collection in the repository.
     *
     * @param collection the collection to update
     * @return the updated collection
     * @throws ProcessOperationException if the collection ID is invalid or the name is missing
     */
    public Collection updateCollection(Collection collection) throws ProcessOperationException {
        if (isNullOrEmpty(collection.getName()) || !repo.existsById(collection.getId())) {
            throw new ProcessOperationException(
                    "Invalid collection ID or missing name",
                    HttpStatus.BAD_REQUEST.value(),
                    ExceptionType.INVALID_REQUEST
            );
        }
        return repo.save(collection);
    }

    /**
     * Deletes a collection by its ID.
     *
     * @param id the ID of the collection to delete
     * @return the deleted collection
     * @throws ProcessOperationException if the collection does not exist
     */
    public boolean deleteCollectionById(Long id) throws ProcessOperationException {
        Optional<Collection> optionalCollection = repo.findById(id);
        if (optionalCollection.isEmpty()) {
            throw new ProcessOperationException(
                    "Collection not found", HttpStatus.NOT_FOUND.value(), ExceptionType.INVALID_REQUEST);
        }
        repo.delete(optionalCollection.get());
        return true;
    }

    /**
     * Returns if {@link Collection} exists by ID.
     *
     * @param id the ID of the collection
     * @return true or false
     * @throws ProcessOperationException if the ID is invalid
     */
    public boolean collectionExistsById(Long id) throws ProcessOperationException {
        if (id == null) {
            throw new ProcessOperationException(
                    "ID is null", HttpStatus.BAD_REQUEST.value(), ExceptionType.INVALID_REQUEST);
        }
        return repo.existsById(id);
    }

    /**
     * Gets the IDs and names of all collections in the repo.
     */
    public List<CollectionPreview> getIdsAndNames() {
        List<Object[]> result = repo.findIdAndName();
        return result.stream()
                .map(e -> new CollectionPreview((Long) e[0], (String) e[1]))
                .toList();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

}

package server.api;

import commons.Collection;
import commons.CollectionPreview;
import commons.exceptions.ExceptionType;
import commons.exceptions.ProcessOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.NoteRepository;
import server.service.CollectionService;

import java.util.List;
/**
 * REST controller for managing collections via HTTP requests.
 * <p>
 * The {@code CollectionController} class provides endpoints to perform CRUD operations
 * on {@link Collection} entities. It is annotated with {@link RestController} and
 * {@link RequestMapping}, with the base path set to {@code /api/collections}.
 * </p>
 * <p>
 * This controller allows clients to:
 * <ul>
 *     <li>Retrieve all collections or a specific collection by its ID ({@link GetMapping}).</li>
 *     <li>Create a new collections with {@link PostMapping}.</li>
 *     <li>Delete a collection by its ID ({@link DeleteMapping}).</li>
 * </ul>
 * </p>
 * <p>
 * The controller uses {@link NoteRepository} for data persistence and includes
 * validation for the input data and error handling with appropriate HTTP response codes.
 * </p>
 */

@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    private final CollectionService service;

    public CollectionController(CollectionService service) {
        this.service = service;
    }

    @GetMapping(path = {"", "/"})
    public List<Collection> getAll() {
        return service.getAllCollections();
    }

    /**
     * Handles a GET request to retrieve a Collection by its ID.
     *
     * @param id the ID of the Collection to retrieve
     * @return a ResponseEntity containing the Collection if found, or a bad request response if the ID is invalid
     * or does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Collection> getById(@PathVariable("id") long id) {
        try {
            return ResponseEntity.ok(service.getCollectionById(id));
        } catch (ProcessOperationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles a POST request to add a new Collection.
     *
     * @param collection the collection to be added, provided in the request body
     * @return a ResponseEntity containing the saved collection if valid, or a bad request response if the collection is invalid
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Collection> add(@RequestBody Collection collection) {
        try {
            return ResponseEntity.ok(service.createCollection(collection));
        } catch (ProcessOperationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles a PUT request to add a new collection.
     *
     * @param collection the collection to be updated, provided in the request body
     * @return a ResponseEntity containing the changed collection if valid, or a bad request response if the collection is invalid
     */
    @PutMapping(path = {"", "/"})
    public ResponseEntity<Collection> update(@RequestBody Collection collection) {
        try {
            return ResponseEntity.ok(service.updateCollection(collection));
        } catch (ProcessOperationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles a DELETE request to remove a collection by its ID.
     *
     * @param id the ID of the collection to be deleted
     * @return a ResponseEntity containing the deleted collection if successful,
     * a bad request response if the ID is invalid, or a forbidden response if an error occurs
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Collection> deleteCollectionById(@PathVariable Long id) {
        try {
            if (id == null)
                throw new ProcessOperationException("Collection ID is NULL", HttpStatus.BAD_REQUEST.value(),
                        ExceptionType.INVALID_CREDENTIALS);
            return ResponseEntity.ok(service.deleteCollectionById(id));
        } catch (ProcessOperationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Gets IDs and names stored in the repo.
     */
    @GetMapping(params = "idsAndNames")
    public ResponseEntity<List<CollectionPreview>> getIdsAndNames() {
        return ResponseEntity.ok(service.getIdsAndNames());
    }

}

package server.api;

import commons.exceptions.ExceptionType;
import commons.Note;
import commons.NotePreview;
import commons.exceptions.ProcessOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.NoteRepository;
import server.service.NoteService;

import java.util.List;

/**
 * REST controller for managing notes via HTTP requests.
 * <p>
 * The {@code NoteController} class provides endpoints to perform CRUD operations
 * on {@link Note} entities. It is annotated with {@link RestController} and
 * {@link RequestMapping}, with the base path set to {@code /api/notes}.
 * </p>
 * <p>
 * This controller allows clients to:
 * <ul>
 *     <li>Retrieve all notes or a specific note by its ID ({@link GetMapping}).</li>
 *     <li>Create a new note with {@link PostMapping}.</li>
 *     <li>Delete a note by its ID ({@link DeleteMapping}).</li>
 * </ul>
 * </p>
 * <p>
 * The controller uses {@link NoteRepository} for data persistence and includes
 * validation for the input data and error handling with appropriate HTTP response codes.
 * </p>
 */
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @GetMapping(path = {"", "/"})
    public List<Note> getAll() {
        return service.getAllNotes();
    }

    /**
     * Handles a GET request to retrieve a Note by its ID.
     *
     * @param id the ID of the Note to retrieve
     * @return a ResponseEntity containing the Note if found, or a bad request response if the ID is invalid
     * or does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getById(@PathVariable("id") long id) {
        try {
            return ResponseEntity.ok(service.getNoteById(id));
        } catch (ProcessOperationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles a POST request to add a new Note.
     *
     * @param note the Note to be added, provided in the request body
     * @return a ResponseEntity containing the saved Note if valid, or a bad request response if the Note is invalid
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Note> add(@RequestBody Note note) {
        try {
            return ResponseEntity.ok(service.createNote(note));
        } catch (ProcessOperationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles a PUT request to add a new Note.
     *
     * @param note the Note to be updated, provided in the request body
     * @return a ResponseEntity containing the changed Note if valid, or a bad request response if the Note is invalid
     */
    @PutMapping(path = {"", "/"})
    public ResponseEntity<Note> update(@RequestBody Note note) {
        try {
            return ResponseEntity.ok(service.updateNote(note));
        } catch (ProcessOperationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles a DELETE request to remove a Note by its ID.
     *
     * @param id the ID of the Note to be deleted
     * @return a ResponseEntity containing the deleted Note if successful,
     * a bad request response if the ID is invalid, or a forbidden response if an error occurs
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Note> deleteNoteById(@PathVariable Long id) {
        try {
            if (id == null)
                throw new ProcessOperationException("Note ID is NULL", HttpStatus.BAD_REQUEST.value(),
                        ExceptionType.INVALID_CREDENTIALS);
            return ResponseEntity.ok(service.deleteNoteById(id));
        } catch (ProcessOperationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Gets IDs and titles stored in the repo.
     */
    @GetMapping(params = "idsAndTitles")
    public ResponseEntity<List<NotePreview>> getIdsAndTitles() {
        return ResponseEntity.ok(service.getIdsAndTitles());
    }

    /**
     * Adds a note via ws
     * @param note the note that needs to be added
     * @return the added note
     */
    @MessageMapping("/add")
    @SendTo("/topic/add")
    public Note addMessage(Note note) {
        return add(note).getBody();
    }

    /**
     * Deletes a note via ws
     * @param id the id of the note that needs to be deleted
     * @return the deleted note
     */
    @MessageMapping("/delete")
    @SendTo("/topic/delete")
    public Note deleteMessage(Long id) {
        System.out.println("\n\n\n\n\n delete \n\n\n\n\n\n");
        return deleteNoteById(id).getBody();
    }

    /**
     * Updates a note via ws
     * @param note the note that needs to be added
     * @return the added note
     */
    @MessageMapping("/update")
    @SendTo("/topic/update")
    public Note updateMessage(Note note) {
        return update(note).getBody();
    }

    @MessageMapping("/title")
    @SendTo("/topic/title")
    public Note titleMessage(Note note) {
        return update(note).getBody();
    }
}

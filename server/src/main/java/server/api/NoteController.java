package server.api;

import commons.ExceptionType;
import commons.Note;
import commons.ProcessOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.NoteRepository;

import java.util.List;
import java.util.Optional;

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

    private Note selected;
    private final NoteRepository repo;

    public NoteController(NoteRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = {"", "/"})
    public List<Note> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Note> add(@RequestBody Note note) {
        if (isNullOrEmpty(note.title) || isNullOrEmpty(note.content)) {
            return ResponseEntity.badRequest().build();
        }
        Note saved = repo.save(note);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Note> deleteNoteById(@PathVariable Long id) {
        try {
            if (id == null)
                throw new ProcessOperationException("Note ID is NULL", HttpStatus.BAD_REQUEST.value(),
                    ExceptionType.INVALID_CREDENTIALS);
            Optional<Note> optionalNote = repo.findById(id);
            if (optionalNote.isEmpty())
                throw new ProcessOperationException("Note ID is wrong", HttpStatus.BAD_REQUEST.value(),
                        ExceptionType.INVALID_REQUEST);
            repo.delete(optionalNote.get());
            return ResponseEntity.ok(optionalNote.get());
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                return ResponseEntity
                        .status(((ProcessOperationException) e).getStatusCode())
                        .build();
            return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).build();
        }

    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

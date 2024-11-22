package server.api;

import commons.ExceptionType;
import commons.Note;
import commons.ProcessOperationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.NoteRepository;

import java.util.List;
import java.util.Optional;

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
                throw new ProcessOperationException("Note ID is NULL", 400,
                    ExceptionType.INVALID_CREDENTIALS);
            Optional<Note> optionalNote = repo.findById(id);
            if (optionalNote.isEmpty())
                throw new ProcessOperationException("Note ID is wrong", 400,
                        ExceptionType.INVALID_REQUEST);
            repo.delete(optionalNote.get());
            return ResponseEntity.ok(optionalNote.get());
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                return ResponseEntity
                        .status(((ProcessOperationException) e).getStatusCode())
                        .build();
            return ResponseEntity.status(500).build();
        }

    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

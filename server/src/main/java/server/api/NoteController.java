package server.api;

import commons.Note;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.NoteRepository;

import java.util.List;

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

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

package server.service;

import commons.ExceptionType;
import commons.Note;
import commons.ProcessOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import server.database.NoteRepository;

import java.util.List;
import java.util.Optional;

/**
 * {@link Service} class that's responsible for interacting with {@link NoteRepository}.
 * */
@Service
public class NoteService {

    private final NoteRepository repo;

    public NoteService(NoteRepository repo) {
        this.repo = repo;
    }

    public List<Note> getAllNotes() {
        return repo.findAll();
    }

    /**
     * Retrieves a note by its ID.
     *
     * @param id the ID of the note to retrieve
     * @return the note with the specified ID
     * @throws ProcessOperationException if the ID is invalid or the note does not exist
     */
    public Note getNoteById(Long id) throws ProcessOperationException {
        if (id < 0 || !repo.existsById(id)) {
            throw new ProcessOperationException(
                    "Invalid Note ID", HttpStatus.BAD_REQUEST.value(), ExceptionType.INVALID_REQUEST);
        }
        return repo.findById(id).orElseThrow();
    }

    /**
     * Creates a new note and saves it to the repository.
     *
     * @param note the note to be created
     * @return the saved note
     * @throws ProcessOperationException if the note title or content is null or empty
     */
    public Note createNote(Note note) throws ProcessOperationException {
        if (isNullOrEmpty(note.title) || isNullOrEmpty(note.content)) {
            throw new ProcessOperationException(
                    "Note title or content cannot be empty", HttpStatus.BAD_REQUEST.value(), ExceptionType.INVALID_REQUEST);
        }
        return repo.save(note);
    }

    /**
     * Updates an existing note in the repository.
     *
     * @param note the note to update
     * @return the updated note
     * @throws ProcessOperationException if the note ID is invalid or the title is missing
     */
    public Note updateNote(Note note) throws ProcessOperationException {
        if (isNullOrEmpty(note.title) || !repo.existsById(note.id)) {
            throw new ProcessOperationException(
                    "Invalid Note ID or missing title", HttpStatus.BAD_REQUEST.value(), ExceptionType.INVALID_REQUEST);
        }
        return repo.save(note);
    }

    /**
     * Deletes a note by its ID.
     *
     * @param id the ID of the note to delete
     * @return the deleted note
     * @throws ProcessOperationException if the note does not exist
     */
    public Note deleteNoteById(Long id) throws ProcessOperationException {
        Optional<Note> optionalNote = repo.findById(id);
        if (optionalNote.isEmpty()) {
            throw new ProcessOperationException(
                    "Note not found", HttpStatus.NOT_FOUND.value(), ExceptionType.INVALID_REQUEST);
        }
        repo.delete(optionalNote.get());
        return optionalNote.get();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}

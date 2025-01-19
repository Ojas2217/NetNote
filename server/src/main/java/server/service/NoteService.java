package server.service;

import commons.Note;
import commons.NoteCollectionPair;
import commons.NotePreview;
import commons.exceptions.ExceptionType;
import commons.exceptions.ProcessOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.database.CollectionRepository;
import server.database.NoteRepository;

import java.util.List;
import java.util.Optional;

/**
 * {@link Service} class that's responsible for interacting with {@link NoteRepository}.
 */
@Service
public class NoteService {

    private final NoteRepository repo;
    private final CollectionRepository collectionRepo;

    public NoteService(NoteRepository repo, CollectionRepository collectionRepo) {
        this.repo = repo;
        this.collectionRepo = collectionRepo;
    }

    public List<Note> getAllNotes() {
        var notes = repo.findAll();

        if (notes.isEmpty()) return notes;

        // invalid if a note is not assigned to a collection
        var invalid = notes.stream()
                .anyMatch(note -> {
                    return note.getCollection() == null;
                });
        if (invalid) {
            var collections = collectionRepo.findAll();
            if (collections.isEmpty()) System.out.println("""
                    No collections found, to assign loose notes.
                    This is bad! Create an issue for this!
                    """);

            // Sets loose notes to be assigned to the first collection in the server.
            // Since the server doesn't know what the client has set as default, this
            // should be fine for handling loose notes, which will mess up the logic
            // in other parts of the code.
            else {
                notes.forEach(note -> {
                    if (note.getCollection() == null) {
                        note.setCollection(collections.getFirst());
                        repo.save(note);
                    }
                });

                // In case new notes get added while invalid ones get fixed, this should
                // make sure that they get fixed as well. May cause issues though if
                // many notes get spam created.
                notes = getAllNotes();
            }
        }
        return notes;
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
        if (isNullOrEmpty(note.title)) {
            throw new ProcessOperationException(
                    "Note title cannot be empty",
                    HttpStatus.BAD_REQUEST.value(),
                    ExceptionType.INVALID_REQUEST);
        }
        return repo.save(note);
    }

    /**
     * Updates an existing note in the repository.
     *
     * @param noteWithChanges the note to update
     * @return the updated note
     * @throws ProcessOperationException if the note ID is invalid or the title is missing
     */
    @Transactional
    public Note updateNote(Note noteWithChanges) throws ProcessOperationException {
        if (isNullOrEmpty(noteWithChanges.title) || !repo.existsById(noteWithChanges.id)) {
            throw new ProcessOperationException(
                    "Invalid Note ID or missing title", HttpStatus.BAD_REQUEST.value(), ExceptionType.INVALID_REQUEST);
        }
        var noteFromRepoOptional = repo.findById(noteWithChanges.id);
        if (noteFromRepoOptional.isEmpty())
            throw new ProcessOperationException(
                    "Invalid Note doesn't exist", HttpStatus.BAD_REQUEST.value(), ExceptionType.INVALID_REQUEST);
        var note = noteFromRepoOptional.get();
        note.setTitle(noteWithChanges.title);
        note.setContent(noteWithChanges.content);
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

    /**
     * Returns if {@link Note} exists by ID.
     *
     * @param id the ID of the note
     * @return true or false
     * @throws ProcessOperationException if the ID is invalid
     */
    public boolean noteExistsById(Long id) throws ProcessOperationException {
        if (id == null) {
            throw new ProcessOperationException(
                    "ID is null", HttpStatus.BAD_REQUEST.value(), ExceptionType.INVALID_REQUEST);
        }
        return repo.existsById(id);
    }


    private static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Gets the IDs and titles of all notes in the repo.
     *
     * @throws ProcessOperationException if query result is empty
     */
    public List<NotePreview> getIdsAndTitles() {
        List<Object[]> result = repo.findIdAndTitle();
        return result.stream()
                .map(e -> NotePreview.of((Long) e[0], (String) e[1]))
                .toList();
    }

    /**
     * Assigns a note to a collection.
     */
    @Transactional
    public NoteCollectionPair assignNoteToCollection(NoteCollectionPair pair) throws ProcessOperationException {
        if (pair.getCollection() == null || pair.getNote() == null) {
            throw new ProcessOperationException(
                    "Something was Null.",
                    HttpStatus.BAD_REQUEST.value(),
                    ExceptionType.SERVER_ERROR);
        };
        var collectionOptional = collectionRepo.findById(pair.getCollection().getId());
        var noteOptional = repo.findById(pair.getNote().getId());
        if (collectionOptional.isEmpty() || noteOptional.isEmpty()) {
            throw new ProcessOperationException(
                    "Collection or note not found.",
                    HttpStatus.NOT_FOUND.value(),
                    ExceptionType.INVALID_REQUEST
            );
        }
        var collection = collectionOptional.get();
        var note = noteOptional.get();
        note.setCollection(collection);
        repo.save(note);
        return pair;
    }
}

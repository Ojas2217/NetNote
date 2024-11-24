package client.utils;

import commons.Note;
import commons.ProcessOperationException;
import jakarta.ws.rs.core.GenericType;

import java.util.List;

public class NoteUtils extends ServerUtils {


    /**
     * A function that gets a note
     * @param id the note id
     * @return the note
     * @throws ProcessOperationException e
     */
    public Note getNote(long id) throws ProcessOperationException {
        try {
            return super.get("/api/notes/" + id,
                    new GenericType<>(){});
        } catch (Exception e) {
            if(e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

    /**
     * A function that gets all the notes
     * @return all the notes
     * @throws ProcessOperationException e
     */
    public List<Note> getAllNotes() throws ProcessOperationException {
        try {
            return super.get("/api/notes",
                    new GenericType<>(){});
        } catch (Exception e) {
            if(e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

    /**
     * A function that creates a note
     * @param note the note
     * @return the deleted note
     * @throws ProcessOperationException e
     */
    public Note createNote(Note note) throws ProcessOperationException {
        try {
            return super.post("/api/notes/", note,
                    new GenericType<>(){});
        } catch (Exception e) {
            if(e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

    /**
     * A function that edits a note
     * @param note the note
     * @throws ProcessOperationException e
     */
    public void editNote(Note note) throws ProcessOperationException {
        //This will be something that is done with websockets,
        //that is basically a communication system that is more instant but is more costly
    }

    /**
     * A function that deletes a note
     * @param id the note id
     * @return the deleted note
     * @throws ProcessOperationException e
     */
    public Note deleteNote(long id) throws ProcessOperationException {
        try {
            return super.delete("/api/notes/" + id,
                    new GenericType<>(){});
        } catch (Exception e) {
            if(e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }
}

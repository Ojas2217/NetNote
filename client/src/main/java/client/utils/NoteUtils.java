package client.utils;

import commons.Collection;
import commons.Note;
import commons.NoteCollectionPair;
import commons.NotePreview;
import commons.exceptions.ProcessOperationException;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility class for interacting with the note-related API endpoints.
 * <p>
 * The {@code NoteUtils} class extends {@link ServerUtils} and provides various methods to
 * interact with the backend API for managing notes. These methods include retrieving, creating,
 * editing, and deleting notes through HTTP requests.
 * </p>
 * <p>
 * Each method makes a corresponding API call using the {@link ServerUtils} methods, handles exceptions,
 * and returns the appropriate data, throwing a {@link ProcessOperationException} in case of errors.
 * </p>
 * <ul>
 *     <li>{@link #getNote(long)}: Fetches a specific note by ID.</li>
 *     <li>{@link #getAllNotes()}: Fetches all notes.</li>
 *     <li>{@link #createNote(Note, Collection)}: Creates a new note.</li>
 *     <li>{@link #editNote(Note)}: A placeholder method for editing notes (to be implemented with WebSockets).</li>
 *     <li>{@link #deleteNote(long)}: Deletes a note by its ID.</li>
 * </ul>
 */
public class NoteUtils extends ServerUtils {

    /**
     * A function that gets a note
     *
     * @param id the note id
     * @return the note
     * @throws ProcessOperationException e
     */
    public Note getNote(long id) throws ProcessOperationException {
        try {
            return super.get("/api/notes/" + id,
                    new GenericType<>() {
                    });
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

    /**
     * A function that gets all the notes
     *
     * @return all the notes
     * @throws ProcessOperationException e
     */
    public List<Note> getAllNotes() throws ProcessOperationException {
        try {
            return super.get("/api/notes",
                    new GenericType<>() {
                    });
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

    /**
     * A function that creates a note
     *
     * @param note the note
     * @return the deleted note
     * @throws ProcessOperationException e
     */
    public Note createNote(Note note, Collection collection) throws ProcessOperationException {
        try {
            return super.post("/api/notes/", NoteCollectionPair.of(note, collection),
                    new GenericType<>() {
                    }).getNote();
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

    /**
     * A function that edits a note
     *
     * @param note the note
     * @throws ProcessOperationException e
     */
    public void editNote(Note note) throws ProcessOperationException {
        try {
            super.put("/api/notes/", note,
                    new GenericType<>() {
                    });
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

    /**
     * A function that deletes a note
     *
     * @param id the note id
     * @return the deleted note
     * @throws ProcessOperationException e
     */
    public Note deleteNote(long id) throws ProcessOperationException {
        try {
            return super.delete("/api/notes/" + id,
                    new GenericType<>() {
                    });
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

    /**
     * A function that gets all IDs and titles of notes in {@code List<Pair<Long, String>>} format.
     *
     * @return the deleted note
     * @throws ProcessOperationException e
     */
    public List<NotePreview> getIdsAndTitles() throws ProcessOperationException {
        try {
            return super.get("/api/notes", "idsAndTitles", "", new GenericType<>() {
            });
        } catch (Exception e) {
            if (e instanceof ProcessOperationException)
                throw (ProcessOperationException) e;
            throw e;
        }
    }

    private StompSession session = connect("ws://localhost:8080/websocket");

    /**
     * This method connects to the websocket server
     * @param url the url to the websocket
     * @return a session
     */
    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * with this you can register for a change in the database
     * @param dest the destination to the websocket function
     * @param consumer what needs to happen when there is a change
     */
    public void registerForMessages(String dest, Consumer<Note> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Note.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((Note) payload);
            }
        });
    }

    /**
     * Sends a change to the websocket server
     * @param dest the destination to the websocket function
     * @param o the object
     */
    public void send(String dest, Object o) {
        session.send(dest, o);
    }
}

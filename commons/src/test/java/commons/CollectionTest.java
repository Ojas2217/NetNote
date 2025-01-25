package commons;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import static org.junit.jupiter.api.Assertions.*;

class CollectionTest {
    List<Note> notes = List.of(new Note("title", "content"));
    Collection collection = new Collection("name", notes);

    @Test
    void getName() {
        String expected = "name";
        assertEquals(expected, collection.getName());
    }

    @Test
    void getNotes() {
        assertEquals(notes, collection.getNotes());
    }

    @Test
    void testEquals() {
        Collection collection2 = new Collection("name", notes);
        assertEquals(collection, collection2);
    }

    @Test
    void testHashCode() {
        Collection collection2 = new Collection("name", notes);
        assertEquals(collection.hashCode(), collection2.hashCode());
    }

    @Test
    void testToString() {
        String expected = ToStringBuilder.reflectionToString(collection, MULTI_LINE_STYLE);
        assertEquals(expected, collection.toString());
    }

    @Test
    void setNotes() {
        List<Note> newNotes = List.of(new Note("new title 1", "new content 1"), new Note("new title 2", "new content 2"));
        collection.setNotes(newNotes);

        assertEquals(newNotes, collection.getNotes(), "The notes in the collection should be updated to the new list");
        assertEquals(2, collection.getNotes().size(), "The collection should now contain two notes");
    }
}
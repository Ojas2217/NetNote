package commons;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import static org.junit.jupiter.api.Assertions.*;

class NoteTest {
    Note note;

    @BeforeEach
    void setUp() {
        note = new Note("title", "content");
    }

    @Test
    void getTitle() {
        String expected = "title";
        assertEquals(expected, note.getTitle());
    }

    @Test
    void setTitle() {
        String expected = "title";
        assertEquals(expected, note.getTitle());
        expected = "new Title";
        note.setTitle(expected);
        assertEquals(expected, note.getTitle());
    }

    @Test
    void getContent() {
        String expected = "content";
        assertEquals(expected, note.getContent());
    }

    @Test
    void contentSearchQueryString() {
        List<Integer> expected = List.of(3, 6);
        assertEquals(expected, note.contentSearchQueryString("t"));

        Note note2 = new Note("title", "abcdabc");
        expected = List.of(0, 4);
        assertEquals(expected, note2.contentSearchQueryString("abc"));
    }

    @Test
    void testEquals() {
        Note note2 = new Note("title", "content");
        assertEquals(note, note2);
    }

    @Test
    void testHashCode() {
        Note note2 = new Note("title", "content");
        int expected = note2.hashCode();
        assertEquals(expected, note.hashCode());
    }

    @Test
    void testToString() {
        String expected = ToStringBuilder.reflectionToString(note, MULTI_LINE_STYLE);
        assertEquals(expected, note.toString());
    }

    @Test
    void getId() {
        assertEquals(0, note.getId());
    }

    @Test
    void setContent() {
        String newContent = "new content";
        note.setContent(newContent);
        assertEquals(newContent, note.getContent());
    }

    @Test
    void getCollection() {
        Collection collection = new Collection("Test Collection", List.of(note));
        note.setCollection(collection);
        assertEquals(collection, note.getCollection());
    }

    @Test
    void setCollection() {
        Collection collection = new Collection("Test Collection", List.of());
        note.setCollection(collection);
        assertEquals(collection, note.getCollection());
    }

    @Test
    void contentSearchQueryStringEdgeCases() {
        // null
        note.setContent(null);
        assertTrue(note.contentSearchQueryString("test").isEmpty(), "Expected empty result for null content");

        // empty
        note.setContent("");
        assertTrue(note.contentSearchQueryString("test").isEmpty(), "Expected empty result for empty content");

        // empty query string
        note.setContent("content");
        assertTrue(note.contentSearchQueryString("").isEmpty(), "Expected empty result for empty query string");
    }

    @Test
    void toNotePreview() {
        NotePreview notePreview = note.toNotePreview();
        assertEquals(note.getId(), notePreview.getId());
        assertEquals(note.getTitle(), notePreview.getTitle());
    }



}
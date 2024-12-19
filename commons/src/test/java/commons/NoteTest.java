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
}
package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotePreviewTest {
    NotePreview notePreview = new NotePreview(69L, "title");

    @Test
    void getId() {
        Long expected = 69L;
        assertEquals(expected, notePreview.getId());
    }

    @Test
    void getName() {
        String expected = "title";
        assertEquals(expected, notePreview.getTitle());
    }

    @Test
    void of() {
        CollectionPreview expected = CollectionPreview.of(69L, "title");
        assertEquals(expected.getId(), notePreview.getId());
        assertEquals(expected.getName(), notePreview.getTitle());
    }
}
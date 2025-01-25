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

    @Test
    void testEqualsSameValues() {
        NotePreview anotherNotePreview = new NotePreview(69L, "title");
        assertEquals(notePreview, anotherNotePreview);
    }

    @Test
    void testEqualsDifferentId() {
        NotePreview anotherNotePreview = new NotePreview(70L, "title");
        assertNotEquals(notePreview, anotherNotePreview);
    }

    @Test
    void testEqualsDifferentTitle() {
        NotePreview anotherNotePreview = new NotePreview(69L, "different title");
        assertNotEquals(notePreview, anotherNotePreview);
    }

    @Test
    void testEqualsNull() {
        assertNotEquals(notePreview, null);
    }

    @Test
    void testEqualsDifferentClass() {
        String notANotePreview = "Not a NotePreview";
        assertNotEquals(notePreview, notANotePreview);
    }

    @Test
    void testHashCodeSameValues() {
        NotePreview anotherNotePreview = new NotePreview(69L, "title");
        assertEquals(notePreview.hashCode(), anotherNotePreview.hashCode());
    }

    @Test
    void testHashCodeDifferentId() {
        NotePreview anotherNotePreview = new NotePreview(70L, "title");
        assertNotEquals(notePreview.hashCode(), anotherNotePreview.hashCode());
    }

    @Test
    void testHashCodeDifferentTitle() {
        NotePreview anotherNotePreview = new NotePreview(69L, "different title");
        assertNotEquals(notePreview.hashCode(), anotherNotePreview.hashCode());
    }

    @Test
    void testEmptyConstructor() {
        NotePreview emptyNotePreview = new NotePreview();
        assertNull(emptyNotePreview.getId());
        assertNull(emptyNotePreview.getTitle());
    }

    @Test
    void testOfNullValues() {
        NotePreview preview = NotePreview.of(null, null);
        assertNull(preview.getId());
        assertNull(preview.getTitle());
    }

    @Test
    void testOfEmptyTitle() {
        NotePreview preview = NotePreview.of(69L, "");
        assertEquals(69L, preview.getId());
        assertEquals("", preview.getTitle());
    }

    @Test
    void testImmutability() {
        Long originalId = notePreview.getId();
        String originalTitle = notePreview.getTitle();

        assertEquals(69L, originalId);
        assertEquals("title", originalTitle);
    }

}
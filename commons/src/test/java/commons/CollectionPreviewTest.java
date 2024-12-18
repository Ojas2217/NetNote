package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectionPreviewTest {
    CollectionPreview collectionPreview = new CollectionPreview(69L, "title");

    @Test
    void getId() {
        Long expected = 69L;
        assertEquals(expected, collectionPreview.getId());
    }

    @Test
    void getName() {
        String expected = "title";
        assertEquals(expected, collectionPreview.getName());
    }

    @Test
    void of() {
        CollectionPreview expected = CollectionPreview.of(69L, "title");
        assertEquals(expected.getId(), collectionPreview.getId());
        assertEquals(expected.getName(), collectionPreview.getName());
    }
}
package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileTest {

    private Note note;
    private File file;

    @BeforeEach
    void setUp() {
        note = new Note("grogu", "is da best");
        file = new File(1L, "mando", note);
    }

    @Test
    void getId() {
        assertEquals(1L, file.getId());
    }

    @Test
    void getName() {
        assertEquals("mando", file.getName());
    }

    @Test
    void getNote() {
        assertEquals(note, file.getNote());
    }

    @Test
    void setId() {
        file.setId(33L);
        assertEquals(33L, file.getId());
    }
}
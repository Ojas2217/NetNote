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

    @Test
    void setIdBoundaryValues() {
        file.setId(0L);
        assertEquals(0L, file.getId(), "The ID should be set to 0.");

        file.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, file.getId(), "The ID should be set to Long.MAX_VALUE.");
    }

    @Test
    void getNoteWhenNull() {
        File noNoteFile = new File("noNoteFile", null);

        assertNull(noNoteFile.getNote(), "The note should be null.");
    }

    @Test
    void fileNameIsNull() {
        File nullNameFile = new File(2L, null, note);

        assertNull(nullNameFile.getName(), "The file name should be null.");
    }

    @Test
    void fileNameIsEmpty() {
        File emptyNameFile = new File(3L, "", note);

        assertEquals("", emptyNameFile.getName(), "The file name should be an empty string.");
    }

    @Test
    void noteIsNull() {
        File nullNoteFile = new File(4L, "fileWithoutNote", null);

        assertNull(nullNoteFile.getNote(), "The note should be null when not set.");
    }

    @Test
    void fileNameWithSpecialCharacters() {
        File specialCharFile = new File(5L, "file_@#%$!.txt", note);

        assertEquals("file_@#%$!.txt", specialCharFile.getName(), "The file name should include special characters.");
    }
}
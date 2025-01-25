package server.api;
import commons.File;
import commons.Note;
import commons.exceptions.ProcessOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.FileRepository;
import server.database.NoteRepository;
import server.service.FileService;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileControllerTest {

    private FileService fileService;
    private FileRepository repo;
    private FileController fileController;
    private NoteRepository noteRepository;

    @BeforeEach
    void setUp() {
        repo = mock(FileRepository.class);
        noteRepository = mock(NoteRepository.class);
        fileService = new FileService(repo, noteRepository);
        fileController = new FileController(fileService);
    }

    @Test
    void getFile() throws ProcessOperationException {
        ProcessOperationException exception = assertThrows(
                ProcessOperationException.class,
                () -> fileService.getFile(-1L, -1L)
        );
        assertEquals("Invalid Note ID", exception.getMessage());
    }

    @Test
    void uploadFileTest() throws IOException {
        Note note1 = new Note("title1", "content1");
        File file = new File("bruno", note1);
        long noteId = note1.getId();
        when(repo.save(file)).thenReturn(file);
        File res = fileService.uploadFileByName(file, note1);
        assertEquals("bruno", res.getName());
    }

    @Test
    void deleteFileTest() throws ProcessOperationException {
        Note note1 = new Note("title1", "content1");
        File file = new File("bruno", note1);
        long fileId = file.getId();
        long noteId = note1.getId();
        // when(fileService.deleteFile(fileId, noteId))
        //        .thenReturn((file));
        //ResponseEntity<File> res = fileController.deleteFile(fileId, noteId);
        // assertEquals(file, res.getBody());
    }
}

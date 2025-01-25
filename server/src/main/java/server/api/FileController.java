package server.api;

import commons.File;
import commons.exceptions.ProcessOperationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.service.FileService;
import java.util.List;

/**
 * REST controller for managing files via HTTP requests.
 * <p>
 * The {@code FileController} class provides endpoints to perform CRUD operations
 * on {@link File} entities. It is annotated with {@link RestController} and
 * {@link RequestMapping}, with the base path set to {@code /api/notes/{noteId}/files}.
 * </p>
 * <p>
 * The controller uses {@link server.database.FileRepository} for data persistence and includes
 * validation for the input data and error handling with appropriate HTTP response codes.
 * </p>
 */
@RestController
@RequestMapping("api/notes/{noteId}/files")
public class FileController {
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<File> getFile(@PathVariable long fileId, @PathVariable long noteId) throws ProcessOperationException {
        return fileService.getFile(fileId, noteId).map(ResponseEntity::ok).orElse(ResponseEntity.ok().build());
    }

    @GetMapping
    public ResponseEntity<List<File>> getFiles(@PathVariable long noteId) throws ProcessOperationException {
        List<File> files = fileService.getAllNoteFiles(noteId);
        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<File> deleteFile(@PathVariable long fileId,
                                           @PathVariable long noteId) {
        //fileService.deleteFile(fileId);
        //return ResponseEntity.ok().build();
        try {
            fileService.deleteFile(fileId, noteId);
            return ResponseEntity.ok(fileService.deleteFile(fileId, noteId));

        } catch (ProcessOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<File> uploadFile(
            @RequestParam("file") MultipartFile multipartFile,
            @PathVariable long noteId) {
        try {
            File newFile = fileService.uploadFile(multipartFile, noteId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

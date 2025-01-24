package server.service;

import commons.File;
import commons.Note;
import commons.exceptions.ExceptionType;
import commons.exceptions.ProcessOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import server.database.FileRepository;
import server.database.NoteRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * {@link Service} class that's responsible for interacting with {@link server.database.FileRepository}.
 */
@Service
public class FileService {
    private FileRepository fileRepository;
    private NoteRepository noteRepository;

    public FileService(FileRepository fileRepository, NoteRepository noteRepository) {
        this.fileRepository = fileRepository;
        this.noteRepository = noteRepository;
    }

    public Optional<File> getFile(long fileId, long noteId) throws ProcessOperationException {
        if (!noteRepository.existsById(noteId)) {
            throw new ProcessOperationException(
                    "Invalid Note ID", HttpStatus.BAD_REQUEST.value(), ExceptionType.INVALID_REQUEST);
        }
        return fileRepository.findByIdAndNote(fileId, noteRepository.findById(noteId).orElseThrow());
    }

    public List<File> getAllNoteFiles(long noteId) throws ProcessOperationException {
        if (!noteRepository.existsById(noteId)) {
            throw new ProcessOperationException(
                    "Invalid Note ID", HttpStatus.BAD_REQUEST.value(), ExceptionType.INVALID_REQUEST);
        }
        return fileRepository.findByNoteId(noteRepository.findById(noteId).orElseThrow());
    }

    public void deleteFile(long fileId) {

    }

    public Optional<File> uploadFile(MultipartFile multipartFile, Note note) throws IOException {
        File file = new File(note, multipartFile);
        fileRepository.save(file);
        fileRepository.flush();

        return fileRepository.findById(file.getId());
    }

}

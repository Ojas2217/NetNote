package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String ext;
    private byte[] content;

    @ManyToOne
    @JoinColumn(name = "note_id", unique = false)
    @JsonBackReference
    private Note note;

    public File(String name, Note note) {
        this.name = name;
        this.note = note;
    }

    public File(long id, String name, Note note) {
        this.id = id;
        this.name = name;
        this.note = note;
    }

    public File(Note note, MultipartFile multipartFile) throws IOException {
        this.name = multipartFile.getOriginalFilename();
        if (multipartFile.getOriginalFilename().lastIndexOf('.') == -1) {
            this.ext = "";
        } else {
            this.ext = multipartFile.getOriginalFilename()
                    .substring(multipartFile.getOriginalFilename().lastIndexOf('.') + 1);
        }
        this.content = multipartFile.getBytes();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExt() {
        return ext;
    }

    public byte[] getContent() {
        return content;
    }

    public Note getNote() {
        return note;
    }

    public void setId(long id) {
        this.id = id;
    }
}

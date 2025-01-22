package commons;

import java.util.Objects;

/**
 * Note data transfer object for transferring the ID and title.
 */
public class NotePreview {
    private Long id;
    private String title;

    public NotePreview() {
        // for object mapper.
    }

    public NotePreview(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static NotePreview of(Long id, String title) {
        return new NotePreview(id, title);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NotePreview that = (NotePreview) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}

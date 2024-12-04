package commons;

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
}

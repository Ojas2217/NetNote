package commons;

/**
 * Note data transfer object for transferring the ID and title.
 */
public class NoteDTO {
    private Long id;
    private String title;

    public NoteDTO() {
        // for object mapper.
    }

    public NoteDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static NoteDTO of(Long id, String title) {
        return new NoteDTO(id, title);
    }
}

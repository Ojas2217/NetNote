package commons;

public class CollectionPreview {
    private Long id;
    private String name;

    public CollectionPreview() {
        // for object mapper.
    }

    public CollectionPreview(Long id, String title) {
        this.id = id;
        this.name = title;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static CollectionPreview of(Long id, String name) {
        return new CollectionPreview(id, name);
    }
}

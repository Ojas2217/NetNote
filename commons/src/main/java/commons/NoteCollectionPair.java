package commons;

/**
 * For transferring Note-Collection pairs
 */
public class NoteCollectionPair {
    private Note note;
    private Collection collection;

    public NoteCollectionPair() {

    }

    public NoteCollectionPair(Note note, Collection collection) {
        this.note = note;
        this.collection = collection;
    }

    public static NoteCollectionPair of(Note note, Collection collection) {
        return new NoteCollectionPair(note, collection);
    }

    public Note getNote() {
        return note;
    }

    public Collection getCollection() {
        return collection;
    }
}

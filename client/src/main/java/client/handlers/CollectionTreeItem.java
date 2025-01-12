package client.handlers;

import commons.Collection;
import commons.Note;

/**
 * A handlers class that contains information about a collection or a note inside a treeView node
 */
public class CollectionTreeItem {
    private Note note;
    private Collection collection;

    public CollectionTreeItem(Note note) {
        this.note = note;
    }

    public CollectionTreeItem(Collection collection) {
        this.collection = collection;
    }

    public Note getNote() {
        return this.note;
    }

    public Collection getCollection() {
        return collection;
    }

    @Override
    public String toString() {
        return (note != null) ? note.getTitle() : collection.getName();
    }
}

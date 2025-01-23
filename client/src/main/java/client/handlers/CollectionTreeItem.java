package client.handlers;

import commons.Collection;
import commons.Note;

/**
 * A handlers class that contains information about a collection or a note inside a treeView node
 */
public class CollectionTreeItem {
    private Note note;
    private Collection collection;
    private boolean isDefault;

    public CollectionTreeItem(Note note) {
        this.note = note;
    }

    public CollectionTreeItem(Collection collection, boolean isDefault) {
        this.collection = collection;
        this.isDefault = isDefault;
    }

    public Note getNote() {
        return this.note;
    }

    public Collection getCollection() {
        return collection;
    }

    @Override
    public String toString() {
        if (note != null) {
            return note.getTitle();
        } else if (isDefault) {
            return "[DEFAULT] " + collection.getName();
        } else {
            return collection.getName();
        }
    }
}

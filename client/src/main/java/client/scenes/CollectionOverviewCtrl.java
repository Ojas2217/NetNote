package client.scenes;

import client.handlers.CollectionTreeItem;
import client.services.CollectionOverviewService;
import client.utils.AlertUtils;
import client.utils.CollectionUtils;
import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Collection;
import commons.Note;
import commons.NotePreview;
import commons.exceptions.ProcessOperationException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static commons.exceptions.InternationalizationKeys.*;

/**
 * Overview controller class for the collections menu
 */
public class CollectionOverviewCtrl {
    private final NoteUtils noteUtils;
    private final AlertUtils alertUtils;
    private final CollectionUtils collectionUtils;
    private final CollectionOverviewService collectionOverviewService;
    private final MainCtrl mainCtrl;
    @FXML
    private TreeView<CollectionTreeItem> treeView;
    private Collection defaultCollection;
    /**
     * Gets the required mainCtrl and utils
     *
     * @param mainCtrl the mainCtrl
     * @param noteUtils utils for notes
     * @param alertUtils utils to alert the user
     * @param collectionUtils utils for collections
     */

    @Inject
    public CollectionOverviewCtrl(MainCtrl mainCtrl,
                                  NoteUtils noteUtils,
                                  AlertUtils alertUtils,
                                  CollectionUtils collectionUtils,
                                  CollectionOverviewService collectionOverviewService) {
        this.mainCtrl = mainCtrl;
        this.noteUtils = noteUtils;
        this.alertUtils = alertUtils;
        this.collectionUtils = collectionUtils;
        this.collectionOverviewService = collectionOverviewService;

    }

    /**
     * On startUp initiates the treeview and its events
     */
    public void init() {
        treeView.setShowRoot(false);
        treeView.setOnDragDetected(this::treeViewOnDrag);
        treeView.setOnDragOver(this::treeViewOnDragOver);
        treeView.setOnDragDropped(this::treeViewOnDragDropped);
        initializeDefaultCollection();
    }

    /**
     * On TreeView start drag, the selectedItem is checked whether it is a note
     * If so it is then initiated for the drag-drop operation
     *
     * @param event the event information
     */
    private void treeViewOnDrag(MouseEvent event) {
        TreeItem<CollectionTreeItem> selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue().getNote() == null) return;

        Dragboard dragboard = treeView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(selectedItem.getValue().toString());
        dragboard.setContent(content);

        event.consume();
    }

    /**
     * On TreeView while dragging, checks whether the drag-drop operation contains a value.
     * If so, accepts the transfer
     *
     * @param event the drag information
     */
    private void treeViewOnDragOver(DragEvent event) {
        if (event.getGestureSource() != treeView && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.acceptTransferModes(TransferMode.MOVE);
        event.consume();
    }


    private void treeViewOnDragDropped(DragEvent event) {
        boolean success = dropTreeItem(event);
        onDragDropEnd(event, success);
    }

    /**
     * On TreeView dropped, checks whether the target is a collection.
     * If not, cycles through all available Node parents to see whether one is a collection.
     * If so, moves the item from the dragged collection to the target collection.
     *
     * @param event the drop information
     */
    private boolean dropTreeItem(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (!dragboard.hasString()) return false;

        Object target = event.getTarget();

        if (target instanceof Node targetNode) {
            while (targetNode != null && !(targetNode instanceof TreeCell<?>)) {
                targetNode = targetNode.getParent();
            }
            if (targetNode == null) return false;

            TreeCell<?> treeCell = (TreeCell<?>) targetNode;
            TreeItem<CollectionTreeItem> targetItem = (TreeItem<CollectionTreeItem>) treeCell.getTreeItem();
            TreeItem<CollectionTreeItem> draggedItem = treeView.getSelectionModel().getSelectedItem();

            if (targetItem.getValue().getNote() != null || draggedItem == null || draggedItem == targetItem)
                return false;

            Collection targetCollection = targetItem.getValue().getCollection();
            Collection draggedCollection = draggedItem.getParent().getValue().getCollection();
            Note note = draggedItem.getValue().getNote();

            draggedCollection.removeNote(note);
            targetCollection.addNote(note);

            try {
                collectionUtils.updateCollection(draggedCollection);
                collectionUtils.updateCollection(targetCollection);

                draggedItem.getParent().getChildren().remove(draggedItem);
                targetItem.getChildren().add(draggedItem);
                return true;
            } catch (ProcessOperationException ex) {
                System.out.println(ex.getMessage());

                alertUtils.showError(
                        ERROR,
                        UNABLE_TO_RETRIEVE_DATA,
                        NOTE_MAY_BE_DELETED
                );
            }
        }

        return false;
    }

    private void onDragDropEnd(DragEvent event, boolean success) {
        event.setDropCompleted(success);
        event.consume();
    }

    public void selectCollection() {
        if (treeView.getSelectionModel().isEmpty()) return;
        TreeItem<CollectionTreeItem> selectedCollection = treeView.getSelectionModel().getSelectedItem();
        if (selectedCollection.getParent() != treeView.getRoot()) return;
        if (selectedCollection.getValue().getCollection() == null) return;

        Collection collection = selectedCollection.getValue().getCollection();
        List<Note> notes = collection.getNotes();
        List<NotePreview> notePreviews = new ArrayList<>();
        for (Note note : notes) {
            notePreviews.add(NotePreview.of(note.getId(), note.getTitle()));
        }
        mainCtrl.getOverviewCtrl().setCurrentCollectionNoteList(notePreviews);
        refresh();
        mainCtrl.getOverviewCtrl().refresh();
    }

    public void showAdd() {
        mainCtrl.showAddCollection();
        refresh();
    }

    /**
     * Checks whether the selectedItem is a collection.
     * If so, deletes the selected collection on the server.
     *
     * @throws ProcessOperationException if the server cannot be reached or throws an error
     */
    public void deleteCollection() throws ProcessOperationException {
        TreeItem<CollectionTreeItem> selectedCollection = treeView.getSelectionModel().getSelectedItem();
        if (selectedCollection.getParent() != treeView.getRoot()) return;

        Collection collection = selectedCollection.getValue().getCollection();
        String title = collection.getName();
        if (collection.getName().equals("default")) return;
        int choice = collectionOverviewService.promptDeleteNote();
        if (choice == JOptionPane.YES_OPTION) {
            List<Note> notesToMove = collection.getNotes();
            for (Note note : notesToMove) {
                defaultCollection.getNotes().add(note);
            }
            collectionUtils.deleteCollection(selectedCollection.getValue().getCollection().getId());
            mainCtrl.logRegular("Deleted Collection: '" + title + "'");
        }
        refresh();
    }

    public void refresh() {
        List<Collection> collections = fetchCollections();
        setViewableCollections(collections);
    }


    public void initializeDefaultCollection() {
        List<Collection> existingCollections = fetchCollections();
        if (existingCollections.stream().filter(c -> c.getName().equals("default")).count() == 0) {
            defaultCollection = new Collection("default");
            try {
                collectionUtils.createCollection(defaultCollection);
                updateDefaultCollection(fetchCollections());

            } catch (ProcessOperationException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            updateDefaultCollection(fetchCollections());
        } catch (ProcessOperationException e) {
            throw new RuntimeException(e);

        }
    }
    /**
     * Attempts to fetch all collections from the server
     *
     * @return the list of all found collections
     */

    public List<Collection> fetchCollections() {
        try {
            return collectionUtils.getAllCollections();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            String errorMessage = "Error retrieving data from the server, unable to refresh collections";
            JOptionPane.showMessageDialog(null, errorMessage, "ERROR", JOptionPane.WARNING_MESSAGE);
            return new ArrayList<>();
        }
    }

    /**
     * Populates the treeView with the provided collections, in addition to the default collection.
     * The default collection contains all notes that are not in any other collection.
     *
     * @param collections the collections to display
     */
    private void setViewableCollections(List<Collection> collections) {
        TreeItem<CollectionTreeItem> root = new TreeItem<>();

        try {

            updateDefaultCollection(collections);
            collections.forEach(collection -> {
                TreeItem<CollectionTreeItem> treeItem = new TreeItem<>(new CollectionTreeItem(collection));
                collection.getNotes().forEach(n -> {
                    TreeItem<CollectionTreeItem> noteTreeItem = new TreeItem<>(new CollectionTreeItem(n));
                    treeItem.getChildren().add(noteTreeItem);
                });

                root.getChildren().add(treeItem);
            });

            treeView.setRoot(root);

        } catch (ProcessOperationException ex) {
            System.out.println(ex.getMessage());

            alertUtils.showError(
                    "error",
                    "unable to retrieve data",
                    "collection may be deleted"
            );
        }
    }

    /**
     * Updates the default collection by filling the notes with all notes that can not be found in any
     * other collection.
     *
     * @param collections all collections
     * @throws ProcessOperationException if the server cannot be reached or throws an error
     */
    private void updateDefaultCollection(List<Collection> collections) throws ProcessOperationException {
        List<Note> allNotes = noteUtils.getAllNotes();
        Optional<Collection> defaultCollection = collections.stream().filter(c -> c.getName().equals("default")).findFirst();
        collections.stream().filter(c -> !c.getName().equals("default")).toList();
        for (Note note : allNotes) {
            int check = 0;
            for (Collection collection : collections) {
                if (collection.getNotes().contains(note)) {
                    check++;
                }
            }
            if (check == 0) {
                defaultCollection.get().getNotes().add(note);
            }
        }
    }
}

package client.scenes;

import client.handlers.CollectionTreeItem;
import client.services.CollectionOverviewService;
import client.utils.AlertUtils;
import client.utils.CollectionUtils;
import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Collection;
import commons.Note;
import commons.NoteCollectionPair;
import commons.NotePreview;
import commons.exceptions.ProcessOperationException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static java.util.Objects.isNull;

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
    private List<Collection> collections;

    /**
     * Gets the required mainCtrl and utils
     *
     * @param mainCtrl        the mainCtrl
     * @param noteUtils       utils for notes
     * @param alertUtils      utils to alert the user
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

        updateCollections();

        noteUtils.registerForMessages("/topic/transfer", _ -> refresh());
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
            if (targetCollection.getNotes().stream().anyMatch(n -> n.getTitle().equals(note.getTitle()) && n.getId() != note.getId())) {
                alertUtils.showError(
                        "error",
                        "Note with this title already exists in target collection",
                        "Please change note title to continue"
                );
                return false;
            }
            var pair = NoteCollectionPair.of(note, targetCollection);
            noteUtils.send("/app/transfer", pair);
            draggedItem.getParent().getChildren().remove(draggedItem);
            targetItem.getChildren().add(draggedItem);
            return true;
        }

        return false;
    }

    public void showChildren() {
        if (treeView.getSelectionModel().isEmpty()) return;
        TreeItem<CollectionTreeItem> selectedCollection = treeView.getSelectionModel().getSelectedItem();
        if (selectedCollection.getParent() != treeView.getRoot()) return;
        if (selectedCollection.getValue().getCollection() == null) return;

        Collection collection = selectedCollection.getValue().getCollection();
        selectedCollection.setExpanded(!selectedCollection.isExpanded());
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
        mainCtrl.getOverviewCtrl().setSelectedCollection(collection);
        refresh();
        mainCtrl.getOverviewCtrl().refresh();
    }

    public void selectCollection(Collection collection) {
        if (collection == null) {
            collection = getDefaultCollection();
        }
        List<Note> notes = collection.getNotes();
        List<NotePreview> notePreviews = new ArrayList<>();
        for (Note note : notes) {
            notePreviews.add(NotePreview.of(note.getId(), note.getTitle()));
        }
        mainCtrl.getOverviewCtrl().setCurrentCollectionNoteList(notePreviews);
        mainCtrl.getOverviewCtrl().setSelectedCollection(collection);
        refresh();
        mainCtrl.getOverviewCtrl().refresh();
    }

    public CollectionUtils getCollectionUtils() {
        return collectionUtils;
    }

    public void showAdd() {
        mainCtrl.showAddCollection();
        refresh();
    }

    public void addToCollections(Collection collection) {
        System.out.println(collection);
        collections.add(collection);
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
        mainCtrl.getOverviewCtrl().refresh();
    }

    public void refresh() {
        updateCollections();
        setViewableCollections(collections);
    }

    /**
     * Loads, saves, updates collections.
     */
    public void updateCollections() {

        var localCollections = collections;

        // Loads collections from file if not loaded already.
        if (isNull(collections)) localCollections = mainCtrl.getStorage().getCollections();

        try {

            // Gets collections from the server. Might be better to redesign
            // this part to get collections by id, but this is simpler and
            // shouldn't cause problems with a small number of collections.
            var serverCollections = collectionUtils.getAllCollections();

            if (serverCollections == null || serverCollections.isEmpty()) {

                // Not doing fetching from the server, because creating a collection
                // already sends a response with what will be the only collection
                // on the server, so we avoid the case that it gets immediately
                // deleted and then NullPointerException.
                var collection = initializeDefaultCollection();
                collections = new ArrayList<Collection>(List.of(collection));
                setDefaultCollection(collection);
                return;
            }

            if (localCollections == null || localCollections.isEmpty()) {
                collections = new ArrayList<Collection>(List.of(serverCollections.getFirst()));
                setDefaultCollection(serverCollections.getFirst());
                return;
            }

            var serverCollectionIds = serverCollections.stream().map(Collection::getId).toList();
            collections = new ArrayList<Collection>(localCollections.stream()
                    .map(collection -> {
                        if (serverCollectionIds.contains(collection.getId()))
                            return serverCollections.get(serverCollectionIds.indexOf(collection.getId()));
                        else
                            return null;
                    })

                    // This effectively means that a user will no longer see collections
                    // that they previously had, if they get deleted. Maybe a warning
                    // message would be nice, but I don't think it's necessary.
                    .filter(Objects::nonNull)
                    .toList());

            // This should probably be loaded from and saved in the config.
            setDefaultCollection(collections.getFirst());

        } catch (ProcessOperationException e) {
            System.err.println(e);
        }

        setConfigCollection();
    }

    /**
     * <p>
     *    This method is meant to create a default {@link Collection}
     *    ONLY IF the server has no collections!
     * </p>
     */
    public Collection initializeDefaultCollection() {
        try {
            var collection = new Collection("default");
            return collectionUtils.createCollection(collection);
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

        collections.forEach(collection -> {
            boolean isDefault = false;
            if (Objects.equals(defaultCollection.getName(), collection.getName())) {
                isDefault = true;
            }
            TreeItem<CollectionTreeItem> treeItem = new TreeItem<>(new CollectionTreeItem(collection, isDefault));
            collection.getNotes().forEach(n -> {
                TreeItem<CollectionTreeItem> noteTreeItem = new TreeItem<>(new CollectionTreeItem(n));
                treeItem.getChildren().add(noteTreeItem);
            });
            root.getChildren().add(treeItem);
        });
        treeView.setRoot(root);
    }

    /**
     * Updates the default collection by filling the notes with all notes that can not be found in any
     * other collection.
     *
     * @param collections all collections
     * @throws ProcessOperationException if the server cannot be reached or throws an error
     */
    private void updateDefaultCollection(List<Collection> collections) throws ProcessOperationException {
        setDefaultCollection(collections.getFirst());
    }

    public void seeAll() {
        mainCtrl.getOverviewCtrl().seeAllCollections();
    }

    public Collection getDefaultCollection() {
        return defaultCollection;
    }

    public void setDefaultCollection(Collection collection) {
        this.defaultCollection = collection;
    }

    /**
     * sets the collections to the config
     */
    public void setConfigCollection() {
        if (collections != null) {
            mainCtrl.getStorage().setCollections(collections);
        }
    }

    public void getConfigCollection() {
        collections = mainCtrl.getStorage().getCollections();
    }
}

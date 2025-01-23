package client.scenes;

import client.handlers.CollectionTreeItem;
import client.services.CollectionOverviewService;
import client.utils.AlertUtils;
import client.utils.CollectionUtils;
import client.utils.NoteUtils;
import javafx.scene.control.TreeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class CollectionOverviewCtrlTest {
    private MainCtrl mainCtrl;
    private TreeView<CollectionTreeItem> treeView;
    private NoteUtils noteUtils;
    private AlertUtils alertUtils;
    private CollectionUtils collectionUtils;
    private CollectionOverviewService collectionOverviewService;
    private CollectionOverviewCtrl collectionOverviewCtrl;

    @BeforeEach
    public void beforeAll() throws IllegalAccessException, NoSuchFieldException {
        noteUtils = mock(NoteUtils.class);
        mainCtrl = mock(MainCtrl.class);
        alertUtils = mock(AlertUtils.class);
        collectionOverviewService = mock(CollectionOverviewService.class);
        collectionUtils = mock(CollectionUtils.class);

        collectionOverviewCtrl = new CollectionOverviewCtrl(
                mainCtrl,
                noteUtils,
                alertUtils,
                collectionUtils,
                collectionOverviewService
        );
    }

    @Test
    void init() {
    }
}
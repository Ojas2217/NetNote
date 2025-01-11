package client.scenes;
import client.Helpers.LanguageHelper;
import client.Helpers.NoteSearchHelper;
import client.handlers.ThemeViewHandler;
import client.services.Markdown;
import client.services.NoteOverviewService;
import client.utils.AlertUtils;
import client.utils.NoteUtils;
import commons.Note;
import commons.NotePreview;
import commons.exceptions.ExceptionType;
import commons.exceptions.ProcessOperationException;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoteOverviewCtrlTest {
    private NoteOverviewService noteOverviewService;
    private ThemeViewHandler themeViewHandler;
    private LanguageHelper languageHelper;
    private NoteSearchHelper noteSearchHelper;
    private AlertUtils alertUtils;
    private NoteUtils noteUtils;
    private MainCtrl mainCtrl;
    private Markdown markdown;
    private Note note;
    private NoteOverviewCtrl noteOverviewCtrl;
    private NoteOverviewCtrl noteOverviewCtrl2;
    private TextField searchText;
    private TableView<Note> noteTable;
    private TableView.TableViewSelectionModel<Note> selectionModel;

    @BeforeEach
    public void beforeAll() throws InterruptedException, ProcessOperationException {
        noteUtils = mock(NoteUtils.class);
        mainCtrl = mock(MainCtrl.class);
        themeViewHandler = mock(ThemeViewHandler.class);
        noteSearchHelper = mock(NoteSearchHelper.class);
        alertUtils = mock(AlertUtils.class);
        noteOverviewService = mock(NoteOverviewService.class);
        markdown = mock(Markdown.class);
        languageHelper = mock(LanguageHelper.class);
        note = new Note("new", "note");
        noteOverviewCtrl = new NoteOverviewCtrl(noteUtils, mainCtrl,noteOverviewService,themeViewHandler,languageHelper,noteSearchHelper,alertUtils,markdown);
        noteOverviewCtrl2 = mock(NoteOverviewCtrl.class);
    }


    @Test
    public void test() {
        assertTrue(true);
    }

    @Test
    void fetchNotesFailureTest() throws ProcessOperationException {
        NoteOverviewCtrl controller = new NoteOverviewCtrl(noteUtils, mainCtrl,noteOverviewService,themeViewHandler,languageHelper,noteSearchHelper,alertUtils,markdown);
        when(noteUtils.getIdsAndTitles())
                .thenThrow(new RuntimeException("Server Error"));
        controller.fetchNotes();
        assertNull(controller.getNotes());
    }

    @Test
    void fetchNotesTest() throws ProcessOperationException {
        NoteOverviewCtrl controller = new NoteOverviewCtrl(noteUtils, mainCtrl,noteOverviewService,themeViewHandler,languageHelper,noteSearchHelper,alertUtils,markdown);
        List<NotePreview> mockNotes = List.of(
                new NotePreview(1L, "Note 1"),
                new NotePreview(2L, "Note 2")
        );
        when(noteUtils.getIdsAndTitles())
                .thenReturn(mockNotes);
        controller.fetchNotes();
        assertEquals(mockNotes, controller.getNotes());
    }

    @Test
    void searchNotesTest() {
        noteOverviewCtrl = new NoteOverviewCtrl(noteUtils, mainCtrl,noteOverviewService,themeViewHandler,languageHelper,noteSearchHelper,alertUtils,markdown);
        List<NotePreview> notes = List.of(
                new NotePreview(1L, "title1"),
                new NotePreview(2L, "title2"),
                new NotePreview(3L, "title3")
        );
    }

    @Test
    public void createNoteErrorTest() throws ProcessOperationException {
        Note invalidNote = new Note();
        doThrow(new ProcessOperationException("Error creating note",
                HttpStatus.BAD_REQUEST.value(),
                ExceptionType.INVALID_REQUEST))
                .when(noteUtils).createNote(any(Note.class));

        assertThrows(ProcessOperationException.class, () -> noteUtils.createNote(invalidNote));
    }
}

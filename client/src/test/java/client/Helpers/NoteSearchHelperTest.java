package client.Helpers;

import client.handlers.NoteSearchResult;
import client.utils.AlertUtils;
import client.utils.NoteUtils;
import commons.Note;
import commons.NotePreview;
import commons.exceptions.ProcessOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NoteSearchHelperTest {
    private NoteUtils noteUtils;
    private AlertUtils alertUtils;
    private NoteSearchHelper noteSearchHelper;

    @BeforeEach
    void before() {
        alertUtils = mock(AlertUtils.class);
        noteUtils = mock(NoteUtils.class);
        noteSearchHelper = new NoteSearchHelper(alertUtils);
    }

    @Test
    void SearchNoteContentWithoutQuery() {
        List<NotePreview> notelist = List.of(new NotePreview(111L, "yoda"));
        List<NoteSearchResult> hits = noteSearchHelper.searchNoteContent("", notelist, noteUtils);
        assertTrue(hits.isEmpty(), "Results should be empty for an empty query.");
    }

    @Test
    void searchNoteContentNoMatch() throws ProcessOperationException {
        List<NotePreview> noteList = List.of(new NotePreview(1111L, "obi wan"));
        Note note = mock(Note.class);
        when(note.contentSearchQueryString("query")).thenReturn(new ArrayList<>());
        when(noteUtils.getNote(1111L)).thenReturn(note);

        List<NoteSearchResult> hits = noteSearchHelper.searchNoteContent("query", noteList, noteUtils);
        assertTrue(hits.isEmpty(), "Results should be empty if no matches are found.");
    }
    @Test
    void filterNotes() {
        List<NotePreview> list = List.of(
                new NotePreview(21L, "jupiter Note"),
                new NotePreview(22L, "saturn Note"),
                new NotePreview(23L, "uranus Note")
        );

        List<NotePreview> hits = noteSearchHelper.filterNotes(list, "Note");
        assertEquals(3, hits.size(), "All notes with 'Note' in the title should be included.");
    }

    @Test
    void filterNoteNoMatch() {
        List<NotePreview> noteList = List.of(
                new NotePreview(131L, "darth vader"),
                new NotePreview(231L, "qui gon")
        );

        List<NotePreview> hits = noteSearchHelper.filterNotes(noteList, "query");
        assertTrue(hits.isEmpty(), "Filtered results should be empty if no titles match.");
    }

    @Test
    void getSearchLogString() {
        List<NoteSearchResult> hits = List.of(
                new NoteSearchResult(new NotePreview(11L, "elephant"), 0, 5),
                new NoteSearchResult(new NotePreview(11L, "elephant"), 10, 5),
                new NoteSearchResult(new NotePreview(12L, "in da room"), 0, 5)
        );

        String log = noteSearchHelper.getSearchLogString(hits, "boba");
        assertEquals("Found string 'boba', 3 times, across 2 notes", log);
    }

}

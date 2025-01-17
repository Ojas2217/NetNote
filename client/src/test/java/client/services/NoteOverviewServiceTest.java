package client.services;

import client.state.ResourceBundleHolder;
import commons.NotePreview;
import javafx.collections.ObservableList;
import org.junit.Test;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import static javafx.collections.FXCollections.observableArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NoteOverviewServiceTest {
    private ResourceBundleHolder resourceBundleHolder;
    private NoteOverviewService noteOverviewService;

    @Test
    public void initializeServerTitleTest() {
        resourceBundleHolder = mock(ResourceBundleHolder.class);
        noteOverviewService = new NoteOverviewService(resourceBundleHolder);
        ObservableList<NotePreview> previewList = observableArrayList(
                new NotePreview(1L, "New Title")
        );
        List<NotePreview> noteList = new ArrayList<>(previewList);
        NotePreview thisNote = previewList.get(0);

        NotePreview updatedNote = new NotePreview(1L, "Newer Title");

        noteOverviewService.initializeServerTitle(previewList, thisNote, noteList);

        assertEquals("New Title", previewList.get(0).getTitle());
        assertEquals("New Title", noteList.get(0).getTitle());
    }

    @Test
    public void deleteNoteOptionYes() {
        ResourceBundle resourceBundle = mock(ResourceBundle.class);
        ResourceBundleHolder resourceBundleHolder = mock(ResourceBundleHolder.class);
        NoteOverviewService noteOverviewService1 = new NoteOverviewService(resourceBundleHolder);

        when(resourceBundle.getString("delete.message")).thenReturn("Are you sure?");
        when(resourceBundle.getString("delete.confirm")).thenReturn("Confirm Delete");
        when(resourceBundleHolder.getResourceBundle()).thenReturn(resourceBundle);
        mock(JOptionPane.class);

        when(JOptionPane.showConfirmDialog(
                null,
                "Are you sure?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        )).thenReturn(JOptionPane.YES_OPTION);

        int result = noteOverviewService1.promptDeleteNote();
        assertEquals(JOptionPane.YES_OPTION, result);
    }

    @Test
    public void deleteNoteOptionNo() {
        ResourceBundle resourceBundle = mock(ResourceBundle.class);
        ResourceBundleHolder resourceBundleHolder = mock(ResourceBundleHolder.class);
        NoteOverviewService noteOverviewService1 = new NoteOverviewService(resourceBundleHolder);

        when(resourceBundle.getString("delete.message")).thenReturn("Are you sure?");
        when(resourceBundle.getString("delete.confirm")).thenReturn("Confirm Delete");
        when(resourceBundleHolder.getResourceBundle()).thenReturn(resourceBundle);
        mockStatic(JOptionPane.class);

        when(JOptionPane.showConfirmDialog(
                null,
                "Are you sure?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        )).thenReturn(JOptionPane.NO_OPTION);

        int result = noteOverviewService1.promptDeleteNote();
        assertEquals(JOptionPane.NO_OPTION, result);
    }
}

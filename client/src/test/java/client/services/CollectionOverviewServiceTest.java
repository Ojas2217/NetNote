package client.services;

import client.state.ResourceBundleHolder;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class CollectionOverviewServiceTest {


    @Test
    public void promptDeleteNoteTest() {
        ResourceBundle resourceBundle = mock(ResourceBundle.class);
        ResourceBundleHolder resourceBundleHolder = mock(ResourceBundleHolder.class);
        CollectionOverviewService collectionOverviewService = new CollectionOverviewService();
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

        int result = collectionOverviewService.promptDeleteNote();
        assertEquals(JOptionPane.YES_OPTION, result);
    }

}

package client.services;

import javax.swing.*;
/**
 * Service class for Collection Overview controller, responsible for business logic
 */
public class CollectionOverviewService {

    public int promptDeleteNote() {
        return JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this collection",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
    }
}

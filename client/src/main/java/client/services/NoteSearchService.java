package client.services;

import client.handlers.NoteSearchResult;
import client.utils.NoteUtils;
import commons.Note;
import commons.NotePreview;
import commons.exceptions.ProcessOperationException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A service class that aids the searching of text inside all notes
 */
public class NoteSearchService {

    /**
     * Search all notes for occurrences of queryString
     *
     * @param queryString the string to search for inside the content of each note
     * @return a list of NoteSearchResult that contains the note and an index where the searchValue was found
     */
    public List<NoteSearchResult> searchNoteContent(String queryString, List<NotePreview> notes, NoteUtils server) {
        List<NoteSearchResult> foundInNotes = new ArrayList<>();
        if (queryString.isEmpty()) return foundInNotes;

        notes.forEach(n -> {
            try {
                Note note = server.getNote(n.getId());
                List<Integer> foundIndices = note.contentSearchQueryString(queryString);
                if (!foundIndices.isEmpty()) {
                    // Indexes should be recalculated when a user clicks on the note
                    // since the note gets updated and could change!!!
                    foundIndices.forEach(i -> foundInNotes.add(new NoteSearchResult(n, i, queryString.length())));
                }
            } catch (ProcessOperationException e) {
                System.out.println(e.getMessage());
                String errorMessage = "Error retrieving data from the server, unable to get note " + n.getTitle();
                JOptionPane.showMessageDialog(null, errorMessage, "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        });

        return foundInNotes;
    }

    public String getSearchLogString(List<NoteSearchResult> foundInNotes, String queryString) {
        int amount = foundInNotes.size();
        int noteCount = (int) foundInNotes.stream().map(n -> n.getNotePreview().getId()).distinct().count();
        return "Found string '" + queryString + "', " + amount + " times, across " + noteCount + " notes";
    }

    /**
     * Filters the table with all available notes on the provided text
     *
     * @param text the text to search for
     */
    public List<NotePreview> filterNotes(List<NotePreview> notes, String text) {
        return notes.stream()
                .filter(x -> x.getTitle().contains(text))
                .toList();
    }
}

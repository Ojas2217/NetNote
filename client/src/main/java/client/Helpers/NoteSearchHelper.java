package client.Helpers;

import client.handlers.NoteSearchResult;
import client.utils.AlertUtils;
import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Note;
import commons.NotePreview;
import commons.exceptions.ProcessOperationException;

import java.util.ArrayList;
import java.util.List;

import static commons.exceptions.ErrorKeys.*;

/**
 * A helper class that aids the searching of text inside all notes
 */
public class NoteSearchHelper {

    AlertUtils alertUtils;

    @Inject
    public NoteSearchHelper(AlertUtils alertUtils) {
        this.alertUtils = alertUtils;
    }

    /**
     * Search all notes for occurrences of query
     *
     * @param query the string to search for inside the content of each note
     * @return a list of NoteSearchResult that contains the note and an index where the searchValue was found
     */
    public List<NoteSearchResult> searchNoteContent(String query, List<NotePreview> notes, NoteUtils server) {
        List<NoteSearchResult> foundInNotes = new ArrayList<>();
        if (query.isEmpty()) return foundInNotes;

        notes.forEach(n -> {
            try {
                Note note = server.getNote(n.getId());
                List<Integer> foundIndices = note.contentSearchQueryString(query);
                if (!foundIndices.isEmpty()) {
                    // Indexes should be recalculated when a user clicks on the note
                    // since the note gets updated and could change!!!
                    foundIndices.forEach(i -> foundInNotes.add(new NoteSearchResult(n, i, query.length())));
                }
            } catch (ProcessOperationException e) {
                System.out.println(e.getMessage());

                alertUtils.showError(
                        ERROR,
                        UNABLE_TO_RETRIEVE_DATA,
                        NOTE_MAY_BE_DELETED
                );
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

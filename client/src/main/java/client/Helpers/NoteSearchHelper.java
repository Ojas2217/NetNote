package client.Helpers;

import client.handlers.NoteSearchResult;
import client.handlers.SearchIndices;
import client.utils.AlertUtils;
import client.utils.NoteUtils;
import com.google.inject.Inject;
import commons.Note;
import commons.NotePreview;
import commons.exceptions.ProcessOperationException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static commons.exceptions.InternationalizationKeys.*;

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
                    foundIndices.forEach(i -> {
                        Pair<Integer, Integer> lineNrOffset = getLineNrOffset(note.getContent(), i);
                        SearchIndices searchIndices = new SearchIndices(i, i + query.length(), lineNrOffset);
                        foundInNotes.add(new NoteSearchResult(n, searchIndices));
                    });
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

    /**
     * Gets the lineNr and lineOffset of the given index inside the given content
     *
     * @param content the content to search through
     * @param index the index of the character to search for
     * @return a pair containing the found lineNr and lineOffset
     */
    private Pair<Integer, Integer> getLineNrOffset(String content, int index) {
        List<String> lines = Arrays.stream(content.split("\n")).toList();

        int currentIndex = 0;
        for (int i = 0; i < lines.size(); i++) {
            int newIndex = currentIndex + lines.get(i).length();
            if (index < newIndex) {
                return new Pair<>(i + 1, index - currentIndex + 1 - i);
            }
            currentIndex = newIndex;
        }

        return new Pair<>(-1, -1);
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

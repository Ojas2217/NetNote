package client.handlers;

import commons.NotePreview;
import jakarta.inject.Inject;

/**
 * Handler class for handling the SearchContent result
 * <p>
 * The {@code NoteSearchResult} class handles the user interaction for navigating through a content search result.
 * The user can select one of the found {@code NoteSearchResult} and navigate to the corresponding note and its
 * respective content index, where the search value was found.
 * </p>
 */
public class NoteSearchResult {
    private final NotePreview note;
    private final SearchIndices searchIndices;
    private final String lineSnippet;

    @Inject
    public NoteSearchResult(NotePreview note, SearchIndices searchIndices, String lineSnippet) {
        this.note = note;
        this.searchIndices = searchIndices;
        this.lineSnippet = lineSnippet;
    }

    public NotePreview getNotePreview() {
        return note;
    }

    public int getStartIndex() {
        return searchIndices.getStartIndex();
    }

    public int getEndIndex() {
        return searchIndices.getEndIndex();
    }

    public Integer getLineNr() {
        return searchIndices.getLineNr();
    }

    public Integer getLineOffset() {
        return searchIndices.getLineOffset();
    }

    public String getLineSnippet() {
        return lineSnippet;
    }
}

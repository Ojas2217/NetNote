package client.handlers;

import commons.NoteDTO;

/**
 * Handler class for handling the SearchContent result
 * <p>
 * The {@code NoteSearchResult} class handles the user interaction for navigating through a content search result.
 * The user can select one of the found {@code NoteSearchResult} and navigate to the corresponding note and its
 * respective content index, where the search value was found.
 * </p>
 */
public class NoteSearchResult {
    private final NoteDTO note;
    private final int startIndex;
    private final int endIndex;

    public NoteSearchResult(NoteDTO note, int startIndexResult, int wordLength) {
        this.note = note;
        this.startIndex = startIndexResult;
        this.endIndex = startIndex + wordLength;
    }

    public NoteDTO getNoteDTO() {
        return note;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }
}

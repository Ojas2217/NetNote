package client.handlers;

import commons.NoteDTO;

/**
 * Controller class for handling the SearchContent result
 * <p>
 * The {@code NoteSearchResult} class handles the user interaction for navigating through a content search result.
 * The user can select one of the found {@code NoteSearchResult} and navigate to the corresponding note and its
 * respective content index, where the search value was found.
 * </p>
 */
public class NoteSearchResult {
    private final NoteDTO note;
    private final int startIndex;

    public NoteSearchResult(NoteDTO note, int startIndexResult) {
        this.note = note;
        this.startIndex = startIndexResult;
    }

    public NoteDTO getNoteDTO() {
        return note;
    }
}

package commons.exceptions;

/**
 * Error keys for use with {@link java.util.ResourceBundle}.
 */
public enum ErrorKeys {
    ERROR("error"),
    INFORMATION("information"),

    // Input errors
    EMPTY_TITLE("empty.title"),
    NOTE_WITH_TITLE_EXISTS("note.title.exists"),

    // Server
    UNABLE_TO_RETRIEVE_NOTE("unable.to.retrieve.notes"),

    // Suggestions
    NOTE_MAY_BE_DELETED("note.may.be.deleted"),
    ENTER_VALID_NOTE_TITLE("enter.valid.note.title"),

    ;
    private final String key;

    ErrorKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}

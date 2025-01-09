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
    MARKDOWN_INVALID_REQUEST("markdown.invalid.request"),

    // Server
    UNABLE_TO_RETRIEVE_DATA("unable.to.retrieve.data"),
    MARKDOWN_SERVER("markdown.server"),

    // Suggestions
    NOTE_MAY_BE_DELETED("note.may.be.deleted"),
    SERVER_ERROR("server.error"),
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

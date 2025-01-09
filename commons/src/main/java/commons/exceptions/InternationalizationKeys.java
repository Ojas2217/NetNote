package commons.exceptions;

/**
 * Error keys for use with {@link java.util.ResourceBundle}.
 */
public enum InternationalizationKeys {
    // Types
    ERROR("error"),
    INFORMATION("information"),

    // Delete prompt
    DELETE_CONFIRM("delete.confirm"),
    DELETE_MESSAGE("delete.message"),

    // Input errors
    EMPTY_TITLE("empty.title"),
    NOTE_WITH_TITLE_EXISTS("note.title.exists"),
    MARKDOWN_INVALID_REQUEST("markdown.invalid.request"),

    // Client
    UNHANDLED_EXCEPTION("unhandled.exception"),
    MARKDOWN_INSTANTIATION_ERROR("markdown.instantiation.error"),
    MARKDOWN_RENDER_ERROR("markdown.render.error"),


    // Server
    UNABLE_TO_RETRIEVE_DATA("unable.to.retrieve.data"),
    MARKDOWN_SERVER("markdown.server"),

    // Suggestions
    NOTE_MAY_BE_DELETED("note.may.be.deleted"),
    SERVER_ERROR("server.error"),
    ENTER_VALID_NOTE_TITLE("enter.valid.note.title"),

    ;
    private final String key;

    InternationalizationKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}

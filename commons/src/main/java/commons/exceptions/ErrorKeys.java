package commons.exceptions;

/**
 * Error keys for use with {@link java.util.ResourceBundle}.
 */
public enum ErrorKeys {
    ERROR("error"),
    UNABLE_TO_RETRIEVE_NOTE("unable.to.retrieve.notes"),
    NOTE_MAY_BE_DELETED("note.may.be.deleted"),
    ;
    private final String key;

    ErrorKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}

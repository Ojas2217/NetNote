package commons;

public enum ExceptionType {

    INVALID_REQUEST("INVALID_REQUEST"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS"),
    INVALID_USER("INVALID_USER"),
    SERVER_URL_NOT_FOUND("SERVER_URL_NOT_FOUND"),
    ADMIN_PASSWORD_INCORRECT("ADMIN_PASSWORD_INCORRECT"),
    USER_INVALID("USER_INVALID"),
    USER_ID_INVALID("USER_ID_INVALID"),
    SERVER_ERROR("SERVER_ERROR");

    private final String value;

    /**
     * Constructor
     * @param value - value
     */
    ExceptionType(String value) {
        this.value = value;
    }

    /**
     *
     * @return the value of the ExceptionType
     */
    public String getValue() {
        return this.value;
    }

    /**
     * To String method
     * @return the value of the ExceptionType
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

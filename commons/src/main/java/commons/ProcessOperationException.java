package commons;

import java.util.Objects;

public class ProcessOperationException extends Exception {
    private int statusCode;
    private ExceptionType type;

    /**
     * Constructor for a ProcessOperationException
     *
     * @param message
     * @param statusCode
     * @param type
     */
    public ProcessOperationException(String message, int statusCode, ExceptionType type) {
        super(message);
        this.statusCode = statusCode;
        this.type = type;
    }

    /**
     * Get the status code of the problem
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Get the type of the problem
     *
     * @return the type of the problem
     */
    public ExceptionType getType() {
        return type;
    }

    /**
     * ToString method
     *
     * @return the object in string format
     */
    @Override
    public String toString() {
        return "ProcessOperationException{" +
                "statusCode=" + statusCode +
                ", type=" + type +
                ", message=" + this.getMessage() +
                '}';
    }

    /**
     * Equals method
     *
     * @param o
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessOperationException that = (ProcessOperationException) o;
        return statusCode == that.statusCode && type == that.type &&
                getMessage() == that.getMessage();
    }

    /**
     * HashCode method
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), statusCode, type);
    }
}

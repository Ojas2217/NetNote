package commons;

import java.util.Objects;

/**
 * Custom exception class for handling process operation errors.
 * <p>
 * The {@code ProcessOperationException} class extends {@link Exception} and is used
 * to represent errors that occur during the execution of a process. It includes
 * an HTTP status code and an {@link ExceptionType} to categorize the type of error.
 * </p>
 * <p>
 * This exception is thrown when specific operational issues are encountered,
 * and provides methods to access the status code, exception type, and message.
 * </p>
 * <ul>
 *     <li>{@link #getStatusCode()}: Returns the HTTP status code associated with the exception.</li>
 *     <li>{@link #getType()}: Returns the {@link ExceptionType} indicating the category of the error.</li>
 *     <li>{@link #toString()}: Provides a string representation of the exception.</li>
 *     <li>{@link #equals(Object)}: Compares the exception with another object for equality.</li>
 *     <li>{@link #hashCode()}: Computes the hash code for this exception.</li>
 * </ul>
 */
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

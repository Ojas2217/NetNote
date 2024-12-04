package commons;

import java.util.Objects;
/**
 * Custom exception class for handling Markdown Errors.
 * <p>
 * The {@code MarkdownRenderException} class extends {@link RuntimeException} and is used
 * to represent errors that occur during the execution of a process. It includes
 * an HTTP status code and an {@link ExceptionType} to categorize the type of error.
 * </p>
 * <p>
 * This exception is thrown when Markdown issues relating to syntax / incorrect initialization are encountered,
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

public class MarkdownRenderException extends RuntimeException {
    private int statusCode;
    private ExceptionType type;

    public MarkdownRenderException(String message, int statusCode, ExceptionType type) {
        super(message);
        this.statusCode = statusCode;
        this.type = type;
    }

    public ExceptionType getType() {
        return type;
    }

    public int getStatusCode() {
        return statusCode;
    }

    /**
     * toString method
     * @return String
     */
    @Override
    public String toString() {
        return "MarkdownRenderException{" +
                "statusCode=" + statusCode +
                ", type=" + type +
                '}';
    }

    /**
     * equals method
     * @param o
     * @return equality check
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkdownRenderException that = (MarkdownRenderException) o;
        return statusCode == that.statusCode && type == that.type;
    }

    /**
     * hash code method
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(statusCode, type);
    }
}

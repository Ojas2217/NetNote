package commons;

import java.util.Objects;

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

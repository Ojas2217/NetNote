package commons.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarkdownRenderExceptionTest {

    MarkdownRenderException mre = new MarkdownRenderException("message", 69, ExceptionType.INVALID_REQUEST);

    @Test
    void getType() {
        ExceptionType type = mre.getType();
        assertEquals(ExceptionType.INVALID_REQUEST, type);
    }

    @Test
    void getStatusCode() {
        int statusCode = mre.getStatusCode();
        assertEquals(69, statusCode);
    }

    @Test
    void testToString() {
        String expected = "MarkdownRenderException{" +
                "statusCode=" + 69 +
                ", type=" + ExceptionType.INVALID_REQUEST +
                '}';
        assertEquals(expected, mre.toString());
    }

    @Test
    void testEquals() {
        MarkdownRenderException mre2 = new MarkdownRenderException("message", 69, ExceptionType.INVALID_REQUEST);
        assertTrue(mre.equals(mre2));
    }

    @Test
    void testHashCode() {
        MarkdownRenderException mre2 = new MarkdownRenderException("message", 69, ExceptionType.INVALID_REQUEST);
        assertEquals(mre.hashCode(), mre2.hashCode());
    }
}
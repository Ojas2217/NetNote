package commons.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessOperationExceptionTest {

    ProcessOperationException poe = new ProcessOperationException("message", 69, ExceptionType.INVALID_REQUEST);

    @Test
    void getType() {
        ExceptionType type = poe.getType();
        assertEquals(ExceptionType.INVALID_REQUEST, type);
    }

    @Test
    void getStatusCode() {
        int statusCode = poe.getStatusCode();
        assertEquals(69, statusCode);
    }

    @Test
    void testToString() {
        String expected = "ProcessOperationException{" +
                "statusCode=" + 69 +
                ", type=" + ExceptionType.INVALID_REQUEST +
                ", message=" + "message" +
                '}';
        assertEquals(expected, poe.toString());
    }

    @Test
    void testEquals() {
        ProcessOperationException poe2 = new ProcessOperationException("message", 69, ExceptionType.INVALID_REQUEST);
        assertEquals(poe, poe2);
    }

    @Test
    void testHashCode() {
        ProcessOperationException poe2 = new ProcessOperationException("message", 69, ExceptionType.INVALID_REQUEST);
        assertEquals(poe.hashCode(), poe2.hashCode());
    }
}
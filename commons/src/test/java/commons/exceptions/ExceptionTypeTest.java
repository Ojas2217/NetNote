package commons.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTypeTest {

    @Test
    public void testINVALID_REQUEST() {
        ExceptionType et = ExceptionType.INVALID_REQUEST;

        assertEquals(et.getValue(), "INVALID_REQUEST");
        assertEquals(et.getValue(), et.toString());
    }

    @Test
    public void testINVALID_CREDENTIALS() {
        ExceptionType et = ExceptionType.INVALID_CREDENTIALS;

        assertEquals(et.getValue(), "INVALID_CREDENTIALS");
        assertEquals(et.getValue(), et.toString());
    }

    @Test
    public void testINVALID_USER() {
        ExceptionType et = ExceptionType.INVALID_USER;

        assertEquals(et.getValue(), "INVALID_USER");
        assertEquals(et.getValue(), et.toString());
    }

    @Test
    public void testSERVER_URL_NOT_FOUND() {
        ExceptionType et = ExceptionType.SERVER_URL_NOT_FOUND;

        assertEquals(et.getValue(), "SERVER_URL_NOT_FOUND");
        assertEquals(et.getValue(), et.toString());
    }

    @Test
    public void testADMIN_PASSWORD_INCORRECT() {
        ExceptionType et = ExceptionType.ADMIN_PASSWORD_INCORRECT;

        assertEquals(et.getValue(), "ADMIN_PASSWORD_INCORRECT");
        assertEquals(et.getValue(), et.toString());
    }

    @Test
    public void testUSER_INVALID() {
        ExceptionType et = ExceptionType.USER_INVALID;

        assertEquals(et.getValue(), "USER_INVALID");
        assertEquals(et.getValue(), et.toString());
    }

    @Test
    public void testUSER_ID_INVALID() {
        ExceptionType et = ExceptionType.USER_ID_INVALID;

        assertEquals(et.getValue(), "USER_ID_INVALID");
        assertEquals(et.getValue(), et.toString());
    }

    @Test
    public void testSERVER_ERROR() {
        ExceptionType et = ExceptionType.SERVER_ERROR;

        assertEquals(et.getValue(), "SERVER_ERROR");
        assertEquals(et.getValue(), et.toString());
    }

}
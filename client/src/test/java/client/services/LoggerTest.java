package client.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoggerTest {

    Logger logger;
    private final String dateRegex;

    public LoggerTest() {
        this.dateRegex = "\\d\\d:\\d\\d:\\d\\d\\x{202F}((PM)|(AM)): ";
    }

    @BeforeEach
    public void init() {
        logger = new Logger();
    }

    @Test
    public void testAddRegularLog() {
        String message = "test log";
        String result = logger.addRegularLog(message);

        Pattern pattern = Pattern.compile("^" + dateRegex
                                                    + message + "\n$");
        Matcher matcher = pattern.matcher(result);
        Assertions.assertTrue(matcher.find());
    }

    @Test
    public void testAddErrorLog() {
        String message = "test log";
        String result = logger.addErrorLog(message);

        Pattern pattern = Pattern.compile("^" + logger.getStyleErrorStart()
                                                    + dateRegex + message
                                                    + logger.getStyleErrorEnd()  + "\n$");
        Matcher matcher = pattern.matcher(result);
        Assertions.assertTrue(matcher.find());
    }
}

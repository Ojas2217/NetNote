package client.services;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 *  A service class for keeping track of the entire log.
 *  This log is used to display to the user what common actions have occurred
 *  and displays that information in the UI as markdown.
 */
public class Logger {
    private final String styleErrorStart = "<b><span style=\"color:red;\">";
    private final String styleErrorEnd = "</span></b>";

    private String log = "";
    private final DateTimeFormatter dateTimeFormatter;

    /**
     * Constructor that creates a dateTimeFormatter that outputs the date as "hh:mm:ss (AM/PM)"
     */
    public Logger() {
        this.dateTimeFormatter = new DateTimeFormatterBuilder()
                .appendPattern("hh:mm:ss ")
                .appendText(ChronoField.AMPM_OF_DAY)
                .toFormatter(Locale.US);
    }

    public String getStyleErrorStart() {
        return styleErrorStart;
    }

    public String getStyleErrorEnd() {
        return styleErrorEnd;
    }

    private String getStartOfMessage() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        return dateTime.toLocalTime().format(dateTimeFormatter) + ": ";
    }

    public String addRegularLog(String message) {
        String newLog = getStartOfMessage() + message;
        return log += newLog + "\n";
    }

    public String addErrorLog(String message) {
        String newLog = styleErrorStart + getStartOfMessage() + message + styleErrorEnd;
        return log += newLog + "\n";
    }
}

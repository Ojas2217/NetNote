package client.handlers;

import client.utils.AlertUtils;
import com.google.inject.Inject;

import java.util.Arrays;

/**
 * Exception handler that takes an exception, uses {@link AlertUtils} to show alerts
 */
public class ExceptionHandler {

    private final AlertUtils alertUtils;

    @Inject
    public ExceptionHandler(AlertUtils alertUtils) {
        this.alertUtils = alertUtils;
    }

    public void handle(Throwable throwable) {
        handle(throwable, "An Exception Occurred");
    }

    /**
     * Logs the exception and shows an error to the user
     *
     * @param throwable The throwable to handle
     * @param header    A header for content part of the alert
     */
    public void handle(Throwable throwable, String header) {
        alertUtils.showError(
                throwable.getClass().getSimpleName(),
                header,
                throwable.getMessage()
        );

        // log to console
        System.err.println(throwable.getClass().getSimpleName());
        System.err.println(throwable.getMessage());
        System.err.println(Arrays.stream(throwable.getStackTrace())
                .map(StackTraceElement::toString)
                .reduce("", (a, b) -> a + b + System.lineSeparator())
        );
    }
}

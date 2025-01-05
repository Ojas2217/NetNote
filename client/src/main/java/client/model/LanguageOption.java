package client.model;

import javafx.scene.layout.HBox;
import java.util.Locale;

/**
 * Language option model
 */
public class LanguageOption {
    private final HBox hBox;
    private final Locale locale;

    public LanguageOption(Locale locale, HBox hBox) {
        this.locale = locale;
        this.hBox = hBox;
    }

    public static LanguageOption of(Locale locale, HBox hBox) {
        return new LanguageOption(locale, hBox);
    }

    public HBox getHBox() {
        return hBox;
    }

    public Locale getLocale() {
        return locale;
    }
}

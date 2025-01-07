package client.model;

import javafx.scene.layout.HBox;
import java.util.Locale;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LanguageOption that = (LanguageOption) o;
        return Objects.equals(hBox, that.hBox) && Objects.equals(locale, that.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hBox, locale);
    }

    @Override
    public String toString() {
        return "LanguageOption{" +
                "hBox=" + hBox +
                ", locale=" + locale +
                '}';
    }
}

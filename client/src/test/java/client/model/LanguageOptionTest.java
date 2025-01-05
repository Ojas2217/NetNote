package client.model;

import javafx.scene.layout.HBox;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LanguageOptionTest {

    @Test
    void constructorTest() {
        HBox hBox = new HBox();
        Locale locale = Locale.US;
        LanguageOption languageOption = new LanguageOption(locale, hBox);
        assertNotNull(languageOption);
        assertEquals(locale, languageOption.getLocale());
        assertEquals(hBox, languageOption.getHBox());
    }

    @Test
    void ofTest() {
        HBox hBox = new HBox();
        Locale locale = Locale.FRANCE;
        LanguageOption languageOption = LanguageOption.of(locale, hBox);
        assertNotNull(languageOption);
        assertEquals(locale, languageOption.getLocale());
        assertEquals(hBox, languageOption.getHBox());
    }

    @Test
    void equalsTest() {
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        Locale locale1 = Locale.US;
        Locale locale2 = Locale.UK;
        LanguageOption option1 = new LanguageOption(locale1, hBox1);
        LanguageOption option2 = new LanguageOption(locale1, hBox1);
        LanguageOption option3 = new LanguageOption(locale2, hBox2);
        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
        assertNotEquals(option1, null);
        assertNotEquals(option1, new Object());
    }

    @Test
    void hashTest() {
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        Locale locale1 = Locale.US;
        Locale locale2 = Locale.UK;
        LanguageOption option1 = new LanguageOption(locale1, hBox1);
        LanguageOption option2 = new LanguageOption(locale1, hBox1);
        LanguageOption option3 = new LanguageOption(locale2, hBox2);
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    void toStringTest() {
        HBox hBox = new HBox();
        Locale locale = Locale.GERMANY;
        LanguageOption languageOption = new LanguageOption(locale, hBox);
        String toStringResult = languageOption.toString();
        assertTrue(toStringResult.contains("LanguageOption"));
        assertTrue(toStringResult.contains("hBox=" + hBox.toString()));
        assertTrue(toStringResult.contains("locale=" + locale.toString()));
    }

    @Test
    void equalsDifferentHboxTest() {
        Locale locale = Locale.US;
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        LanguageOption option1 = new LanguageOption(locale, hBox1);
        LanguageOption option2 = new LanguageOption(locale, hBox2);
        assertNotEquals(option1, option2);
    }

    @Test
    void equalsDifferentLocaleTest() {
        HBox hBox = new HBox();
        Locale locale1 = Locale.US;
        Locale locale2 = Locale.UK;
        LanguageOption option1 = new LanguageOption(locale1, hBox);
        LanguageOption option2 = new LanguageOption(locale2, hBox);
        assertNotEquals(option1, option2);
    }

    @Test
    void equalsNullHboxTest() {
        Locale locale = Locale.US;
        LanguageOption option1 = new LanguageOption(locale, null);
        LanguageOption option2 = new LanguageOption(locale, null);
        assertEquals(option1, option2);
    }

    @Test
    void equalsNullLocaleTest() {
        HBox hBox = new HBox();
        LanguageOption option1 = new LanguageOption(null, hBox);
        LanguageOption option2 = new LanguageOption(null, hBox);
        assertEquals(option1, option2);
    }
}
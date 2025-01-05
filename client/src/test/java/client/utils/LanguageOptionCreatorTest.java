package client.utils;

import client.model.LanguageOption;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LanguageOptionCreatorTest {

    @Test
    void createTest() {
        Locale locale = Locale.US;
        LanguageOption languageOption = LanguageOptionCreator.create(locale);
        assertNotNull(languageOption);
        assertEquals(locale, languageOption.getLocale());

        HBox hBox = languageOption.getHBox();
        assertNotNull(hBox);
        assertEquals(locale.getCountry(), hBox.getId());

        assertEquals(2, hBox.getChildren().size());
        assertInstanceOf(Label.class, hBox.getChildren().get(0));
        assertInstanceOf(ImageView.class, hBox.getChildren().get(1));

        Label label = (Label) hBox.getChildren().get(0);
        assertEquals(locale.getDisplayLanguage(), label.getText());

        ImageView imageView = (ImageView) hBox.getChildren().get(1);
        assertNotNull(imageView.getImage());
        String expectedImagePath = "/flags/small/" + locale.getCountry().toLowerCase() + ".png";
        assertTrue(imageView.getImage().getUrl().contains(expectedImagePath));
    }

    @Test
    void createWithNullLocaleTest() {
        Locale locale = null;
        assertThrows(NullPointerException.class, () -> LanguageOptionCreator.create(locale));
    }

    @Test
    void testCreateWithEmptyCountryCode() {
        Locale locale = Locale.of("en");
        assertThrows(ExceptionInInitializerError.class, () -> LanguageOptionCreator.create(locale));
    }

    @Test
    void testCreateWithMissingFlagImage() {
        Locale locale = Locale.of("en", "XX");
        assertThrows(NoClassDefFoundError.class, () -> LanguageOptionCreator.create(locale));
    }
}
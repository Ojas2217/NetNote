package client.utils;

import client.model.LanguageOption;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Locale;

/**
 * Utility class for creating {@link HBox} type language options
 */
public class LanguageOptionCreator {

    /**
     * Creates language option.
     **/
    public static LanguageOption create(Locale locale) {
        HBox hbox = new HBox();
        hbox.setId(locale.getCountry());
        Label label = new Label(locale.getLanguage().equals("pi") ? "Pirate" : locale.getDisplayLanguage());
        ImageView imageView = new ImageView();
        Image flag = new Image("/flags/small/" + locale.getCountry().toLowerCase() + ".png");
        imageView.setImage(flag);
        hbox.getChildren().addAll(label, imageView);
        return LanguageOption.of(locale, hbox);
    }
}

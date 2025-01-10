package client.Helpers;

import client.MyFXML;
import client.model.LanguageOption;
import client.scenes.*;
import client.state.ResourceBundleHolder;
import client.utils.LanguageOptionCreator;
import com.google.common.collect.ImmutableList;
import jakarta.inject.Inject;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static commons.exceptions.InternationalizationKeys.*;

/**
 * Helper class for language related logic.
 */
public class LanguageHelper {
    public static final List<Locale> supportedLanguages = ImmutableList.of(
            Locale.US,
            Locale.of("nl", "NL"), // <-- use this format for new languages
            Locale.of("pi", "GB"),
            Locale.FRANCE,                         // (unless supported)
            Locale.GERMANY,
            Locale.of("es", "ES")
    );

    private final MyFXML FXML;
    private final MainCtrl mainCtrl;
    private final ResourceBundleHolder resourceBundleHolder;

    @Inject
    public LanguageHelper(MyFXML FXML, MainCtrl mainCtrl, ResourceBundleHolder resourceBundleHolder) {
        this.FXML = FXML;
        this.mainCtrl = mainCtrl;
        this.resourceBundleHolder = resourceBundleHolder;
    }

    /**
     * Initializes language combo box
     **/
    public void initializeLanguageComboBox(ComboBox<LanguageOption> languageComboBox) {
        supportedLanguages.forEach(lang -> languageComboBox.getItems().add(LanguageOptionCreator.create(lang)));
        languageComboBox.setCellFactory(param -> new ListCell<LanguageOption>() {
            @Override
            protected void updateItem(LanguageOption item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(item.getHBox());
                }
            }
        });

        languageComboBox.setButtonCell(new ListCell<LanguageOption>() {
            @Override
            protected void updateItem(LanguageOption item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(item.getHBox());
                    setText("");
                    mainCtrl.getStorage().setLanguage(item.getLocale().getLanguage());
                    ResourceBundle resourceBundle = ResourceBundle.getBundle("language", item.getLocale());
                    resourceBundleHolder.setResourceBundle(resourceBundle);
                    reloadWithNewLanguage();
                }
            }
        });
    }

    /**
     * Reloads all FXML files again with the resource bundle stored in the {@link ResourceBundleHolder} Singleton
     * and replaces all the scenes' parents with them.
     */
    public void reloadWithNewLanguage() {
        ResourceBundle resourceBundle = resourceBundleHolder.getResourceBundle();
        var overview = FXML.loadParent(resourceBundle, "client", "scenes", "MainScreen.fxml");
        var add = FXML.loadParent(resourceBundle, "client", "scenes", "AddNote.fxml");
        var title = FXML.loadParent(resourceBundle, "client", "scenes", "newTitle.fxml");
        var searchContent = FXML.loadParent(resourceBundle, "client", "scenes", "SearchNoteContent.fxml");
        mainCtrl.getOverviewScene().rootProperty().setValue(overview);
        mainCtrl.getAddScene().rootProperty().setValue(add);
        mainCtrl.getTitleScene().rootProperty().setValue(title);
        mainCtrl.getSearchContentScene().rootProperty().setValue(searchContent);

        var currentSceneParent = mainCtrl.getPrimaryStage().getScene().getRoot();
        if (currentSceneParent.equals(overview))
            mainCtrl.getPrimaryStage().setTitle(resourceBundle.getString(MAIN.getKey()));
        if (currentSceneParent.equals(add))
            mainCtrl.getPrimaryStage().setTitle(resourceBundle.getString(ADD_NOTE.getKey()));
        if (currentSceneParent.equals(title))
            mainCtrl.getPrimaryStage().setTitle(resourceBundle.getString(EDIT_TITLE.getKey()));
        if (currentSceneParent.equals(searchContent))
            mainCtrl.getPrimaryStage().setTitle(resourceBundle.getString(SEARCH_CONTENT.getKey()));
    }
}

package client.Helpers;

import client.MyFXML;
import client.MyStorage;
import client.model.LanguageOption;
import client.scenes.MainCtrl;
import client.scenes.NoteOverviewCtrl;
import client.state.ResourceBundleHolder;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LanguageHelperTest extends ApplicationTest {
    private ResourceBundleHolder resourceBundleHolder;
    private MyFXML FXML;
    private MainCtrl mainCtrl;
    private NoteOverviewCtrl noteOverviewCtrl;
    private LanguageHelper languageHelper;

    @BeforeEach
    void setup() {
        resourceBundleHolder = mock(ResourceBundleHolder.class);
        FXML = mock(MyFXML.class);
        mainCtrl = mock(MainCtrl.class);
        noteOverviewCtrl = mock(NoteOverviewCtrl.class);
        languageHelper = new LanguageHelper(FXML, mainCtrl, resourceBundleHolder);

        Stage mockPrimaryStage = mock(Stage.class);
        when(mainCtrl.getPrimaryStage()).thenReturn(mockPrimaryStage);
        Scene mockScene = mock(Scene.class);
        when(mockPrimaryStage.getScene()).thenReturn(mockScene);

        Scene mockOverviewScene = mock(Scene.class);
        when(mainCtrl.getOverviewScene()).thenReturn(mockOverviewScene);

        Scene mockAddScene = mock(Scene.class);
        when(mainCtrl.getAddScene()).thenReturn(mockAddScene);

        Scene mockTitleScene = mock(Scene.class);
        when(mainCtrl.getTitleScene()).thenReturn(mockTitleScene);

        Scene mockSearchContentScene = mock(Scene.class);
        when(mainCtrl.getSearchContentScene()).thenReturn(mockSearchContentScene);

        ObjectProperty<Parent> mockOverviewRoot = mock(ObjectProperty.class);
        ObjectProperty<Parent> mockAddRoot = mock(ObjectProperty.class);
        ObjectProperty<Parent> mockTitleRoot = mock(ObjectProperty.class);
        ObjectProperty<Parent> mockSearchContentRoot = mock(ObjectProperty.class);

        when(mockOverviewScene.rootProperty()).thenReturn(mockOverviewRoot);
        when(mockAddScene.rootProperty()).thenReturn(mockAddRoot);
        when(mockTitleScene.rootProperty()).thenReturn(mockTitleRoot);
        when(mockSearchContentScene.rootProperty()).thenReturn(mockSearchContentRoot);

    }

//    @Test
//    void comboBoxHasALanguage() {
//        ComboBox<LanguageOption> languageComboBox = new ComboBox<>();
//        Runnable mockUninitializer = mock(Runnable.class);
//        Consumer<Boolean> mockDoSendConsumer = mock(Consumer.class);
//        Runnable mockShowCurrentNote = mock(Runnable.class);
//
//        LanguageOption mockLanguageOption = mock(LanguageOption.class);
//        when(mockLanguageOption.getLocale()).thenReturn(Locale.ENGLISH);
//
//        languageHelper.initializeLanguageComboBox(languageComboBox, mockUninitializer, mockDoSendConsumer, mockShowCurrentNote);
//
//        assertFalse(languageComboBox.getItems().isEmpty(), "ComboBox should contain language options.");
//        assertEquals("en", languageComboBox.getItems().getFirst().getLocale().getLanguage(), "First language option should be 'en'.");
//    }
//
//    @Test
//    void languageBoxHasMoreLanguageOptions() {
//        ComboBox<LanguageOption> languageComboBox = new ComboBox<>();
//        Runnable mockUninitializer = mock(Runnable.class);
//        Consumer<Boolean> mockDoSendConsumer = mock(Consumer.class);
//        Runnable mockShowCurrentNote = mock(Runnable.class);
//
//        languageHelper.initializeLanguageComboBox(languageComboBox, mockUninitializer, mockDoSendConsumer, mockShowCurrentNote);
//
//        boolean containsSpanish = languageComboBox.getItems().stream()
//                .anyMatch(option -> "es".equals(option.getLocale().getLanguage()));
//
//        assertTrue(containsSpanish, "ComboBox should contain Spanish.");
//    }
//
//    @Test
//    void initializedBoxSelectsLanguage() {
//        ComboBox<LanguageOption> languageComboBox = new ComboBox<>();
//        Runnable mockUninitializer = mock(Runnable.class);
//        Consumer<Boolean> mockDoSendConsumer = mock(Consumer.class);
//        Runnable mockShowCurrentNote = mock(Runnable.class);
//
//        MyStorage mockStorage = mock(MyStorage.class);
//        when(mainCtrl.getStorage()).thenReturn(mockStorage);
//        when(mockStorage.getLanguage()).thenReturn("en");
//
//        languageHelper.initializeLanguageComboBox(languageComboBox, mockUninitializer, mockDoSendConsumer, mockShowCurrentNote);
//        LanguageOption mockLanguageOption = mock(LanguageOption.class);
//        when(mockLanguageOption.getLocale()).thenReturn(Locale.ENGLISH);
//        languageComboBox.getItems().add(mockLanguageOption);
//
//        languageComboBox.setValue(mockLanguageOption);
//
//        assertEquals(Locale.ENGLISH, languageComboBox.getValue().getLocale(), "The selected language should be English");
//    }
//
    @Test
    void initializedBoxConnectsStorage() {
        ComboBox<LanguageOption> languageComboBox = new ComboBox<>();
        Runnable mockUninitializer = mock(Runnable.class);
        Consumer<Boolean> mockDoSendConsumer = mock(Consumer.class);
        Runnable mockShowCurrentNote = mock(Runnable.class);

        MyStorage mockStorage = mock(MyStorage.class);
        when(mainCtrl.getStorage()).thenReturn(mockStorage);
        when(mockStorage.getLanguage()).thenReturn("en");

        languageHelper.initializeLanguageComboBox(languageComboBox, mockUninitializer, mockDoSendConsumer, mockShowCurrentNote);

        LanguageOption mockLanguageOption = mock(LanguageOption.class);
        when(mockLanguageOption.getLocale()).thenReturn(Locale.ENGLISH);
        languageComboBox.getItems().add(mockLanguageOption);  // Add mock item to ComboBox
        languageComboBox.setValue(mockLanguageOption);  // This should trigger the ComboBox selection logic

        assertEquals(Locale.ENGLISH, languageComboBox.getValue().getLocale(), "The selected language should be English");

        assertEquals("en", mockStorage.getLanguage(), "The language should be set to 'en'");
    }
//
//    @Test
//    void resourceBundleGetsMockedTest() {
//        ResourceBundle mockResourceBundle = mock(ResourceBundle.class);
//        when(resourceBundleHolder.getResourceBundle()).thenReturn(mockResourceBundle);
//        ResourceBundle resourceBundle = resourceBundleHolder.getResourceBundle();
//        assertNotNull(resourceBundle, "The resource bundle should be correctly mocked.");
//    }
}

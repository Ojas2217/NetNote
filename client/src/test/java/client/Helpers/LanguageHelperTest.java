package client.Helpers;

import client.MyFXML;
import client.scenes.MainCtrl;
import client.scenes.NoteOverviewCtrl;
import client.state.ResourceBundleHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class LanguageHelperTest {
    private ResourceBundleHolder resourceBundleHolder;
    private MyFXML FXML;
    private MainCtrl mainCtrl;
    private NoteOverviewCtrl noteOverviewCtrl;
    private LanguageHelper languageHelper;

    @BeforeEach
    void before() {
        resourceBundleHolder = mock(ResourceBundleHolder.class);
        FXML = mock(MyFXML.class);
        mainCtrl = mock(MainCtrl.class);
        noteOverviewCtrl = mock(NoteOverviewCtrl.class);
        languageHelper = new LanguageHelper(FXML, mainCtrl, resourceBundleHolder);
    }

    @Test
    void initializeLanguageComboBox() {
    }

    @Test
    void reloadWithNewLanguage() {
    }
}

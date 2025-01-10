/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;
import client.MyFXML;
import client.MyModule;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import static com.google.inject.Guice.createInjector;

import client.handlers.ExceptionHandler;
import client.utils.AlertUtils;
import com.google.inject.Injector;


import static org.mockito.Mockito.*;

public class MainCtrlTest {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private static final ExceptionHandler exceptionHandler =
            new ExceptionHandler(new AlertUtils());
    private MainCtrl mainCtrl;
    private Stage primaryStage;
    private Stage searchContentStage;
    private NoteOverviewCtrl mockOverviewCtrl;
    private AddNoteControl mockAddCtrl;
    private NewNoteTitleCtrl mockNewCtrl;
    private SearchNoteContentCtrl mockSearchCtrl;

    private Scene mockOverviewScene;
    private Scene mockAddScene;
    private Scene mockTitleScene;

    @BeforeEach
    public void setUp() {
        primaryStage = mock(Stage.class);
        searchContentStage = mock(Stage.class);

        mockOverviewCtrl = mock(NoteOverviewCtrl.class);
        mockAddCtrl = mock(AddNoteControl.class);
        mockNewCtrl = mock(NewNoteTitleCtrl.class);
        mockSearchCtrl = mock(SearchNoteContentCtrl.class);

        Parent overviewParent = new Pane();
        Parent addParent = new Pane();
        Parent titleParent = new Pane();
        Parent searchContentParent = new Pane();

        mainCtrl = new MainCtrl();

        var overview = FXML.load(NoteOverviewCtrl.class, "client", "scenes", "MainScreen.fxml");
        var add = FXML.load(AddNoteControl.class, "client", "scenes", "AddNote.fxml");
        var title = FXML.load(NewNoteTitleCtrl.class, "client", "scenes", "newTitle.fxml");
        var searchContent = FXML.load(SearchNoteContentCtrl.class, "client", "scenes", "SearchNoteContent.fxml"
        );
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
    }

}

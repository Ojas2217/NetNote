
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

package client;

import static com.google.inject.Guice.createInjector;

import client.handlers.ExceptionHandler;
import client.scenes.NewNoteTitleCtrl;
import client.scenes.NoteOverviewCtrl;
import client.scenes.SearchNoteContentCtrl;
import client.state.ResourceBundleHolder;
import client.utils.AlertUtils;
import com.google.inject.Injector;
import client.scenes.AddNoteControl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main class for the client application.
 * <p>
 * This class extends {@link Application} and serves as the entry point for the
 * JavaFX client. It initializes the dependency injection using Guice, loads
 * the FXML views, and sets up the primary stage for the application.
 * </p>
 * <p>
 * The {@link ServerUtils} class is used to ensure that the server is available
 * before proceeding, and controllers like {@link NoteOverviewCtrl},
 * and {@link MainCtrl} are managed via dependency injection.
 * </p>
 */
public class Main extends Application {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private static final ExceptionHandler exceptionHandler =
            new ExceptionHandler(new AlertUtils(new ResourceBundleHolder()));

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Thread.setDefaultUncaughtExceptionHandler((_, throwable) -> {
            exceptionHandler.handle(throwable, "An Unhandled Exception Occurred!");
        });

        var serverUtils = INJECTOR.getInstance(ServerUtils.class);

        // Awaits for the server to become available instead of ending the program
        if (!serverUtils.isServerAvailable()) //noinspection CheckStyle
        {
            var msg = "Server needs to be started before the client, but it does not seem to be available";
            System.err.println(msg);

            final int SLEEP_DURATION = 500;

            while (!serverUtils.isServerAvailable()) {
                System.out.println("Waiting for server...");
                Thread.sleep(SLEEP_DURATION);
            }

            System.out.println("Found server, starting program");
        }

        // todo: this needs to be proper lang selection prior to launching the program
        // todo: make this use config
        loadLocale(primaryStage, Locale.of("en", "US"));
    }

    /**
     * Loads fxml files with resources provided based on what locale is passed.
     * Should reinitialize the primary stage when changing locale, so should work dynamically as well.
     * */
    public static void loadLocale(Stage primaryStage, Locale locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("language", locale);
        var overview = FXML.load(NoteOverviewCtrl.class, resourceBundle, "client", "scenes", "MainScreen.fxml");
        var add = FXML.load(AddNoteControl.class, resourceBundle, "client", "scenes", "AddNote.fxml");
        var title = FXML.load(NewNoteTitleCtrl.class, resourceBundle, "client", "scenes", "newTitle.fxml");
        var searchContent = FXML.load(SearchNoteContentCtrl.class, resourceBundle,
                "client", "scenes", "SearchNoteContent.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, overview, add, searchContent, title);
    }
}

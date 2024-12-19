
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

import client.handlers.NoteSearchResult;
import client.handlers.SceneInfo;
import client.services.Logger;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;

/**
 * Controller class for managing the primary stage and scenes of the client application.
 * <p>
 * The {@code MainCtrl} class acts as a central point for navigation and UI management.
 * It initializes the main application stage and handles transitions between the
 * "Quote Overview" and "Add Quote" scenes.
 * </p>
 * <p>
 * The class depends on {@link NoteOverviewCtrl} and {@link AddNoteControl} for managing
 * specific functionalities in their respective scenes. It also dynamically updates
 * the stage's title and content based on the active scene.
 * </p>
 */
public class MainCtrl {

    private Stage primaryStage;
    private Stage searchContentStage;

    private NoteOverviewCtrl overviewCtrl;
    private AddNoteControl addCtrl;
    private NewNoteTitleCtrl newCtrl;
    private SearchNoteContentCtrl searchNoteContentCtrl;

    private Scene overview;
    private Scene add;
    private Scene title;

    private final Logger logger = new Logger();

    /**
     * Initializes the primary stage and sets up the scenes and controllers for the application.
     *
     * @param primaryStage the main application stage
     * @param overview     a pair containing the controller and root node for the note overview scene
     * @param add          a pair containing the controller and root node for the add note scene
     */
    public void initialize(Stage primaryStage,
                           Pair<NoteOverviewCtrl, Parent> overview,
                           Pair<AddNoteControl, Parent> add,
                           Pair<SearchNoteContentCtrl, Parent> searchContent,
                           Pair<NewNoteTitleCtrl, Parent> title) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());
        this.addCtrl = add.getKey();
        this.newCtrl = title.getKey();
        this.add = new Scene(add.getValue());
        this.title = new Scene(title.getValue());

        showOverview();
        primaryStage.show();

        initializeSearchContentStage(searchContent);
        searchNoteContentCtrl.init();
    }

    /**
     * Create an additional stage that handles the searchContent functionality
     * This class will always be shown above the mainScene
     *
     * @param searchContent the controller for the searchContentScene
     */
    private void initializeSearchContentStage(Pair<SearchNoteContentCtrl, Parent> searchContent) {
        this.searchNoteContentCtrl = searchContent.getKey();
        Scene searchContentScene = new Scene(searchContent.getValue());
        this.searchContentStage = new Stage();
        searchContentStage.setTitle("SearchContent");
        searchContentStage.setScene(searchContentScene);
        searchContentStage.setAlwaysOnTop(true);
    }

    /**
     * Shows the main overview and refreshes the tableView with all the notes from the server
     */
    public void showOverview() {
        primaryStage.setTitle("Main");
        primaryStage.setScene(overview);
        overview.setOnKeyPressed(e -> overviewCtrl.keyPressed(e));
        overviewCtrl.emptySearchText();
        overviewCtrl.refresh();
    }

    public void showOverview(NoteSearchResult searchResult) {
        showOverview();
        overviewCtrl.show(searchResult);
    }

    public void logRegular(String message) {
        String logString = logger.addRegularLog(message);
        overviewCtrl.log(logString);
    }

    public void logError(String message) {
        String logString = logger.addErrorLog(message);
        overviewCtrl.log(logString);
    }

    /**
     * shows the "add a note" scene
     */
    public void showAdd() {
        primaryStage.setTitle("Notes: Adding Note");
        primaryStage.setScene(add);
        addCtrl.clearFields();
        addCtrl.getNoteTitle().setFocusTraversable(Boolean.FALSE);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showNewTitle() {
        primaryStage.setTitle("New Title");
        primaryStage.setScene(title);
        title.setOnKeyPressed(e -> newCtrl.keyPressed(e));
    }

    public NewNoteTitleCtrl getNewCtrl() {
        return newCtrl;
    }

    public NoteOverviewCtrl getOverviewCtrl() {
        return overviewCtrl;
    }

    /**
     * Toggles between light and dark theme
     */

    public boolean changeTheme() {
        if (overview.getStylesheets().isEmpty()) {
            overview.getStylesheets().add(getClass().getResource("contrast.css").toExternalForm());
            return true;
        } else {
            overview.getStylesheets().clear();
            return false;
        }
    }

    /**
     * Shows the searchResult UI that lists the notes and their indices where a SearchValue is found
     * in the content of a note. The User is able to select one of the SearchResults in a listView
     * and go to the corresponding note and index inside the content of that note.
     *
     * @param searchResult the list of all notes and their indices where a certain SearchValue was found
     */
    public void showSearchContent(List<NoteSearchResult> searchResult) {
        SceneInfo searchContentSceneInfo = getSceneInfo(searchContentStage);
        if (searchResult.isEmpty()) {
            searchContentStage.hide();
            return;
        }

        applySceneInfo(searchContentStage, searchContentSceneInfo);
        searchContentStage.show();
        Platform.runLater(() -> primaryStage.requestFocus());

        searchNoteContentCtrl.setSearchResult(searchResult);
    }

    /**
     * Get the size and position of a scene
     *
     * @param stage the stage that the scene is associated with
     * @return a SceneInfo that contains the size and position of the provided stage
     */
    public SceneInfo getSceneInfo(Stage stage) {
        Point2D size = new Point2D(stage.getWidth(), stage.getHeight());
        Point2D pos = new Point2D(stage.getX(), stage.getY());
        return new SceneInfo(size, pos);
    }

    /**
     * Applies the provided sceneInfo to the provided stage
     *
     * @param stage the stage to apply the size and position to
     * @param sceneInfo the size and position to apply
     */
    public void applySceneInfo(Stage stage, SceneInfo sceneInfo) {
        stage.setWidth(sceneInfo.getSize().getX());
        stage.setHeight(sceneInfo.getSize().getY());
        stage.setX(sceneInfo.getPos().getX());
        stage.setY(sceneInfo.getPos().getY());
    }
}

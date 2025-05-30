
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

import client.MyStorage;
import client.handlers.NoteSearchResult;
import client.handlers.StageInfo;
import client.services.Logger;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.List;
import java.util.ResourceBundle;

import static commons.exceptions.InternationalizationKeys.*;

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

    private MyStorage storage;
    private Stage primaryStage;
    private Stage searchContentStage;
    private Stage collectionsStage;
    private Stage addCollectionStage;

    private NoteOverviewCtrl overviewCtrl;
    private AddNoteControl addCtrl;
    private NewNoteTitleCtrl newCtrl;
    private SearchNoteContentCtrl searchNoteContentCtrl;
    private CollectionOverviewCtrl collectionOverviewCtrl;
    private AddCollectionCtrl addCollectionCtrl;

    private Scene overview;
    private Scene add;
    private Scene title;
    private final int xCoordinate = 950;
    private final int yCoordinate = 400;
    private final Logger logger = new Logger();

    public Scene getOverviewScene() { return overview; }

    public Scene getSearchContentScene() { return searchContentStage.getScene(); }

    public Scene getAddScene() { return add; }

    public Scene getTitleScene() { return title; }

    public Stage getPrimaryStage() { return primaryStage; }

    private boolean isDarkMode;

    public boolean isDarkMode() {
        return isDarkMode;
    }

    /**
     * Initializes the primary stage and sets up the scenes and controllers for the application.
     *
     * @param primaryStage the main application stage
     * @param overview     a pair containing the controller and root node for the note overview scene
     * @param add          a pair containing the controller and root node for the add note scene
     */
    public void initialize(MyStorage storage,
                           Stage primaryStage,
                           Pair<NoteOverviewCtrl, Parent> overview,
                           Pair<AddNoteControl, Parent> add,
                           Pair<SearchNoteContentCtrl, Parent> searchContent,
                           Pair<NewNoteTitleCtrl, Parent> title,
                           Pair<CollectionOverviewCtrl, Parent> collections,
                           Pair<AddCollectionCtrl, Parent> addCollections) {
        this.storage = storage;
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());
        this.addCtrl = add.getKey();
        this.newCtrl = title.getKey();

        this.add = new Scene(add.getValue());
        this.title = new Scene(title.getValue());
        this.isDarkMode = storage.getTheme().equals("dark");

        showOverview();
        primaryStage.show();

        initializeSearchContentStage(searchContent);
        searchNoteContentCtrl.init();

        initializeCollectionStage(collections);
        collectionOverviewCtrl.init();

        initializeAddCollectionStage(addCollections);
        
        if (isDarkMode) overviewCtrl.changeTheme();
    }

    /**
     * Initializes an additional stage that handles the searchContent functionality
     * This class will always be shown above the mainScene
     *
     * @param searchContent the controller for the searchContentScene
     */
    private void initializeSearchContentStage(Pair<SearchNoteContentCtrl, Parent> searchContent) {
        this.searchNoteContentCtrl = searchContent.getKey();
        Scene searchContentScene = new Scene(searchContent.getValue());
        this.searchContentStage = new Stage();
        searchContentStage.setTitle(getResourceBundle().getString(SEARCH_CONTENT.getKey()));
        searchContentStage.setScene(searchContentScene);
        searchContentStage.setAlwaysOnTop(true);
        searchContentStage.setOnCloseRequest(e -> saveStageInfo(searchContentStage));
    }

    /**
     * Initializes an additional stage that handles the users ability to control notes using collections
     *
     * @param collections the controller for the collections
     */
    private void initializeCollectionStage(Pair<CollectionOverviewCtrl, Parent> collections) {
        this.collectionOverviewCtrl = collections.getKey();
        Scene collectionScene = new Scene(collections.getValue());
        this.collectionsStage = new Stage();
        collectionsStage.setTitle(getResourceBundle().getString(COLLECTIONS.getKey()));
        collectionsStage.setScene(collectionScene);
    }

    /**
     * Initializes an additional stage that handles the creation of new collections
     *
     * @param addCollections the controller for the adding of new collections
     */
    private void initializeAddCollectionStage(Pair<AddCollectionCtrl, Parent> addCollections) {
        this.addCollectionCtrl = addCollections.getKey();
        Scene addCollectionScene = new Scene(addCollections.getValue());
        this.addCollectionStage = new Stage();
        addCollectionStage.setTitle(getResourceBundle().getString(COLLECTIONS.getKey()));
        addCollectionStage.setScene(addCollectionScene);
        addCollectionScene.setOnKeyPressed(e -> addCollectionCtrl.keyPressed(e));
    }

    /**
     * Shows the main overview and refreshes the tableView with all the notes from the server
     */
    public void showOverview() {
        primaryStage.setTitle(getResourceBundle().getString(MAIN.getKey()));
        primaryStage.setScene(overview);
        overview.setOnKeyPressed(e -> overviewCtrl.keyPressed(e));
        overviewCtrl.emptySearchText();
        overviewCtrl.refresh();
    }

    public void showOverview(NoteSearchResult searchResult) {
        searchContentStage.hide();
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
        primaryStage.setTitle(getResourceBundle().getString(ADD_NOTE.getKey()));
        primaryStage.setScene(add);
        addCtrl.clearFields();
        addCtrl.getNoteTitle().setFocusTraversable(Boolean.FALSE);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showNewTitle() {
        primaryStage.setTitle(getResourceBundle().getString(EDIT_TITLE.getKey()));
        primaryStage.setScene(title);
        title.setOnKeyPressed(e -> newCtrl.keyPressed(e));
    }

    public NewNoteTitleCtrl getNewCtrl() {
        return newCtrl;
    }

    public NoteOverviewCtrl getOverviewCtrl() {
        return overviewCtrl;
    }

    public CollectionOverviewCtrl getCollectionOverviewCtrl() {
        return collectionOverviewCtrl;
    }

    /**
     * Toggles between light and dark theme
     */

    public boolean changeTheme() {
        if (!overview.getStylesheets().contains(getClass().getResource("contrast.css").toExternalForm())) {
            overview.getStylesheets().add(getClass().getResource("contrast.css").toExternalForm());
            add.getStylesheets().add(getClass().getResource("contrast.css").toExternalForm());
            title.getStylesheets().add(getClass().getResource("contrast.css").toExternalForm());
            searchContentStage.getScene().getStylesheets().add(getClass().getResource("contrast.css").toExternalForm());
            collectionsStage.getScene().getStylesheets().add(getClass().getResource("contrast.css").toExternalForm());
            addCollectionStage.getScene().getStylesheets().add(getClass().getResource("contrast.css").toExternalForm());
            isDarkMode = true;
            storage.setTheme("dark");
            return true;
        } else {
            overview.getStylesheets().clear();
            add.getStylesheets().clear();
            title.getStylesheets().clear();
            searchContentStage.getScene().getStylesheets().clear();
            collectionsStage.getScene().getStylesheets().clear();
            addCollectionStage.getScene().getStylesheets().clear();
            isDarkMode = false;
            storage.setTheme("light");
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
        if (searchResult.isEmpty()) {
            searchContentStage.hide();
            return;
        }

        StageInfo searchContentStageInfo = storage.getSearchNoteContentStageInfo();
        applySceneInfo(searchContentStage, searchContentStageInfo);
        searchContentStage.show();

        Platform.runLater(() -> primaryStage.requestFocus());
        searchNoteContentCtrl.setSearchResult(searchResult);
    }

    public void showCollections() {
        collectionOverviewCtrl.refresh();
        collectionsStage.show();
        collectionsStage.toFront();
    }

    public void showAddCollection() {
        addCollectionStage.show();
    }

    public void closeAddCollection() {
        addCollectionStage.hide();
        collectionOverviewCtrl.refresh();
    }

    private void saveStageInfo(Stage stage) {
        StageInfo stageInfo = getStageInfo(stage);
        storage.setSearchNoteContentStageInfo(stageInfo);
    }

    /**
     * Get the size and position of a stage
     *
     * @param stage the stage that the scene is associated with
     * @return a StageInfo that contains the size and position of the provided stage
     */
    public StageInfo getStageInfo(Stage stage) {
        return new StageInfo(stage.getWidth(), stage.getHeight(), stage.getX(), stage.getY());
    }

    /**
     * Applies the provided sceneInfo to the provided stage
     *
     * @param stage the stage to apply the size and position to
     * @param stageInfo the size and position to apply
     */
    public void applySceneInfo(Stage stage, StageInfo stageInfo) {
        if (stageInfo == null) {
            searchContentStage.sizeToScene();
            searchContentStage.setX(xCoordinate);
            searchContentStage.setY(yCoordinate);
        } else {
            stage.setWidth(stageInfo.width());
            stage.setHeight(stageInfo.height());
            stage.setX(stageInfo.x());
            stage.setY(stageInfo.y());
        }
    }

    public ResourceBundle getResourceBundle() {
        return overviewCtrl.getNoteOverviewService().getResourceBundleHolder().getResourceBundle();
    }

    public MyStorage getStorage() {
        return this.storage;
    }
}

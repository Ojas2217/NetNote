
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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

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
    private NoteOverviewCtrl overviewCtrl;
    private Scene overview;
    private AddNoteControl addCtrl;
    private Scene add;
    private Scene title;

    /**
     * Initializes the primary stage and sets up the scenes and controllers for the application.
     *
     * @param primaryStage the main application stage
     * @param overview a pair containing the controller and root node for the note overview scene
     * @param add a pair containing the controller and root node for the add note scene
     */
    public void initialize(Stage primaryStage,
                           Pair<NoteOverviewCtrl, Parent> overview,
                           Pair<AddNoteControl, Parent> add,
                           Pair<NewNoteTitleCtrl, Parent> title) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());
        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());
        this.title = new Scene(title.getValue());
        showOverview();
        primaryStage.show();
    }

    /**
     * Shows the main overview and refreshes the tableView with all the notes from the server
     */
    public void showOverview() {
        primaryStage.setTitle("Main");
        primaryStage.setScene(overview);

        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Notes: Adding Note");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showNewTitle() {
        primaryStage.setTitle("New Title");
        primaryStage.setScene(title);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }
}

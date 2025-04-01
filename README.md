# NetNote

NetNote is a distributed Note Taking application made as a part of the CSE1105 course at TU Delft. It Uses JavaFX for the Frontend and SpringBoot for the backend.

**Table Of Contents**\
[Installation](#installation-and-usage)\
[Layout](#layout)\
[Shortcuts](#program-shortcuts)\
[Features](#implemented-features)

# Installation and Usage
To run the project from the command line, you either need to have [Maven](https://maven.apache.org/install.html) installed on your local system (`mvn`) or you need to use the Maven wrapper (`mvnw`). You can then execute

	mvn clean install

to package and install the artifacts for the three subprojects. Afterwards, you can run ...

	cd server
	mvn spring-boot:run

to start the server or ...

	cd client
	mvn javafx:run

to run the client. Please note that the server needs to be running, before the client will start.

# Layout
The program consists of multiple elements namely:

## Searching
On top of the program a searchbar can be found which the user can interact with.
This searchbar will automatically filter all available note titles and find any that contain the given input.
The filtered notes will be displayed inside the 'Note selection'.

If the input starts with '#' a new window will open. This window contains the locations where the input (excluding the '#') is/are found.
This search function searches all available notes. Clicking on any of the found items will make the program go to that specific note and select the part that matches the input.
This window will automatically close when no result is found, whilst also remembering its size and location when it was closed so that it is able to open on a formatted basis.

The 'Clear' button located at the top right will clear the searchbars input.

## Note selection
On the left side of the program a list of all available notes on the server are displayed. Each note can be selected and viewed.
Next to that the overview lets the user right-click any individual note and edit the selected note using a sub-menu.
This sub-menu consists of several buttons:
1. Edit note title: This option lets the user rename the title of the selected note
2. Delete note: This option lets the user delete the selected note
3. Refresh: This option lets the user manually refresh the selected note from the server

## Note content overview
In the center of the program the content of the currently selected note is displayed.
The content of the currently selected note can be edited by left-clicking the overview and typing any character.

## Markdown renderer
On the right side of the program a rendered version of the content of the currently selected note is displayed to the user.
This rendering is handled by Markdown.
Note: The webview DOES NOT change colours when you click the 'change theme' button, this is because the content of the webview can be customized by the user. (using the config file webView.css)

## Logger
On the bottom center another markdown rendering can be observed. This is a logger which keeps track of certain events like adding and deleting of notes,
and displays this to the user, thus providing informative feedback.

## Bottom actions
On the bottom of the program several buttons can be found. These include:
1. Edit title: Allows for the editing of the title of the currently selected note
2. Refresh: Allows for the manual refreshing of the currently selected note
3. Delete: Allows for the deletion of the currently selected note
4. Add: Allows for the creation of a new note
5. Switch theme: Allows for the switching of themes
6. Languages: Allows for live switching between languages of the program

# Program Shortcuts
**When the user has selected a note and is viewing the main scene**
- Enter: This will refresh the currently selected note
- Escape: This will focus the searchbar
- 'A': This will prompt the creation of a new note
- Ctrl + 'T': This will prompt the changing of the currently selected title

**When the user is creating a new note title**
- Enter: This will confirm the new title
- Escape: This will cancel the new title creation

# Config files

**There are three config files, all located in the 'config' directory**
1. For the markdown webview (webView.css), note: even though we have two webviews being used in our app, only the markdown one was to be styled according to the requirements.
2. For the user language and theme (userConfig.properties)
3. For Collections (collections.txt)

# Implemented Features

## Collections
The Collections button can be found on the main screen, when clicked it prompts a menu containing all the collection, here you can:
- add new collections
- delete collections
- move notes between collections (using drag and drop)
- see all collections (using the see all button)
Things to be noted:
- When no collection is selected (either on startup or when the 'see-all' button is clicked) any new note will be added to the DEFAULT Collection.
- If you make any changes (new title) to a note when no collection is selected please select that notes collection (default if none) to view the change
- The default collection can not be changed.

## Live Language Switch
Our app supports six languages, all of which can be accessed through the drop-down menu on the bottom right
- Once you change your language, the flag icon next to the box gets updated, alternatively the user can also click on this flag to access the language menu

## Automated change synchronization

Our app uses websockets to synchronize changes in live time (Only changes related to a note are synchronized)
to be noted: we use a buffer to buffer the text content of a note, as updating with every keystroke would be inefficient and cause bugs.

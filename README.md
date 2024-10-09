# Six Qui Prend - Card Game (JavaFX)
<img src="https://www.letempledujeu.fr/IMG/arton12671.jpg?1670253592" alt="Game Image" width="200px"/>

## Introduction

This project is an implementation of the card game "6 qui prend!" using **JavaFX** and SceneBuilder. The goal of the project is to provide an interactive, playable version of the game with a graphical user interface (GUI). It allows users to play against **computer-controlled bots**.

The game is developed for single-player mode with the option to play against 1 to 3 bots. The bot difficulty can be set to either "Noob" or "Pro," and after each game, the user is presented with their ranking and the option to start a new game or change settings.

- Link to the **rules** of the game: [Rules of Six Qui Prend](https://cdn.1j1ju.com/medias/e6/4b/d2-6-qui-prend-regle.pdf)

---


## Features & Functionality

- **Single-player gameplay**: The user can play against AI-controlled bots.
- **Bot customization**: Choose between 1 to 3 bots, with adjustable difficulty levels ("Noob" or "Pro").
- **Graphical User Interface**: Interactive and user-friendly interface built with JavaFX and SceneBuilder.
- **Endgame Summary**: After each game, the user is presented with the final rankings and can choose to start a new game or modify settings.
- **Game rules adherence**: Implements all the core rules of "6 qui prend!", including card placements, scoring, and penalties.

---


## Project Structure

### Root Directory
- **mvnw** & **mvnw.cmd**: Maven wrapper scripts that allow the project to be built without requiring Maven to be installed on the system.
- **pom.xml**: The Maven Project Object Model file, which manages project dependencies, build configurations, and other settings.
- **Regles.pdf**: A reference document containing the official game rules of "6 qui prend!" in French.

### `src/main/java/com/isep/sixquiprend/Core/`
This folder contains the core game logic, implementing the rules and mechanics of "6 qui prend!":
- **Bot.java**: Handles the logic and behavior of AI-controlled players (bots), including decision-making based on difficulty levels ("Noob" or "Pro").
- **Card.java**: Represents a card in the game, containing the card's value and associated "têtes de boeuf" (penalty points).
- **Game.java**: The main class managing the overall game flow, handling turns, card placement, and player actions.
- **GameState.java**: Tracks the current state of the game, including card placements and player scores.
- **Player.java**: Manages human player actions, score calculation, and interactions with the game.
- **Row.java**: Represents a row of cards on the board, managing the addition of new cards according to the game's rules.

### `src/main/java/com/isep/sixquiprend/GUI/`
This folder contains classes responsible for the graphical user interface (GUI), developed using JavaFX:
- **GameApplication.java**: The **main entry point** for the application, responsible for launching the JavaFX application and initializing the GUI.
- **DialogueBox.java**: Manages the in-game dialogue boxes for communication with the player (e.g., instructions, alerts, game events).
- **EndGamePopupController.java**: Handles the endgame popup, displaying the final rankings and giving the player the option to restart or change settings.
- **GameController.java**: Manages the main gameplay interface, handling player actions, card placement, and game interactions.
- **ProfileCreationController.java**: Controls the player profile creation screen, where players can create or select their in-game profile.
- **StartController.java**: Manages the main start menu, allowing the player to start a new game, access settings, or exit.

### `src/main/resources/com/isep/sixquiprend/GUI/`
Contains FXML files used by JavaFX to define the layout and structure of the GUI screens:
- **DialogueBox.fxml**: Defines the layout for the dialogue box shown during the game.
- **EndGamePopup.fxml**: Layout for the endgame popup, displaying the game results and options for the player.
- **game.fxml**: The main gameplay screen layout, displaying the card table, player stats, and game controls.
- **ProfileCreation.fxml**: Layout for the profile creation and selection screen.
- **Start.fxml**: Layout for the starting menu, allowing the player to start a game or adjust settings.

### `src/main/resources/com/isep/sixquiprend/images/`
This folder contains the images used in the game's GUI:
- **cards/**: Contains all images for the cards in the game, each representing one of the 104 unique cards in "6 qui prend!".
- **elements/**: Stores UI elements such as icons and profile images

---


## Technologies Used

- **Java**: The primary programming language used for game logic.
- **JavaFX**: A framework used for building the graphical user interface.
- **SceneBuilder**: A visual design tool to create JavaFX FXML files for the GUI layout.
- **Maven**: A build automation tool that manages project dependencies and configurations.

---

## Gameplay

### 1. Home

<img src="Gameplay%20Images/Home.png" alt="Home Screen" width="450px"/>

---

### 2. Game Configuration
This screen allows players to configure the game before starting. Players can set up options such as the number of players and other settings.

<img src="Gameplay%20Images/Game%20Configuration.png" alt="Game Configuration" width="450px"/>

---

### 3. In game - Before playing a card
This is the screen where the player chooses a card from their deck to play during the game.

<img src="Gameplay%20Images/Game%20-%20Play%20a%20card.png" alt="Play a Card" width="450px"/>

---

### 4. In game - After playing a card
This screenshot shows the game in action after a card has been played, illustrating how the board updates with the newly played cards. To see the animations, watch the video.

<img src="Gameplay%20Images/Game%20-%20Animation%20after%20play.png" alt="Game Animation After Play" width="450px"/>


---

### 5. Game End

<img src="Gameplay%20Images/Game%20end.png" alt="Game End" width="450px"/>

## Demo Video


[Watch the demo video](https://www.youtube.com/your-video-link-here)



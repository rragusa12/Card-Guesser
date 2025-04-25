package workspace;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.nio.file.Paths;
import javafx.scene.control.TextField;

public class MainApplication extends Application {

    private String playerName;
    private String difficulty;
    private int level;

    private MediaPlayer mediaPlayer;  // For background music
    private boolean isMuted = false;  // For mute/unmute toggle

    @Override
    public void start(Stage primaryStage) {
        // Set the initial stage and show the first scene
        primaryStage.setTitle("Card Guessing Game");

        // Create and set up the NameScene (Player Name Input)
        Scene nameScene = createNameScene(primaryStage);

        // Set the initial scene
        primaryStage.setScene(nameScene);
        primaryStage.show();
    }

    private Scene createNameScene(Stage primaryStage) {
        // Create a VBox to vertically arrange elements
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);  // Center the elements inside the VBox
    
        // Create a welcome label
        Label welcomeLabel = new Label("Welcome to Card Guesser!");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    
        // Create a label for the player name input prompt
        Label namePromptLabel = new Label("Enter Player name:");
    
        // Create a TextField for player name input
        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter your name");
        nameInput.setMaxWidth(250);  // Set a maximum width for the text field so it doesn't span the entire screen
    
        // Create a Start Game button
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> {
            String playerName = nameInput.getText().trim();
            if (!playerName.isEmpty()) {
                // Save the player's name
                setPlayerName(playerName);
    
                // Transition to the DifficultyScene
                Scene difficultyScene = createDifficultyScene(primaryStage);
                primaryStage.setScene(difficultyScene);
            } else {
                System.out.println("Please enter a valid name!");
            }
        });
    
        // Add the elements to the VBox
        vbox.getChildren().addAll(welcomeLabel, namePromptLabel, nameInput, startButton);
    
        // Create a StackPane and add the VBox to it
        StackPane namePane = new StackPane();
        namePane.getChildren().add(vbox);
    
        // Return the Scene
        return new Scene(namePane, 600, 400);
    }

    protected Scene createDifficultyScene(Stage primaryStage) {
        VBox vbox = new VBox(10); // VBox for vertically stacking the label and buttons
        vbox.setAlignment(Pos.CENTER); // Center the items within the VBox
        BorderPane difficultyPane = new BorderPane();
        
        // Label at the top
        Label label = new Label("Select Difficulty:");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;"); // Optional styling for label
    
        // Difficulty buttons
        Button easyButton = new Button("Easy");
        easyButton.setOnAction(e -> {
            setDifficulty("Easy");
            setLevel(1); // Set starting level to 1
            Scene gameScene = createGameScene(primaryStage);
            primaryStage.setScene(gameScene);
        });
    
        Button mediumButton = new Button("Medium");
        mediumButton.setOnAction(e -> {
            setDifficulty("Medium");
            setLevel(1);
            Scene gameScene = createGameScene(primaryStage);
            primaryStage.setScene(gameScene);
        });
    
        Button hardButton = new Button("Hard");
        hardButton.setOnAction(e -> {
            setDifficulty("Hard");
            setLevel(1);
            Scene gameScene = createGameScene(primaryStage);
            primaryStage.setScene(gameScene);
        });
    
        Button endlessButton = new Button("Endless");
        endlessButton.setOnAction(e -> {
            setDifficulty("Endless");
            setLevel(1);
            Scene gameScene = createGameScene(primaryStage);
            primaryStage.setScene(gameScene);
        });
    
        // Add the label and buttons to the VBox
        vbox.getChildren().addAll(label, easyButton, mediumButton, hardButton, endlessButton);
    
        // Set VBox in the center of the BorderPane
        difficultyPane.setCenter(vbox);
    
        // Return the Scene with the BorderPane layout
        return new Scene(difficultyPane, 600, 400);
    }

    // Create Game Scene
    protected Scene createGameScene(Stage primaryStage) {
        // Initialize the GameScene with the selected difficulty, player name, and level
        GameScene gameScene = new GameScene(playerName, difficulty, level);
        Scene scene = gameScene.createGameScene(primaryStage);

        // Set up background music and mute button
        setupMusic(primaryStage, scene);

        return scene;
    }

    private void setupMusic(Stage primaryStage, Scene scene) {
        // Load the music file
        String musicFile = "workspace/music/launch-bg-music.mp3"; // Correct path to your music file
        Media media = new Media(Paths.get(musicFile).toUri().toString());
        mediaPlayer = new MediaPlayer(media);

        // Start playing the music on loop
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loops the music
        mediaPlayer.play();

        // Create a mute button in the top-right corner
        Button muteButton = new Button("Mute");
        muteButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        muteButton.setOnAction(e -> toggleMute());

        // Add the mute button to the scene (top-right corner)
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(muteButton);
        borderPane.setRight(muteButton);

        // Set the scene to the border pane
        StackPane gamePane = new StackPane();
        gamePane.getChildren().add(borderPane);
        scene.setRoot(gamePane);
    }

    private void toggleMute() {
        if (isMuted) {
            mediaPlayer.setMute(false);
            isMuted = false;
        } else {
            mediaPlayer.setMute(true);
            isMuted = true;
        }
    }

    // Set player name
    public void setPlayerName(String name) {
        this.playerName = name;
    }

    // Set game difficulty
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    // Set game level
    public void setLevel(int level) {
        this.level = level;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

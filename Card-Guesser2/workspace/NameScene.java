package workspace;

import javafx.geometry.Pos;  // Import Pos for alignment
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.nio.file.Paths;

public class NameScene {

    private MediaPlayer mediaPlayer;
    private boolean isMuted = false;

    public Scene createNameScene(Stage primaryStage, MainApplication app) {
        StackPane namePane = new StackPane();

        // Create a TextField for player name input
        Label label = new Label("Enter your Player Name:");
        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter your name");

        // Create a Start Game button
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> {
            String playerName = nameInput.getText().trim();
            if (!playerName.isEmpty()) {
                // Save the player's name
                app.setPlayerName(playerName);

                // Transition to the DifficultyScene
                Scene difficultyScene = createDifficultyScene(primaryStage, app);
                primaryStage.setScene(difficultyScene);
            } else {
                System.out.println("Please enter a valid name!");
            }
        });

        // Create the mute button
        Button muteButton = new Button("Mute");
        muteButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        muteButton.setOnAction(e -> toggleMute());

        // Add mute button to the top-right corner
        BorderPane nameBorderPane = new BorderPane();
        nameBorderPane.setTop(muteButton);
        nameBorderPane.setCenter(namePane);
        
        // Add the label, input, and button to the StackPane
        namePane.getChildren().addAll(label, nameInput, startButton);
        StackPane.setAlignment(nameInput, Pos.CENTER);  // Align TextField in center
        StackPane.setAlignment(startButton, Pos.BOTTOM_CENTER);  // Align button at the bottom center
        
        // Set up the media player for background music
        setupMusic(app);

        return new Scene(nameBorderPane, 600, 400);
    }

    private Scene createDifficultyScene(Stage primaryStage, MainApplication app) {
        StackPane difficultyPane = new StackPane();

        // Difficulty buttons
        Button easyButton = new Button("Easy");
        easyButton.setOnAction(e -> {
            app.setDifficulty("Easy");
            app.setLevel(1); // Set starting level to 1
            Scene gameScene = app.createGameScene(primaryStage);
            primaryStage.setScene(gameScene);
        });

        Button mediumButton = new Button("Medium");
        mediumButton.setOnAction(e -> {
            app.setDifficulty("Medium");
            app.setLevel(1);
            Scene gameScene = app.createGameScene(primaryStage);
            primaryStage.setScene(gameScene);
        });

        Button hardButton = new Button("Hard");
        hardButton.setOnAction(e -> {
            app.setDifficulty("Hard");
            app.setLevel(1);
            Scene gameScene = app.createGameScene(primaryStage);
            primaryStage.setScene(gameScene);
        });

        Button endlessButton = new Button("Endless");
        endlessButton.setOnAction(e -> {
            app.setDifficulty("Endless");
            app.setLevel(1);
            Scene gameScene = app.createGameScene(primaryStage);
            primaryStage.setScene(gameScene);
        });

        difficultyPane.getChildren().addAll(easyButton, mediumButton, hardButton, endlessButton);
        return new Scene(difficultyPane, 600, 400);
    }

    private void setupMusic(MainApplication app) {
        // Ensure that the media player is initialized
        String musicFile = "workspace/launch-bg-music.mp3"; // Update with your actual path
        Media media = new Media(Paths.get(musicFile).toUri().toString());
        mediaPlayer = new MediaPlayer(media);

        // Start playing the music on loop
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loops the music
        mediaPlayer.play();
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
}

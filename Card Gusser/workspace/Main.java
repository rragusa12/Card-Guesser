/**
* @description 
* This program is a basic card guessing game. 
* It features two modes where the player tries to guess either the suit or the rank
* of a hidden card drawn from a standard deck.
* Each guess must be made within a 15-second time limit, and players progress through 10 levels
* earning score points for correct guesses, with optional vague hints available 
* that reduce the score earned. The application includes simple sound effects, background, 
* images and basic popups for game feedback and navigation.
*
*/
package workspace;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.InputStream; 


public class Main extends Application {

    private MediaPlayer backgroundMusicPlayer; 
    private boolean isMuted = false;          
    private String difficulty = "Easy";      
    private int currentLevel = 1;         

    private Stage primaryStage;      
    private Scene rulesScene;           
    private Scene difficultyScene;          
    private Scene gameScene;                 

    private final Deck gameDeck = new Deck();

    private static final double SCENE_WIDTH = 800;
    private static final double SCENE_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Card Master"); 

        setupBackgroundMusic();
        SoundManager.loadSounds();

        showRulesScene();

        primaryStage.show();
    }

    public void showRulesScene() {
        if (rulesScene == null) {
            Rules rulesScreen = new Rules(); // get the rules ui content
            // wrap the content with background and create the scene
            rulesScene = wrapWithBackground(rulesScreen.createRulesContent(this));
        }
        primaryStage.setScene(rulesScene); // set the current scene to rules
    }

    // shows the difficulty selection screen
    public void showDifficultyScene() {
         if (difficultyScene == null) {
            Rules rulesScreen = new Rules(); // reuse Rules class for its difficulty part
            // wrap content with background and create scene
            difficultyScene = wrapWithBackground(rulesScreen.createDifficultySelectionContent(this));
        }
        primaryStage.setScene(difficultyScene); // set the current scene
    }

    // shows the main game screen
    public void showGameScene() {
        // always create a new game scene instance when starting a new game
        GameScene gameManager = new GameScene(this);
        gameScene = wrapWithBackground(gameManager.createGameContent());
        primaryStage.setScene(gameScene); 
    }

    // loads and prepares the background music
    private void setupBackgroundMusic() {
        try {
             String musicResourceUri = Main.class.getResource("/workspace/music/launch-bg-music.mp3").toExternalForm();
             Media media = new Media(musicResourceUri);
             backgroundMusicPlayer = new MediaPlayer(media);
             backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // make it loop forever
             if (!isMuted) {
                 backgroundMusicPlayer.play();
             }
        } catch (Exception e) { 
            System.err.println("Error loading background music: " + e.getMessage());
            backgroundMusicPlayer = null; 
        }
    }

    // toggles the music mute state
    public void toggleMusicMute() {
        if (backgroundMusicPlayer != null) {
            isMuted = !isMuted; // flip the mute state
            backgroundMusicPlayer.setMute(isMuted); // apply to player
            System.out.println("Music Muted: " + isMuted);
        }
    }

    // allows other classes to check if music is muted
    public boolean isMuted() {
        return isMuted;
    }

    // sets the chosen difficulty
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        System.out.println("Difficulty set to: " + difficulty);
    }

    // gets the current difficulty
    public String getDifficulty() {
        return difficulty;
    }

    // sets the current level 
    public void setLevel(int level) {
        this.currentLevel = level;
         System.out.println("Level set to: " + level);
    }

    // gets the current level
    public int getLevel() {
        return currentLevel;
    }

    // provides access to the shared game deck
    public Deck getGameDeck() {
        return gameDeck;
    }

    // helper to wrap ui content with a background image and create a scene
    public Scene wrapWithBackground(Parent content) {
        StackPane stack = new StackPane(); // use stackpane to layer background and content

        try {
             InputStream bgStream = getClass().getResourceAsStream("/workspace/background/background.jpg");
             if (bgStream == null) {
                 System.err.println("Background image resource not found!");
                 stack.getChildren().add(content);
             } else {
                 Image backgroundImage = new Image(bgStream);
                 ImageView backgroundView = new ImageView(backgroundImage);

                 backgroundView.setFitWidth(SCENE_WIDTH);
                 backgroundView.setFitHeight(SCENE_HEIGHT);
                 backgroundView.setPreserveRatio(false);

                 // add background first, then content on top
                 stack.getChildren().addAll(backgroundView, content);
             }
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            if (!stack.getChildren().contains(content)) {
                stack.getChildren().add(content);
            }
        }

        return new Scene(stack, SCENE_WIDTH, SCENE_HEIGHT);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
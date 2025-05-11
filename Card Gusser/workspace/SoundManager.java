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

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SoundManager {

    public static final String SOUND_CORRECT = "CORRECT";
    public static final String SOUND_WRONG = "WRONG";
    public static final String SOUND_WIN = "WIN";
    public static final String SOUND_LOSE = "LOSE";
    public static final String SOUND_FLIP = "FLIP";

    private static Media correctMedia;
    private static Media wrongMedia;
    private static Media winMedia;
    private static Media loseMedia;
    private static Media flipMedia;

    private static boolean soundsLoaded = false; 

    // loads all sound files when the game starts
    public static void loadSounds() {
        if (soundsLoaded) {
             return;
        }
        try {
            correctMedia = loadMedia("/workspace/sounds/correct.mp3");
            wrongMedia   = loadMedia("/workspace/sounds/wrong.mp3");
            winMedia     = loadMedia("/workspace/sounds/win.mp3");
            loseMedia    = loadMedia("/workspace/sounds/lose.mp3");
            flipMedia    = loadMedia("/workspace/sounds/flip.mp3");

            soundsLoaded = true;
            System.out.println("Sounds loaded successfully.");
        } 
        catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
        }
    }

    private static Media loadMedia(String resourcePath) {
         try {
             // get the full path to the resource
             String resourceUri = SoundManager.class.getResource(resourcePath).toExternalForm();
             // create the media object
             return new Media(resourceUri);
         } 
         catch (Exception e) {
             System.err.println("Failed to load sound resource: " + resourcePath + " - " + e.getMessage());
             return null;
         }
    }

    // plays the requested sound effect
    public static void playSound(String soundIdentifier) {
        if (!soundsLoaded) {
            System.err.println("Cannot play sound: " + soundIdentifier + " (Sounds not loaded)");
            return;
        }

        Media mediaToPlay = null;

        if (soundIdentifier.equals(SOUND_CORRECT)) {
            mediaToPlay = correctMedia;
        } 
        else if (soundIdentifier.equals(SOUND_WRONG)) {
            mediaToPlay = wrongMedia;
        } 
        else if (soundIdentifier.equals(SOUND_WIN)) {
            mediaToPlay = winMedia;
        } 
        else if (soundIdentifier.equals(SOUND_LOSE)) {
            mediaToPlay = loseMedia;
        } 
        else if (soundIdentifier.equals(SOUND_FLIP)) {
            mediaToPlay = flipMedia;
        } 
        else {
            System.err.println("Unknown sound identifier: " + soundIdentifier);
            return; 
        }

        if (mediaToPlay == null) {
            System.err.println("Cannot play sound: " + soundIdentifier + " (Media not loaded)");
            return;
        }

        try {
            MediaPlayer mediaPlayer = new MediaPlayer(mediaToPlay);
            mediaPlayer.seek(Duration.ZERO); 
            mediaPlayer.play();
        } 
        catch (Exception e) {
            System.err.println("Error playing sound " + soundIdentifier + ": " + e.getMessage());
        }
    }
}
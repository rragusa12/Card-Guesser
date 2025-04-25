package workspace;

import javafx.stage.Stage;
import javafx.animation.RotateTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.File;

public class GameScene {

    private String playerName;
    private String difficulty;
    private int level;
    private int lives;
    private boolean isCardFlipped;
    private boolean isMuted;
    private MediaPlayer mediaPlayer;
    private Card currentCard;
    private int hintCount;
    private ImageView cardImageView; // For displaying card images
    private String guessedSuit;  // Store guessed suit as a string
    private String guessedRank;  // Store guessed rank as a string

    public GameScene(String playerName, String difficulty, int level) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.level = level;
        this.lives = 5; // Starting lives for each game
        this.isCardFlipped = false;
        this.isMuted = false;
        this.hintCount = difficulty.equals("Easy") ? 3 : difficulty.equals("Medium") ? 2 : difficulty.equals("Hard") ? 1 : 0;
    }

    public Scene createGameScene(Stage primaryStage) {
        StackPane gamePane = new StackPane();

        // Display player name, difficulty, level, and lives
        Text playerInfo = new Text("Player: " + playerName + " | Difficulty: " + difficulty + " | Level: " + level + " | Lives: " + lives + " | Hints: " + hintCount);

        // Create the card placeholder (use ImageView for card image display)
        cardImageView = new ImageView();
        cardImageView.setFitWidth(100);
        cardImageView.setFitHeight(150);
        cardImageView.setImage(new Image(getClass().getResource("/Card_images/0_back_of_card.png").toExternalForm())); // Initial card back

        // Buttons for guessing the card
        Button guessCardButton = new Button("Guess Card");
        guessCardButton.setOnAction(e -> {
            boolean correctGuess = makeGuess(); // Check guess

            if (correctGuess) {
                flipCard(); // Flip card after correct guess
                nextLevel(); // Call nextLevel after a correct guess
            } else {
                lives--;
                if (lives <= 0) {
                    gameOver(); // Call gameOver when lives drop to zero
                    // Transition to difficulty scene (if game over)
                    Scene difficultyScene = new MainApplication().createDifficultyScene(primaryStage);
                    primaryStage.setScene(difficultyScene);
                }
            }
        });

        // Music toggle button
        Button muteButton = new Button("Mute Music");
        muteButton.setOnAction(e -> toggleMute());

        // Hint button
        Button hintButton = new Button("Hint");
        hintButton.setOnAction(e -> useHint());

        // Add components to the game pane
        gamePane.getChildren().addAll(playerInfo, cardImageView, guessCardButton, muteButton, hintButton);

        // Layout positioning
        cardImageView.setTranslateY(-50);
        guessCardButton.setTranslateY(100);
        muteButton.setTranslateY(150);
        hintButton.setTranslateY(200);

        // Initialize background music
        String musicFile = "path_to_music_file.mp3"; // Replace with actual music path
        Media media = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop music
        mediaPlayer.play();

        // Initialize currentCard randomly for the game
        setRandomCard();

        return new Scene(gamePane, 600, 400);
    }

    private boolean makeGuess() {
        int guessedSuitInt = convertSuitToInt(guessedSuit);
        int guessedRankInt = convertRankToInt(guessedRank);

        // Guess checking logic based on difficulty
        if (difficulty.equals("Easy")) {
            return currentCard.getSuit() == guessedSuitInt;
        } else if (difficulty.equals("Medium")) {
            return currentCard.getRank() == guessedRankInt;
        } else if (difficulty.equals("Hard")) {
            return currentCard.getSuit() == guessedSuitInt && currentCard.getRank() == guessedRankInt;
        }
        return false;
    }

    private void flipCard() {
        if (isCardFlipped) {
            return; // Prevent flipping back if already flipped
        }

        // Perform flip animation
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(cardImageView);
        rotate.setByAngle(180);
        rotate.setDuration(Duration.millis(500)); // Flip duration
        rotate.setCycleCount(1);

        rotate.setOnFinished(event -> {
            // After flip, update the image to show the guessed card
            String cardImagePath = getCardImagePath(currentCard.getRank(), currentCard.getSuit());
            Image cardImage = new Image(getClass().getResource("/Card_images/" + cardImagePath + ".png").toExternalForm());
            cardImageView.setImage(cardImage); // Update image to the correct card
        });

        rotate.play();
        isCardFlipped = true;
    }

    private void useHint() {
        if (hintCount > 0) {
            String hintMessage = "";
            if (difficulty.equals("Easy")) {
                hintMessage = "Hint: The suit is " + currentCard.getSuit();
            } else if (difficulty.equals("Medium")) {
                hintMessage = "Hint: The rank is " + currentCard.getRank();
            } else if (difficulty.equals("Hard")) {
                hintMessage = "Hint: The suit is " + currentCard.getSuit() + " and the rank is " + currentCard.getRank();
            }

            System.out.println(hintMessage);
            hintCount--;
            System.out.println("Hints remaining: " + hintCount);
        } else {
            System.out.println("No hints left!");
        }
    }

    private void toggleMute() {
        if (isMuted) {
            mediaPlayer.play(); // Unmute
            isMuted = false;
        } else {
            mediaPlayer.pause(); // Mute
            isMuted = true;
        }
    }

    // Helper methods to convert suit and rank to integers
    private int convertSuitToInt(String suit) {
        switch (suit.toLowerCase()) {
            case "hearts": return 1;
            case "diamonds": return 2;
            case "clubs": return 3;
            case "spades": return 4;
            default: return -1; // Invalid suit
        }
    }

    private int convertRankToInt(String rank) {
        switch (rank.toLowerCase()) {
            case "ace": return 1;
            case "2": return 2;
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            case "8": return 8;
            case "9": return 9;
            case "10": return 10;
            case "jack": return 11;
            case "queen": return 12;
            case "king": return 13;
            default: return -1; // Invalid rank
        }
    }

    private void nextLevel() {
        level++;
        // Logic for moving to the next level and increasing difficulty (if applicable)
        System.out.println("Next level: " + level);
        // Reset card for the next round
        setRandomCard();
    }

    private void gameOver() {
        System.out.println("Game Over!");
        // Transition back to the difficulty selection screen or any other relevant screen
    }

    private void setRandomCard() {
        // Randomly generate a new card for the current game round
        // The card could be selected from a predefined deck or dynamically generated.
        currentCard = new Card();  // Assuming Card constructor initializes a card randomly
    }

    // Helper method to generate the card image path
    private String getCardImagePath(int rank, int suit) {
        String[] suits = {"clubs", "diamonds", "hearts", "spades"};
        String cardImagePath = "";

        // Handle specific cases like joker or back of card
        if (rank == 0 && suit == -1) {
            return "back_of_card"; // Image name for the back of the card
        } else if (rank == 0 && suit == 0) {
            return "joker_black"; // Image name for the black joker
        } else if (rank == 0 && suit == 1) {
            return "joker_red"; // Image name for the red joker
        }

        // Handle numbered cards (2 through 10)
        if (rank >= 2 && rank <= 10) {
            cardImagePath = rank + "_of_" + suits[suit];
        }
        // Handle face cards (jack, queen, king)
        else if (rank == 11) {
            cardImagePath = "jack_of_" + suits[suit];
        } else if (rank == 12) {
            cardImagePath = "queen_of_" + suits[suit];
        } else if (rank == 13) {
            cardImagePath = "king_of_" + suits[suit];
        }
        // Handle ace cards
        else if (rank == 14) {
            cardImagePath = "ace_of_" + suits[suit];
        }

        return cardImagePath;
    }
}

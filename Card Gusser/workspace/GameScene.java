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

import javafx.animation.*;
import javafx.application.*; 
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class GameScene {

    private Main mainApp;           
    private Deck deck;              // the deck of cards for the game
    private String difficulty;      // easy or hard
    private Level currentLevelData; 
    private int currentLevelNumber; 
    private int score;              
    private int totalHintsRemaining;
    private boolean hintUsedThisLevel; 

    // ui elements 
    private ImageView cardImageView2; 
    private HBox cardDisplayArea;     
    private BorderPane layout;
    private VBox centerArea;
    private Text playerInfoText;
    private Text timerText;
    private ImageView cardImageView;
    private HBox guessInputBox;
    private ComboBox<String> suitComboBox; // dropdown for easy mode
    private ComboBox<String> rankComboBox; // dropdown for hard mode
    private Button guessButton;
    private Button hintButton;
    private Button restartButton;
    private Button quitToMenuButton;
    private Text feedbackText;      // shows correct, incorrect, hints

    // timer stuff
    private Timeline levelTimer;    // the timer itself
    private int secondsLeft;        // how many seconds are left on the timer

    // card image
    private Image cardBackImage;    
    // lists of possible suits and ranks 
    private final List<String> SUITS = List.of("Hearts", "Diamonds", "Clubs", "Spades");
    private final List<String> RANKS = List.of("2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace");

    // game state flag
    private boolean levelInProgress; // is the player currently playing a level
    private int level10GuessIndex; // 0 = guessing first card, 1 = guessing second card

    // sets up the game scene object
    public GameScene(Main mainApp) {
        this.mainApp = mainApp;
        this.difficulty = mainApp.getDifficulty();
        this.deck = mainApp.getGameDeck();
        this.currentLevelNumber = 1;
        this.score = 0;
        this.totalHintsRemaining = 3;
        loadResources();
    }

    // loads the card back image resource
     private void loadResources() {
         try {
             InputStream cs = getClass().getResourceAsStream("/workspace/Card_images/0_back_of_card.png");
             if (cs != null) {
                 cardBackImage = new Image(cs);
             } 
             else {
                 cardBackImage = null;
                 System.err.println("Card back image resource not found!");
             }
         } catch (Exception e) {
             System.err.println("Error loading card back image: " + e);
             cardBackImage = null;
         }
     }

    // creates the main layout node for the game screen
    public Parent createGameContent() {
        layout = new BorderPane();
        layout.setPadding(new Insets(10));

        layout.setTop(createTopBar());
        layout.setCenter(createCenterArea());
        layout.setBottom(createBottomBar());

        startLevel(currentLevelNumber);
        return layout;
    }


    // creates the top info bar score, timer, and mute button
    private HBox createTopBar() {
        playerInfoText = new Text();
        playerInfoText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        playerInfoText.setFill(Color.WHITE);

        timerText = new Text("Time: --");
        timerText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        timerText.setFill(Color.YELLOW);

        Button muteButton = createMuteButton(mainApp);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(20); // spacing
        topBar.getChildren().addAll(playerInfoText, timerText, spacer, muteButton); // add elements
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        updatePlayerInfoUI(); // set initial text
        return topBar;
    }

    // creates the center area  
    private VBox createCenterArea() {
        centerArea = new VBox(15); centerArea.setAlignment(Pos.CENTER); 
        centerArea.setPadding(new Insets(20));
        List<Node> nodesToAdd = new ArrayList<>();

        // HBox to hold one or two card views
        cardDisplayArea = new HBox(10); 
        cardDisplayArea.setAlignment(Pos.CENTER);

        // Create both ImageViews
        cardImageView = new ImageView();
        cardImageView.setFitWidth(120); 
        cardImageView.setFitHeight(180);

        cardImageView2 = new ImageView(); // second card view (for level 10)
        cardImageView2.setFitWidth(120);
         cardImageView2.setFitHeight(180);

        if (cardBackImage != null) {
            cardImageView.setImage(cardBackImage);
            cardImageView2.setImage(cardBackImage); // Set back image initially for both
            cardDisplayArea.getChildren().add(cardImageView); // Add first view now
        } 
        else {
            StackPane cardPlaceholder = new StackPane(new Label("Card Back\nMissing"));
            cardPlaceholder.setPrefSize(120 * 2 + 10, 180); 
            cardPlaceholder.setStyle("-fx-border-color: white; -fx-border-width: 1;");
            nodesToAdd.add(cardPlaceholder); // Add placeholder instead of HBox
        }

        // Add the card display HBox if images loaded
        if (cardBackImage != null) {
            nodesToAdd.add(cardDisplayArea);
        }

        // Setup other controls (remain the same)
        guessInputBox = new HBox(10); 
        guessInputBox.setAlignment(Pos.CENTER); 
        nodesToAdd.add(guessInputBox);

        hintButton = new Button("Use Hint"); 
        styleButton(hintButton); 
        hintButton.setOnAction(e -> useHint()); 
        nodesToAdd.add(hintButton);

        feedbackText = new Text("");
        feedbackText.setFont(Font.font("Arial", 14));
        feedbackText.setFill(Color.CYAN); 
        nodesToAdd.add(feedbackText);

        guessButton = new Button("Guess"); 
        styleButton(guessButton); 
        guessButton.setOnAction(e -> processGuess()); 
        nodesToAdd.add(guessButton);

        centerArea.getChildren().addAll(nodesToAdd);
        return centerArea;
    }

    // creates the bottom bar restart and quit buton
     private HBox createBottomBar() {
         restartButton = new Button("Restart Game");
         styleButton(restartButton);
         restartButton.setOnAction(e -> askToRestartGame());

         quitToMenuButton = new Button("Quit to Menu");
         styleButton(quitToMenuButton);
         quitToMenuButton.setOnAction(e -> askToQuitToMenu());

         Region spacer = new Region(); // pushes buttons right
         HBox.setHgrow(spacer, Priority.ALWAYS);

         // arrange buttons horizontally, pushed right
         HBox bottomBar = new HBox(10); // spacing
         bottomBar.getChildren().addAll(spacer, restartButton, quitToMenuButton); // add elements
         bottomBar.setAlignment(Pos.CENTER_RIGHT);
         bottomBar.setPadding(new Insets(10));
         return bottomBar;
     }

    // sets up and starts a new level
    private void startLevel(int levelNum) {
        this.currentLevelNumber = levelNum;
        this.hintUsedThisLevel = false; 
        this.levelInProgress = true;
        this.level10GuessIndex = 0;

        if (levelNum == 1) { deck.shuffle(); }

        currentLevelData = new Level(deck, levelNum);

        if (currentLevelData == null || currentLevelData.getCardsToGuess().isEmpty()) {
            System.err.println("Failed start level " + levelNum);
            // show error popup safely
            Platform.runLater(() -> {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Could not start level " + levelNum + ".\nReturning to menu.", ButtonType.OK);
                errorAlert.setTitle("Level Error"); errorAlert.setHeaderText("Card Drawing Failed");
                stopTimer(); 
                errorAlert.showAndWait(); 
                mainApp.showDifficultyScene();
            });
            return;
        }

        updatePlayerInfoUI();
        resetCardView(); // reset both image views to card back
        setupGuessInputsForLevel();
        feedbackText.setText(""); feedbackText.setFill(Color.CYAN);
        enableControls(); // make buttons clickable

        // manage visibility and content of the card display area
        cardDisplayArea.getChildren().clear(); // clear previous cards
        cardDisplayArea.getChildren().add(cardImageView); // always add the first one
        if (levelNum == 10 && cardImageView2 != null) {
            // level 10: add the second image view as well
            cardDisplayArea.getChildren().add(cardImageView2);
            feedbackText.setText("Level 10: Guess the first card!"); // initial prompt
        }

        startTimer();   
        System.out.println("Starting Level: " + levelNum);
    }

    // sets up the correct guess input dropdowns based on difficulty
    private void setupGuessInputsForLevel() {
        guessInputBox.getChildren().clear(); // clear old ones
        suitComboBox = null; // reset
        rankComboBox = null;

        // easy mode
        if (difficulty.equals("Easy")) {
            suitComboBox = new ComboBox<>();
            suitComboBox.getItems().addAll(SUITS);
            suitComboBox.setValue(SUITS.get(0)); 
            Label suitLabel = new Label("Guess Suit:");
            suitLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            guessInputBox.getChildren().addAll(suitLabel, suitComboBox);
        }
        // hard mode
        else if (difficulty.equals("Hard")) {
            rankComboBox = new ComboBox<>();
            rankComboBox.getItems().addAll(RANKS);
            rankComboBox.setValue(RANKS.get(0)); 
            Label rankLabel = new Label("Guess Rank:");
            rankLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            guessInputBox.getChildren().addAll(rankLabel, rankComboBox);
        }
    }


    // handles the player clicking the Guess button 
    private void processGuess() {
        if (!levelInProgress || currentLevelData == null || currentLevelData.getCardsToGuess().isEmpty()) {
            return;
        }

        List<Card> targetCards = currentLevelData.getCardsToGuess();
        boolean correct = false;
        Card currentCardToGuess = null;
        int currentGuessIndex;

        // determine current guess index
        if (currentLevelNumber == 10) {
            currentGuessIndex = level10GuessIndex;
        } else {
            currentGuessIndex = 0;
        }

        // validate index
        if (currentGuessIndex < 0 || currentGuessIndex >= targetCards.size()) {
            System.err.println("Error: Invalid guess index " + currentGuessIndex + " for level " + currentLevelNumber);
            return;
        }

        currentCardToGuess = targetCards.get(currentGuessIndex);

        // check guess based on difficulty
        if (difficulty.equals("Easy")) {
            String guessedSuitStr = null;
            if (suitComboBox != null) {
                guessedSuitStr = suitComboBox.getValue();
            }
            int guessedSuitInt = convertSuitToInt(guessedSuitStr);
            if (guessedSuitInt == currentCardToGuess.getSuit()) {
                correct = true;
            }
        } else if (difficulty.equals("Hard")) {
            String guessedRankStr = null;
            if (rankComboBox != null) {
                guessedRankStr = rankComboBox.getValue();
            }
            int guessedRankInt = convertRankToInt(guessedRankStr);
            if (guessedRankInt == currentCardToGuess.getRank()) {
                correct = true;
            }
        }

        // handle result
        if (correct) {
            SoundManager.playSound(SoundManager.SOUND_CORRECT);
            animateCardFlip(currentCardToGuess, true, currentGuessIndex);

            if (currentLevelNumber == 10) {
                if (currentGuessIndex == 0) {
                    // first card correct in level 10
                    levelInProgress = true;
                    level10GuessIndex = 1;
                    feedbackText.setText("First card correct! Now guess the second card.");
                    feedbackText.setFill(Color.LIGHTBLUE);
                    hintUsedThisLevel = false;
                    if (totalHintsRemaining > 0) {
                        hintButton.setDisable(false);
                    } else {
                        hintButton.setDisable(true);
                    }
                    stopTimer();
                    startTimer();
                } else {
                    // second card correct in level 10
                    levelInProgress = false;
                    stopTimer();
                    disableControls();
                    awardXP();
                    updatePlayerInfoUI();
                    feedbackText.setText("Second card correct! Level 10 Complete!");
                    feedbackText.setFill(Color.LIMEGREEN);
                }
            } else {
                // correct guess for levels 1-9
                levelInProgress = false;
                stopTimer();
                disableControls();
                awardXP();
                updatePlayerInfoUI();
                feedbackText.setText("Correct!");
                feedbackText.setFill(Color.LIMEGREEN);
            }
        } else {
            SoundManager.playSound(SoundManager.SOUND_WRONG);
            feedbackText.setText("Incorrect! Try again.");
            feedbackText.setFill(Color.RED);
        }
    }


    // called only after the FINAL correct guess animation for a level finishes
    private void handlePostFlip(boolean wasCorrect) {
        if (wasCorrect) {
            final int MAX_LEVELS = 10;

            // Check if the level just fully completed was the final one needed
            // This should only be true if we just finished level 10 (cardIndex==1) or level < 10
            if (currentLevelNumber == MAX_LEVELS) { // Win condition
                SoundManager.playSound(SoundManager.SOUND_WIN);
                Platform.runLater(() -> showEndGamePopup(true, "Congratulations! You beat " + difficulty + " mode!"));
            } 
            else if (currentLevelNumber < MAX_LEVELS) { 
                PauseTransition delay = new PauseTransition(Duration.seconds(0.75));
                delay.setOnFinished(e -> startLevel(currentLevelNumber + 1));
                delay.play();
            }
        }
    }

    private void useHint() {
        if (totalHintsRemaining > 0 && levelInProgress && currentLevelData != null && !currentLevelData.getCardsToGuess().isEmpty()) {
    
            // determine which card to give hint for based on level 10 state
            int cardIndex;
            if (currentLevelNumber == 10) {
                cardIndex = level10GuessIndex;
            } else {
                cardIndex = 0;
            }
    
            Card targetCard = null;
            if (cardIndex >= 0 && cardIndex < currentLevelData.getCardsToGuess().size()) {
                targetCard = currentLevelData.getCardsToGuess().get(cardIndex);
            }
    
            if (targetCard == null) return;
    
            totalHintsRemaining--;
    
            // only flag hintUsedThisLevel if it's the final guess required for the level's score
            if (currentLevelNumber < 10) {
                hintUsedThisLevel = true;
            } else if (currentLevelNumber == 10 && level10GuessIndex == 1) {
                hintUsedThisLevel = true;
            }
    
            hintButton.setDisable(true); // disable hint button after use 
            updatePlayerInfoUI();
            feedbackText.setFill(Color.ORANGE);
    
            // provide vague hint based on difficulty and the target card
            if (difficulty.equals("Easy")) {
                int suit = targetCard.getSuit();
                String colorHint;
                if (suit <= 1) {
                    colorHint = "Red (Hearts/Diamonds)";
                } else {
                    colorHint = "Black (Clubs/Spades)";
                }
                feedbackText.setText("Hint: The suit color is " + colorHint);
            } else if (difficulty.equals("Hard")) {
                int rank = targetCard.getRank();
                String rangeHint;
                if (rank <= 6) {
                    rangeHint = "Low (2-6)";
                } else if (rank <= 10) {
                    rangeHint = "Medium (7-10)";
                } else {
                    rangeHint = "High (Jack-Ace)";
                }
                feedbackText.setText("Hint: The rank is in the " + rangeHint + " range.");
            }
    
            if (totalHintsRemaining <= 0) {
                hintButton.setDisable(true);
            }
    
        } else if (totalHintsRemaining <= 0) {
            feedbackText.setText("No hints remaining!");
            feedbackText.setFill(Color.YELLOW);
            hintButton.setDisable(true);
        }
    }
    


    // calculates and adds score for completing a level
    private void awardXP() {
        int levelBonus = currentLevelNumber;
        int earnedXP;
        // simple score: 10 base + level number, but only 5 if hint used
        if (hintUsedThisLevel) {
            earnedXP = 5;
        }
        else {
            earnedXP = 10 + levelBonus;
        }
        score += earnedXP; // add score

        System.out.println("Level " + currentLevelNumber + " complete. +" + earnedXP + " XP (Hint used: " + hintUsedThisLevel + ")");
    }


    // called when the timer runs out
    private void handleTimeout() {
         if (!levelInProgress) return;
         levelInProgress = false; 
         stopTimer(); 
         disableControls(); 
         SoundManager.playSound(SoundManager.SOUND_LOSE);
         Platform.runLater(() -> showEndGamePopup(false, "Time's up! The card was: " + getTargetCardInfo()));
     }

     // helper to disable all interactive controls
     private void disableControls() {
        if (guessButton != null) {
            guessButton.setDisable(true);
        }
        if (quitToMenuButton != null) {
            quitToMenuButton.setDisable(true);
        }
        if (hintButton != null) {
            hintButton.setDisable(true);
        }
        if (restartButton != null) {
            restartButton.setDisable(true);
        }
        if (suitComboBox != null) {
            suitComboBox.setDisable(true);
        }
        if (rankComboBox != null) {
            rankComboBox.setDisable(true);
        }
     }

     // helper to enable interactive controls
     private void enableControls() {
         if (guessButton != null){
             guessButton.setDisable(false);
         }
         if (quitToMenuButton != null) {
            quitToMenuButton.setDisable(false);
         }
         if (hintButton != null) {
            hintButton.setDisable(totalHintsRemaining <= 0 || !levelInProgress);
         }
         if (restartButton != null) {
            restartButton.setDisable(false);
        }
         if (suitComboBox != null) {
            suitComboBox.setDisable(false);
        }
         if (rankComboBox != null) {
            rankComboBox.setDisable(false);
        }
     }


    // handles the Restart Game button click
    private void askToRestartGame() {
        stopTimer(); 
        boolean wasInProgress = levelInProgress; // remember if level was active
        levelInProgress = false; 

        // show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Restart Game");
        alert.setHeaderText("Restart from Level 1?");
        alert.setContentText("Restart "+difficulty+" mode from Level 1? Score resets.");

        // get the users choice ok or cancel
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL); // default cancel

        if (result == ButtonType.OK) { // user confirmed restart
             deck.shuffle(); score = 0; totalHintsRemaining = 3; // reset state
             startLevel(1); // start level 1
        } 
        else { // user cancelled
            levelInProgress = wasInProgress; // restore state
            // resume timer only if level was active before
            if (wasInProgress && secondsLeft > 0 && levelTimer != null) {
                 levelTimer.play(); 
                 enableControls(); 
            } 
            else { 
                 disableControls();
                 // ensure timer shows 0 and is red if time was up or timer invalid
                 if (secondsLeft <=0) {
                    updateTimerText(); 
                    if(timerText != null) timerText.setFill(Color.RED);
                 }
            }
        }
    }

    // handles the Quit to Menu button 
    private void askToQuitToMenu() {
        stopTimer(); 
        boolean wasInProgress = levelInProgress; // remember state
        levelInProgress = false; // prevent actions

        // show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit Game");
        alert.setHeaderText("Quit to Menu?");
        alert.setContentText("Return to difficulty selection? Score (" + score + ") is lost.");
        ButtonType yesButton = new ButtonType("Yes, Quit", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("No, Continue", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);

        // get the users choice yes or no
        ButtonType result = alert.showAndWait().orElse(noButton); // default no

        if (result == yesButton) { // user confirmed quit
            mainApp.showDifficultyScene(); // go back to difficulty screen
        } 
        else { 
            levelInProgress = wasInProgress; // restore state
            // resume timer only if level was active before
            if (wasInProgress && secondsLeft > 0 && levelTimer != null) {
                 levelTimer.play();
                 enableControls(); 
            } 
            else { 
                disableControls();
                // ensure timer shows 0 and is red if time was up or timer invalid
                if (secondsLeft <=0) {
                   updateTimerText(); // Make sure text shows 0
                   if(timerText != null) timerText.setFill(Color.RED);
                }
            }
        }
    }

    // starts the level timer
    private void startTimer() {
        stopTimer(); 
        secondsLeft = 15; // set timer duration 
        updateTimerText(); // display initial time
        timerText.setFill(Color.YELLOW);

        levelTimer = new Timeline(); 
        levelTimer.setCycleCount(Timeline.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {
             // check if level ended prematurely 
             if (!levelInProgress) {
                 stopTimer(); // stop timer if level no longer active
                 return;
             }

             secondsLeft--; // count down
             updateTimerText(); // update display

             if (secondsLeft <= 0) { // check if time ran out
                 handleTimeout();
             } 
             else if (secondsLeft <= 5) { // flash red when time is low
                 if (secondsLeft % 2 == 0) {
                     timerText.setFill(Color.RED);
                 } 
                 else {
                     timerText.setFill(Color.WHITE);
                 }
             } 
             else {
                 timerText.setFill(Color.YELLOW);
             }
        });

        levelTimer.getKeyFrames().add(frame); // add the action to the timer
        levelTimer.play(); // start the timer running
    }

    // stop time
    private void stopTimer() {
        if (levelTimer != null) {
            levelTimer.stop(); // stop the timeline
        }
        // update text display one last time
        updateTimerText();
        // make sure color is red if stopped at 0 or less
        if (secondsLeft <= 0) {
            if (timerText != null) { 
                timerText.setFill(Color.RED);
            }
        }
    }

    // updates the timer text display
    private void updateTimerText() {
         if (timerText != null) {
             timerText.setText("Time: " + Math.max(0, secondsLeft));
         }
     }

    // updates the player info text 
    private void updatePlayerInfoUI() {
        if (playerInfoText != null) {
            playerInfoText.setText("Level: " + currentLevelNumber + " | Score: " + score + " | Hints: " + totalHintsRemaining);
        }
    }

    // resets the card image(s) to show the back
    private void resetCardView() {
        if (cardImageView != null && cardBackImage != null) {
            cardImageView.setImage(cardBackImage);
            cardImageView.setRotate(0); cardImageView.setScaleX(1);
        }
        // also reset the second image view
        if (cardImageView2 != null && cardBackImage != null) {
            cardImageView2.setImage(cardBackImage);
            cardImageView2.setRotate(0); cardImageView2.setScaleX(1);
        }
        // clear effects
        if (cardImageView != null) {
            cardImageView.setEffect(null);
        }
        if (cardImageView2 != null) {
            cardImageView2.setEffect(null);
        }
    }

    // gets the image object for a specific card
    private Image getCardImage(Card card) {
        if (card == null) return cardBackImage; // safety check
        String filename = getCardImageFilename(card); // get filename
        if (filename == null) return cardBackImage; // safety check
        try {
            String resourcePath = "/workspace/Card_images/" + filename; // build path
            InputStream imageStream = getClass().getResourceAsStream(resourcePath); // load stream
            if (imageStream == null) {
                System.err.println("Image resource not found: " + resourcePath); 
                return cardBackImage; 
            }
            return new Image(imageStream); // create image
        }
        catch (Exception e) { 
            System.err.println("Error loading image " + filename + ": " + e.getMessage()); 
            return cardBackImage;
        }
    }

     // generates the filename string for a card image
     private String getCardImageFilename(Card card) {
        if (card == null || card.getSuit() < 0 || card.getSuit() >= SUITS.size()) return null; 
        int rankIndex = card.getRank() - 2; // map rank 2-14 to index 0-12
        if (rankIndex < 0 || rankIndex >= RANKS.size()) return null; // validate rank
        String suitName = SUITS.get(card.getSuit()).toLowerCase();
        String rankName = RANKS.get(rankIndex).toLowerCase();
        return rankName + "_of_" + suitName + ".png"; 
     }

     private String getTargetCardInfo() {
        if (currentLevelData != null && !currentLevelData.getCardsToGuess().isEmpty()) {
            List<Card> cards = currentLevelData.getCardsToGuess();
            if (currentLevelNumber == 10 && cards.size() == 2) {
                // level 10 show both cards
                String card1Info;
                if (cards.get(0) != null) {
                    card1Info = cards.get(0).toString();
                } else {
                    card1Info = "Invalid Card 1";
                }
    
                String card2Info;
                if (cards.get(1) != null) {
                    card2Info = cards.get(1).toString();
                } else {
                    card2Info = "Invalid Card 2";
                }
    
                return card1Info + " and " + card2Info;
            } else if (!cards.isEmpty()) {
                // levels 1-9 show the single card
                Card target = cards.get(0);
                if (target != null) {
                    return target.toString();
                } else {
                    return "Invalid Card";
                }
            }
        }
        return "Unknown";  
    }
    

    // simplified card reveal 
    private void animateCardFlip(Card cardToShow, boolean correctGuess, int cardIndex) {
        ImageView targetImageView;
        if (cardIndex == 1 && cardImageView2 != null) {
            targetImageView = cardImageView2;
        } else {
            targetImageView = cardImageView;
        }

        if (targetImageView == null || targetImageView.getScene() == null) {
            boolean isFinalGuess;
            if (currentLevelNumber < 10 || cardIndex == 1) {
                isFinalGuess = true;
            } else {
                isFinalGuess = false;
            }

            if (correctGuess && isFinalGuess) {
                handlePostFlip(true);
            }
            return;
        }

        Image faceImage = getCardImage(cardToShow);
        if (faceImage == null) {
            faceImage = cardBackImage;
        }
        final Image finalFaceImage = faceImage;

        PauseTransition showDelay = new PauseTransition(Duration.millis(200));
        showDelay.setOnFinished(event -> {
            targetImageView.setImage(finalFaceImage);

            boolean isFinalGuess;
            if (currentLevelNumber < 10 || cardIndex == 1) {
                isFinalGuess = true;
            } else {
                isFinalGuess = false;
            }

            if (correctGuess && isFinalGuess) {
                handlePostFlip(true);
            }
        });
        showDelay.play();
    }
 
    // shows the final popup when game ends 
    private void showEndGamePopup(boolean isWin, String message) {
        stopTimer(); levelInProgress = false; disableControls(); // stop game state
        Platform.runLater(() -> {
            Alert.AlertType type; // determine popup type
            if (isWin) { 
                type = Alert.AlertType.INFORMATION; 
            } 
            else { 
                type = Alert.AlertType.WARNING; 
            }
            Alert alert = new Alert(type); // create alert
            alert.setHeaderText(message); // set main message

            if (isWin) { // win popup setup
                alert.setTitle("Mode Complete!");
                alert.setContentText("Final Score: " + score + "\nPlay again?");
                // setup buttons for win
                ButtonType rstGame = new ButtonType("Restart Game (" + difficulty + ")");
                ButtonType chgDiff = new ButtonType("Change Difficulty");
                ButtonType quit = new ButtonType("Quit to Menu", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(rstGame, chgDiff, quit);
                // show and handle result
                ButtonType res = alert.showAndWait().orElse(quit); // default quit
                if (res == rstGame) { 
                    deck.shuffle(); score = 0; totalHintsRemaining = 3; startLevel(1); 
                }
                else {
                    mainApp.showDifficultyScene(); 
                } // quit or change difficulty

            } else { // lose popup setup
                 alert.setTitle("Level Failed!");
                 alert.setContentText("Your score: " + score + "\nTry again?");
                 // setup buttons for lose
                 ButtonType rtyLvl = new ButtonType("Retry Level " + currentLevelNumber);
                 ButtonType chgDiff = new ButtonType("Change Difficulty");
                 ButtonType quit = new ButtonType("Quit to Menu", ButtonBar.ButtonData.CANCEL_CLOSE);
                 alert.getButtonTypes().setAll(rtyLvl, chgDiff, quit);
                 // show and handle result
                 ButtonType res = alert.showAndWait().orElse(quit); // default quit
                 if (res == rtyLvl) {
                    startLevel(currentLevelNumber); 
                } // retry same level
                 else { 
                    mainApp.showDifficultyScene(); 
                } // quit or change difficulty
            }
        });
    }

    // converts suit to int
    private int convertSuitToInt(String suit) {
        if (suit == null) {
            return -1; 
        }
        return SUITS.indexOf(suit);
    }

    // converts rank to int
    private int convertRankToInt(String rank) {
        if (rank == null) {
            return -1;
        }
        int index = RANKS.indexOf(rank);
        if (index != -1) {
            return index + 2;
        } 
        else {
            return -1; 
        }
    }

    // creates the mute/unmute button
    private Button createMuteButton(Main mainApp) {
         String buttonText;
         if (mainApp.isMuted()) { 
            buttonText = "Unmute"; 
        } 
        else { 
            buttonText = "Mute"; 
        }
         Button muteButton = new Button(buttonText);
         styleButton(muteButton); 
         muteButton.setOnAction(e -> {
             mainApp.toggleMusicMute();
             if (mainApp.isMuted()) { 
                muteButton.setText("Unmute"); 
            } 
            else { 
                muteButton.setText("Mute"); 
            }
         });
         return muteButton;
    }

     // applies a basic style to a button 
    private void styleButton(Button button) {
          button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
          final String baseStyle = "-fx-background-color: #4682B4;-fx-text-fill: white;-fx-background-radius: 8;-fx-padding: 8 15;";
          button.setStyle(baseStyle);
    }

} 
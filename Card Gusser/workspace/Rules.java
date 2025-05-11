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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Rules {

    public Parent createRulesContent(Main mainApp) {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20)); 

        Button muteButton = createMuteButton(mainApp);
        HBox topBox = new HBox(muteButton);
        topBox.setAlignment(Pos.TOP_RIGHT); 
        layout.setTop(topBox); 

        VBox centerBox = new VBox(30); 
        centerBox.setAlignment(Pos.CENTER); 

        Label titleLabel = new Label("Welcome to Card Master!");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;"); 

        Label rulesLabel = new Label(
                """
                Guess the hidden card based on the chosen difficulty:
                - Easy: Guess the correct Suit (Hearts, Diamonds, Clubs, Spades).
                - Hard: Guess the correct Rank (2, 3, ..., King, Ace).

                You have 15 seconds per level and 3 hints for the entire game.
                Correct guesses earn XP. Using a hint reduces XP earned. Good luck!"""
        );
        rulesLabel.setFont(Font.font("Arial", 16)); 
        rulesLabel.setTextAlignment(TextAlignment.CENTER); 
        rulesLabel.setWrapText(true);
        rulesLabel.setStyle("-fx-text-fill: white; -fx-line-spacing: 5px;");


        Button continueButton = new Button("Continue");
        styleButton(continueButton); 
        continueButton.setOnAction(e -> mainApp.showDifficultyScene());

        centerBox.getChildren().addAll(titleLabel, rulesLabel, continueButton);
        layout.setCenter(centerBox);

        return layout; 
    }

    public Parent createDifficultySelectionContent(Main mainApp) {
         BorderPane layout = new BorderPane();
         layout.setPadding(new Insets(20));

         Button muteButton = createMuteButton(mainApp);
         HBox topBox = new HBox(muteButton);
         topBox.setAlignment(Pos.TOP_RIGHT);
         layout.setTop(topBox);

         VBox centerLayout = new VBox(30); // stack title and button row
         centerLayout.setAlignment(Pos.CENTER);

         Label titleLabel = new Label("Select Difficulty");
         titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
         titleLabel.setStyle("-fx-text-fill: white;");

         HBox buttonBox = new HBox(20); 
         buttonBox.setAlignment(Pos.CENTER);

         // create easy and hard buttons
         Button easyButton = createDifficultyButton("Easy", mainApp);
         Button hardButton = createDifficultyButton("Hard", mainApp); 
         buttonBox.getChildren().addAll(easyButton, hardButton); 
         centerLayout.getChildren().addAll(titleLabel, buttonBox); 
         layout.setCenter(centerLayout);  

        return layout;
    }

    private Button createMuteButton(Main mainApp) {
        String buttonText;
        if (mainApp.isMuted()) {
            buttonText = "Unmute";
        } else {
            buttonText = "Mute";
        }
        Button muteButton = new Button(buttonText);
        styleButton(muteButton); 
        muteButton.setOnAction(e -> {
            mainApp.toggleMusicMute();
            if (mainApp.isMuted()) {
                 muteButton.setText("Unmute");
            } else {
                 muteButton.setText("Mute");
            }
        });
        return muteButton;
    }

    // creates a difficulty selection button ("Easy" or "Hard")
    private Button createDifficultyButton(String difficulty, Main mainApp) {
        Button button = new Button(difficulty);
        styleButton(button);
        button.setMinWidth(150);

        button.setOnAction(e -> {
            mainApp.setDifficulty(difficulty);
            mainApp.setLevel(1); 
            mainApp.getGameDeck().shuffle(); 
            mainApp.showGameScene(); 
        });
        return button;
    }

    private void styleButton(Button button) {
         button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
         final String baseStyle = "-fx-background-color: #4682B4;-fx-text-fill: white;-fx-background-radius: 8;-fx-padding: 8 15;";
         button.setStyle(baseStyle);
     }
}
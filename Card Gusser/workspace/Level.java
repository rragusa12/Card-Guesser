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

import java.util.ArrayList;
import java.util.List;

public class Level {

    private final int levelNumber;
    private final int cardsToGuessCount;
    private final Deck deck;
    private final List<Card> cardsToGuess;

    // creates a level
    public Level(Deck deck, int levelNumber) {
        this.deck = deck;
        this.levelNumber = levelNumber;
        this.cardsToGuess = new ArrayList<>();

        if (levelNumber == 10) {
            this.cardsToGuessCount = 2; // Level 10 has two cards
        } 
        else {
            this.cardsToGuessCount = 1; // Other levels have one card
        }

        prepareGuessingCards(); // draw the cards for the level
    }

    // draws the cards needed for this level from the deck
    private void prepareGuessingCards() {
        this.cardsToGuess.clear(); 
        for (int i = 0; i < cardsToGuessCount; i++) { 
            Card dealtCard = deck.drawCard(); 
            if (dealtCard != null) {
                this.cardsToGuess.add(dealtCard); // add it to this levels list
            } 
            else {
                System.err.println("Error: Could not draw card " + (i+1) + " for level " + levelNumber + "!");
                break; 
            }
        }
    }

    public List<Card> getCardsToGuess() {
        return new ArrayList<>(this.cardsToGuess);
    }
    public int getInitialCardsToGuessCount() {
        return this.cardsToGuessCount;
    }
    public int getLevelNumber() {
        return this.levelNumber;
    }
}
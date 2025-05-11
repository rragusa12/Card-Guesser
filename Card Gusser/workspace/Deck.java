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
import java.util.Collections;
import java.util.List;

public class Deck {
    // list to hold the cards
    private List<Card> cards;
    // index of the next card to draw
    private int currentCardIndex;

    // creates and shuffles a new deck
    public Deck() {
        cards = new ArrayList<>(); // use arraylist to store cards
        for (int suit = 0; suit <= 3; suit++) { // 0-3 for suits
            for (int rank = 2; rank <= 14; rank++) { // 2-14 for ranks
                cards.add(new Card(rank, suit));
            }
        }
        currentCardIndex = 0; // start at the top of the deck
        shuffle(); // shuffle the deck
    }

    // shuffles the deck and resets the draw index
    public void shuffle() {
        Collections.shuffle(cards); 
        currentCardIndex = 0; // reset to the top after shuffling
    }

    // draws the next card from the deck
    public Card drawCard() {
        if (currentCardIndex < cards.size()) {
            return cards.get(currentCardIndex++);
        }
        else {
            System.out.println("Deck empty. Reshuffling...");
            shuffle();
            if (currentCardIndex < cards.size()) {
                 return cards.get(currentCardIndex++);
            } 
            else {
                System.err.println("Error: Deck is fundamentally empty after reshuffle.");
                return null;
            }
        }
    }

    public int cardsRemaining() {
        return cards.size() - currentCardIndex;
    }
}
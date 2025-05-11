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

public class Card {
    // card rank (2-14, where 11=J, 12=Q, 13=K, 14=A)
    private final int rank;
    // card suit (0=Hearts, 1=Diamonds, 2=Clubs, 3=Spades)
    private final int suit;

    // constructor to create a card
    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
    }

    // gets the rank
    public int getRank() {
        return rank;
    }

    // gets the suit
    public int getSuit() {
        return suit;
    }

    // returns a string representation like "Ace of Spades"
    @Override
    public String toString() {
        String rankString;
        // convert rank number to name
        switch (rank) {
            case 11: rankString = "Jack"; break;
            case 12: rankString = "Queen"; break;
            case 13: rankString = "King"; break;
            case 14: rankString = "Ace"; break;
            default: rankString = Integer.toString(rank); // for 2-10
        }

        String suitString;
        // convert suit number to name
        switch (suit) {
            case 0: suitString = "Hearts"; break;
            case 1: suitString = "Diamonds"; break;
            case 2: suitString = "Clubs"; break;
            case 3: suitString = "Spades"; break;
            default: suitString = "Unknown";
        }

        return rankString + " of " + suitString;
    }

    // checks if two cards are the same (same rank and suit)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Card other = (Card) obj;
        // cards are equal if rank and suit match
        return this.rank == other.rank && this.suit == other.suit;
    }
}
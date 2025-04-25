package workspace;

// Card class
class Card {
    private int rank, suit;

    // Default constructor that initializes a random card
    public Card() {
        // Randomly select suit (1 - 4) and rank (1 - 13)
        this.suit = (int) (Math.random() * 4); // 0-3 (Hearts, Diamonds, Clubs, Spades)
        this.rank = (int) (Math.random() * 13) + 1; // 1-13 (Ace to King)
    }

    // Constructor to create a card with specific rank and suit
    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
    }

    // Getters
    public int getRank() {
        return rank;
    }

    public int getSuit() {
        return suit;
    }

    // Converts the card rank and suit to a string format
    public String toString() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String rankStr;
        switch (rank) {
            case 1: rankStr = "Ace"; break; // Ace is 1
            case 11: rankStr = "Jack"; break;
            case 12: rankStr = "Queen"; break;
            case 13: rankStr = "King"; break;
            default: rankStr = String.valueOf(rank); break;
        }
        return rankStr + " of " + suits[suit];
    }

    // Check if two cards are equal (same rank and suit)
    public boolean equals(Card other) {
        return this.rank == other.rank && this.suit == other.suit;
    }
}

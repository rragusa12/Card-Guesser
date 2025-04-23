package workspace;
public class card {
        //Data Members
        int Rank;
        int Suit;
        //Card Constructor
        public card(int Rank, int Suit){
            this.Rank = Rank;
            this.Suit = Suit;
        }
        //get methods
        public int getRank() {
            return Rank;
        }
    
        public int getSuit() {
            return Suit;
        }

    // Override the toString() method
    @Override
    public String toString() {
        String rankString;
        switch (Rank) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                rankString = Integer.toString(Rank);
                break;
            case 11:
                rankString = "Jack";
                break;
            case 12:
                rankString = "Queen";
                break;
            case 13:
                rankString = "King";
                break;
            case 14:
                rankString = "Ace";
                break;
            default:
                rankString = "Unknown";
        }

        String suitString;
        switch (Suit) {
            case 0:
                suitString = "Hearts";
                break;
            case 1:
                suitString = "Diamonds";
                break;
            case 2:
                suitString = "Clubs";
                break;
            case 3:
                suitString = "Spades";
                break;
            default:
                suitString = "Unknown";
        }

        return rankString + " of " + suitString;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Same object instance
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Not a Card object or null
        }
        card other = (card) obj;
        return this.Rank == other.Rank && this.Suit == other.Suit;
    }
}

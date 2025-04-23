package workspace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class deck {
    //member variables
    private List<card> cards;
    //init function
    public deck() {
        cards = new ArrayList<>();
        buildDeck();
        shuffleDeck();
    }
    //methods for manip

    //fills deck to be a standard 52 card deck (consider options here for difficulty?)
    private void buildDeck() {
        for (int suit = 0; suit < 4; suit++) { // 0: Hearts, 1: Diamonds, 2: Clubs, 3: Spades
            for (int rank = 2; rank <= 14; rank++) { // 2 to 14 (Ace)
                cards.add(new card(rank, suit));
            }
        }
    }

    public List<card> getCards() {
        return cards;
    }

    public card dealCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0); // Deal from the top
        }
        return null; // deck is empty
    }

    public int getSize() {
        return cards.size();
    }

    public void shuffleDeck() {
        Collections.shuffle(cards);
    }
}

package workspace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Deck class
class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        buildDeck();
        shuffle();
    }

    private void buildDeck() {
        for (int suit = 0; suit < 4; suit++)
            for (int rank = 2; rank <= 14; rank++)
                cards.add(new Card(rank, suit));
    }

    public void shuffle() { Collections.shuffle(cards); }
    public Card dealCard() { return cards.isEmpty() ? null : cards.remove(0); }
    public int size() { return cards.size(); }
}

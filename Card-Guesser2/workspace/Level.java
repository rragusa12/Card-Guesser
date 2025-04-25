package workspace;
import java.util.ArrayList;
import java.util.List;

// Level class
class Level {
    private int levelNumber;
    private Deck deck;
    private List<Card> guessingList;

    public Level(Deck deck, int levelNumber) {
        this.deck = deck;
        this.levelNumber = levelNumber;
        guessingList = new ArrayList<>();
        prepareCards();
    }

    private void prepareCards() {
        guessingList.clear();
        int num = (levelNumber / 5) + 1;
        for (int i = 0; i < num; i++) {
            Card c = deck.dealCard();
            if (c != null) guessingList.add(c);
        }
    }

    public boolean guess(Card guess) {
        if (!guessingList.isEmpty() && guess.equals(guessingList.get(0))) {
            guessingList.remove(0);
            return true;
        }
        return false;
    }

    public int remaining() { return guessingList.size(); }
    public int getLevelNumber() { return levelNumber; }
    public List<Card> getGuessingList() { return guessingList; }
}

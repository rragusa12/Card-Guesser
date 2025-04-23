package workspace;
import java.util.ArrayList;
import java.util.List;

public class level {
    //member variables
    int levelNumber;
    int guessingCards;  //number of cards the user will guess this round
    deck currentDeck;      //contains the deck object which has the current deck (check deck.java)
    List<card> guessingList; //list of cards to be guessed

    //init function
    public level(deck currentDeck, int levelNumber){
        this.currentDeck = currentDeck;
        this.levelNumber = levelNumber;
        //the number of cards to guess goes up after 5 levels
        this.guessingCards = ((levelNumber/5)+1); //add 1 for int math
        this.guessingList = new ArrayList<>();
        prepareGuessingCards(); //add level cards to this.guessingList
    }
    //void function that prepares a number of cards to guess and adds it to 'this.guessingList'
    private void prepareGuessingCards() {
        this.guessingList.clear(); // Clear the previous list
        for (int i = 0; i < guessingCards; i++) {
            card dealtCard = currentDeck.dealCard();
            if (dealtCard != null) {
                this.guessingList.add(dealtCard);
            } else {
                //System.out.println("Error: Not enough cards in the deck to prepare for the level.");
                break; // Stop if the deck runs out
            }
        }
    }

    //call this function to make a guess and see if your card matches the guessingCard
    public boolean makeGuess(card playerGuess) {
        if (playerGuess.equals(this.guessingList.get(0))){
            this.guessingList.remove(0);
            return true;
        } else {
            return false;
        }
    }

    //getters
    public List<card> getGuessingList() {
        return this.guessingList;
    }

    public int getGuessingListCount() {
        return this.guessingList.size();
    }

    public int getLevelNumber() {
        return this.levelNumber;
    }

    public int getGuessingCardsCount() {
        return this.guessingCards;
    }
}

package workspace;
import java.util.Scanner;
//import java.util.Timer;
//import java.util.TimerTask;



public class startgame {
    public static void main(String[] args) {
        //member variables
        deck gameDeck = new deck();
        Scanner scanner = new Scanner(System.in);
        boolean hasLost;
        int currentLevelInt;
        int playerSuit;
        int playerValue;
        //Timer timer;
        //int timeLimit = 60; //60 seconds per level
        int playerLives;
        boolean cardsGuessed = false;

        //main function
        hasLost = false;
        System.out.println("Welcome to the Card Guessing Game!");
        System.out.println("Please select a starting difficulty level. We recommend between 1-5 for beginners!");
        //Make sure the scanner has a number and not something else (not super relevant)
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // consume the invalid input
        }
        currentLevelInt = scanner.nextInt();
        //while the user has not lost yet...
        while (hasLost == false){
            //reset player lives to 5
            playerLives = 5;
            //create the level of the appropriate levelInt
            level currentLevel = new level(gameDeck,currentLevelInt);
            cardsGuessed = false;
            while (cardsGuessed == false) {

                //Print and receive user input
                System.out.println("Please enter the suit of the card you would like to guess.");
                System.out.println("0 = Hearts, 1 = Diamonds, 2 = Clubs, 3 = Spades :");

                //Make sure the scanner has a number and not something else (not super relevant)
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // consume the invalid input
                }
                playerSuit = scanner.nextInt(); //store suit guess in playerSuit

                System.out.println("Please enter the value of the card you would like to guess.");
                System.out.println("2-10 or 11 = Jack, 12 = Queen, 13 = King, 14 = Ace :");

                //Make sure the scanner has a number and not something else (not super relevant)
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // consume the invalid input
                }
                playerValue = scanner.nextInt();//store value Guess in playerValue

                //create new card object using player inputs
                card playerCard = new card(playerValue, playerSuit);

                //check if that card is correct and if so...
                if (currentLevel.makeGuess(playerCard)){
                    //print win statement
                    System.out.println("You guessed correct!");
                    //see if more cards are available to guess and if not...
                    if (currentLevel.getGuessingListCount() == 0) {
                        cardsGuessed = true;
                        currentLevelInt++;
                        System.out.println("Level Beaten! Proceeding to level: " + currentLevelInt);
                    }
                } else {
                    System.out.println("Incorrect Guess please try again!");
                    playerLives--;
                    System.out.println("Subtracting 1 from your lives, current lives: " + playerLives);
                    //debug print of the actual card for now
                    System.out.println("The actual Card Is:" + currentLevel.guessingList.get(0).toString());
                }
            }

        }










        scanner.close();
    }

}


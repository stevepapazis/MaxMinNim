package minmaxnim;

import java.util.Scanner;


// a class that models the player of the game
class Player {
	Scanner scanner = new Scanner(System.in);
	
	void askQuestion(String message) { System.out.print( "> " + message + " " ); }
	
	void printError(String message) { System.out.println( "  " + message ); }
	
	void inform(String message) { System.out.println( "- " + message ); }
	
	void addEmptyLine() { System.out.println(); }

    // introduce the game to the player
    void explainGame() {
        inform("There is a board with cards and the cards are divided into groups.");
        inform("In each turn you can remove some cards from a single group.");
        inform("The last player to remove cards from the board is the winner.");
        inform("Now, let's play Nim!\n");
    }
	
    // prompt the user for an integer input greater than a given value
    int getInput(String message, String errorMessage) {
        while (true) {
            askQuestion(message);
            try {
                if ( scanner.hasNextLine() ) return Integer.parseInt( scanner.nextLine() );
            } catch (NumberFormatException e) {
                printError(errorMessage);
            }
        } 
    }
    
    // prompt the user for an integer input greater than a given value min
    int getInput(String message, String errorMessage, int min) {
        while (true) {
        	int num = getInput(message, errorMessage);
            if ( num >= min ) return num;
            printError(errorMessage);
        } 
    }
    
    // prompt the user for an integer input greater than a given value min
    // and smaller than a given value max
    int getInput(String message, String errorMessage, int min, int max) {
        while (true) {
        	int num = getInput(message, errorMessage);
        	if ( num >= min && num <= max ) return num;
            printError(errorMessage);
        } 
    }
    
    // ask how many cards are there on the board
    int askNumberOfTotalCards() {
    	return getInput(
    		"How many cards are there on the board?",
    		"Input should be an integer >= 2.",
    		2
    	);
    }
    
    // ask how many groups are there on the board
    int askNumberOfGroups(int totalCards) {
        return getInput(
            "How many groups of cards are there on the board?", 
            String.format("Each group has at least 2 cards. Therefore, the input should be an integer <= %d.", totalCards/2),
            1, totalCards/2
        );
    }
    
    // ask whether the player will setup the board manually
    boolean askToSetupBoardManually() {
        askQuestion("Do you want to set the cards on the board manually? (y/N)");
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes") ? true : false;
    }
    
    // ask how many cards are there in a given group
    int askNumberOfCardsInGroup(int groupNum, int cardsRemainingToDeal) {
        return getInput(
            String.format("How many cards are there in group %d?", groupNum+1),
            String.format("Input should be an integer between 2 and %d.", cardsRemainingToDeal),
            2, cardsRemainingToDeal
        );
    }  
    
    // ask what is the maximum number of cards that can be removed from the board in each turn
    int askBoundForGroup(int groupNum, int groupSize) {
        return getInput(
            String.format("What is the maximum amount of cards that can be removed from group %d in each turn?", groupNum+1),
            String.format("Input should be a positive integer <= %d.", groupSize),
            1, groupSize
        );
    }
    
    // ask the player to pick a group to remove the cards from 
    int askGroup(int boardSize) {
        return getInput(
            String.format("Pick a group between 1 and %d:", boardSize),
            "Invalid group selected, the group is empty.",
            1, boardSize
        ) - 1;
    }
    
    // ask the player how many cards to remove
    int askCards(int groupNum, int bound) {
        return getInput(
            String.format("Pick a number of cards between 1 and %d to be removed from group %d:", bound, groupNum+1),
            "Invalid amount of cards selected.",
            1, bound
        );
    }

    // report what was the last move the AI played
    void reportAIMove(Move move, int totalEvaluations, int numberOfIntermediateResults) {
        inform( String.format("The computer removed %d cards from group %d.", move.getAmount(), move.getGroup()+1) );
        inform( 
            String.format(
                "It has performed %d evaluations of the game positions \n  and it has stored %d intermediate results thus far.", 
                totalEvaluations, numberOfIntermediateResults
            )
        );
    }

    // get a valid move from the user
    Move getMove(Board board, int[] bounds) {
        int group;
        do group = askGroup(board.size()); while (board.getCardsFromGroup(group) == 0);
        int cards = askCards(group, Math.min(board.getCardsFromGroup(group), bounds[group]));
        
        return new Move(group, cards);
    }   
}
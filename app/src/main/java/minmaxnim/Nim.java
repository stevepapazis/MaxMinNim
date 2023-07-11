package minmaxnim;

import java.util.Random;


// the class that models the game
class Nim {
    Board board; // an object representing the state of the game
    int[] bounds; // how many cards can be removed from each group?
    
    int turn = 1; // how many turns have passed since the beginning of the game
    
    boolean playerToPlayNext; // who is playing next?

    boolean MAX = true; // == AI == MAX 
    boolean MIN = !MAX; // == opponent == MIN

    Player human = new Player(); // the human player
    AiPlayer ai = new AiPlayer(); // the AI player
    
    // generates a new game with user provided information
    Nim() {
        human.explainGame();
        
        int totalCards = human.askNumberOfTotalCards();
        int numberOfGroups = human.askNumberOfGroups(totalCards);
        
        board = new Board(numberOfGroups);
        bounds = new int[numberOfGroups];
        
        pickPlayerToPlayRandomly();

        boolean setupBoardManually = human.askToSetupBoardManually();
        
        if ( setupBoardManually ) 
            generateBoardManually(totalCards);
        else
            generateBoardAutomatically(totalCards);
    }

    // picks randomly the first player to play
    void pickPlayerToPlayRandomly() {
        Random random = new Random();
        playerToPlayNext = random.nextBoolean(); 
    }
    
    // creates a random board with random bounds
    void generateBoardAutomatically(int totalCards) { 
        board.fillRandomly(totalCards);
        Random random = new Random();
        for (int i = 0; i < board.size(); i++) 
            bounds[i] = 1 + random.nextInt(board.getCardsFromGroup(i));
        human.inform("The game board was generated automatically.");
    }
    
    // creates a new board from user input
    void generateBoardManually(int totalCards) {
        int cardsRemainingToDeal = totalCards - 2*board.size();
        int numberOfGroups = board.size();
        board.emptyGroups = 0;
        
        for (int i = 0; i < numberOfGroups-1; i++ ) {
            human.addEmptyLine();
            board.print(bounds);
            int cardsDealt = human.askNumberOfCardsInGroup(i, cardsRemainingToDeal+2);
            board.setCardsInGroup(i, cardsDealt);
            cardsRemainingToDeal -= cardsDealt-2;
            bounds[i] = human.askBoundForGroup( i, board.getCardsFromGroup(i) );
        }

        board.setCardsInGroup(numberOfGroups-1, cardsRemainingToDeal+2);
        human.addEmptyLine();
        board.print(bounds);
        bounds[numberOfGroups-1] = human.askBoundForGroup( numberOfGroups-1, board.getCardsFromGroup(numberOfGroups-1) );

    }

    // execute the game loop
    void play() {
        while (!isFinished()) {
            human.addEmptyLine();
            human.inform("Turn " + turn);
            board.print(bounds);

            if (playerToPlayNext == MAX) {
                Move move = ai.getMove(board, bounds);
                human.reportAIMove(move, ai.totalEvaluations, ai.numberOfIntermediateResults());
                board.removeCards(move);

            } else {
                human.inform("Your turn");
                Move move = human.getMove(board, bounds);
                board.removeCards(move);                
            }

            playerToPlayNext = !playerToPlayNext;
            turn += 1;
        }
        
        human.addEmptyLine();
        human.inform( MAX == playerToPlayNext? "Congratulations, you won!" : "Game over! The computer won!" );
    }    

    // is the game finished?
    boolean isFinished() { return board.isEmpty(); }
}
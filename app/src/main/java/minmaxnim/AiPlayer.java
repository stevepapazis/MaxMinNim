package minmaxnim;

import java.util.TreeMap;


// an AI agent that uses the minmax algorithm 
class AiPlayer {
    private TreeMap<NormalizedBoard, Boolean> boardsWithKnownEvaluations = new TreeMap<>(); // we store intermediate values to speed up the evaluation of the minmax recursion 
    final private boolean WIN = true; // the game is won
    final private boolean LOSS = !WIN; // the game is lost 

    int totalEvaluations = 0; // an approximation of the total number of times there was a request to evaluate the board
    
    // how many intermediate results have been computed thus far?
    int numberOfIntermediateResults() { return boardsWithKnownEvaluations.size(); }

    // evaluates the board position
    // WIN := true == sure win from that position with optimal strategy
    // LOSS :== false == sure loss if the opponent employs the optimal strategy
    boolean evaluatePosition(Board board, int[] bounds) {
    	totalEvaluations += 1;
    	return evaluatePosition( new NormalizedBoard(board, bounds) );
    }
    
    // evaluates the normalized board position
    // WIN := true == sure win from that position with optimal strategy
    // LOSS :== false == sure loss if the opponent employs the optimal strategy
    boolean evaluatePosition(NormalizedBoard board) {
        totalEvaluations += 1;

        if ( board.areAllCardsInOneGroup() ) { return WIN; }
    
        if ( board.isEmpty() ) { return LOSS; }

        if ( boardsWithKnownEvaluations.containsKey(board) ) 
            return boardsWithKnownEvaluations.get(board);
                
        for (int i = board.emptyGroups; i < board.size(); i++)
            for (int cards = board.getCardsFromGroup(i); cards >= 1; cards--) {

            	Move move = new Move(i, cards);
                board.removeCards(move);
                boolean opponentResult = evaluatePosition(new NormalizedBoard(board));
                board.undo(move);

                if ( opponentResult == LOSS ) {
                    boardsWithKnownEvaluations.put(board, WIN);
                    return WIN;
                }
            }

        boardsWithKnownEvaluations.put(board, LOSS);
        return LOSS;
    }

    // uses the minimax algorithm to find the best move
    Move getMove(Board board, int[] bounds) {

        for (int i = 0; i < board.size(); i++) {
            
            int availableCards = Math.min(board.getCardsFromGroup(i), bounds[i]);
            
            for (int cards = availableCards; cards >= 1; cards--) {

                Move move = new Move(i, cards);
                board.removeCards(move);
                boolean opponentResult = evaluatePosition(board, bounds);
                board.undo(move);

                if (opponentResult == LOSS) return move;
            }

        }
        
        return board.getRandomMove(bounds);
    }  
}
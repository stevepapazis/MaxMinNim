package minmaxnim;

import java.util.Arrays;


// a class modeling the state of the board, normalizing it so that 
// the groups are reduced modulo certain bounds and they're also sorted
class NormalizedBoard extends Board {
    NormalizedBoard(NormalizedBoard board) { 
        super(board); 
        Arrays.sort(groups);
    }

    NormalizedBoard(Board board, int[] bounds) {
    	super(board);

        emptyGroups = 0;
    	
        for (int i = 0; i < board.size(); i++) {
            groups[i] = board.getCardsFromGroup(i) % (bounds[i] + 1);
            if (groups[i] == 0) emptyGroups += 1;
        }

        Arrays.sort(groups);
    }
}
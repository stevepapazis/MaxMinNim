package minmaxnim;

import java.util.Arrays;
import java.util.Random;


// a class modeling the state of the board
class Board implements Comparable<Board> {
    int[] groups; // how many cards are there in each group of the board?
    int emptyGroups; // how many groups have no cards left?

    // empty constructor
    Board(int size) {
        groups = new int[size];
        emptyGroups = size;
    }

    // deep copy constructor
    Board(int[] groups, int emptyGroups) {
        this.groups = Arrays.copyOf(groups, groups.length);
        this.emptyGroups = emptyGroups;
    }

    // deep copy constructor
    Board(Board board) { this(board.groups, board.emptyGroups); }

    // how many groups does the board contain?
    int size() { return groups.length; }

    // how many empty groups are there on the board?
    int getEmptyGroups() { return emptyGroups; }
    
    // gets the number of cards in a group
    int getCardsFromGroup(int groupNum) { return groups[groupNum]; }

    // sets the number of cards in a group
    void setCardsInGroup(int groupNum, int cards) { groups[groupNum] = cards; }
    
    // defines an ordering 
    @Override public int compareTo(Board otherBoard) { return Arrays.compare(groups, otherBoard.groups); }
    
    // are there any cards left on the board?
    boolean isEmpty() { return getEmptyGroups() == size(); }

    // are all the cards in one group?
    boolean areAllCardsInOneGroup() { return getEmptyGroups() == size() - 1; }

    // removes cards from a group; it doesn't check the validity of the move
    void removeCards(Move move) {
        int group = move.getGroup();
        int amount = move.getAmount();
        groups[group] -= amount; 
        if (getCardsFromGroup(group) == 0) emptyGroups += 1;
    }
    
    // undoes the removal of cards from a group; it doesn't check the validity of the move
    void undo(Move move) {
        int group = move.getGroup();
        int amount = move.getAmount();
        if (getCardsFromGroup(group) == 0) emptyGroups -= 1;
        groups[group] += amount; 
    }

    // get a random move
    Move getRandomMove(int[] bounds) {
        Random random = new Random();
        int group, amount;
        do group = random.nextInt(size()); while ( getCardsFromGroup(group) == 0 );
        amount = 1 + random.nextInt( Math.min(getCardsFromGroup(group), bounds[group]) );
        return new Move(group, amount);
    }

    // distributes randomly a number of cards to the groups available on the board
    void fillRandomly(int totalCards) {
        Random random = new Random();
        totalCards -= 2*size();
        Arrays.fill(groups, 0, size(), 2); 
        for (int i = 0; i < totalCards; i++) {
            int group = random.nextInt(size());
            groups[group] += 1;
        }
        emptyGroups = 0;
    }
    
    // computes the XOR sum of the number of cards in each group of the normalized board
    int xor(int[] bounds) {
        int xor = getCardsFromGroup(0) % (bounds[0] + 1);
        for (int i = 1; i < size(); i++) xor ^= getCardsFromGroup(i) % (bounds[i] + 1);
        return xor;
    }

    // print the state of the board
    void print(int[] bounds) {
        System.out.printf("- Board:  %d", getCardsFromGroup(0));
        for (int i = 1; i < size(); i++) System.out.printf(",%d", getCardsFromGroup(i));
        System.out.printf("\n- Bounds: %d", bounds[0]);
        for (int i = 1; i < size(); i++) System.out.printf(",%d", bounds[i]);
        System.out.printf("\n (The xor sum of the columns modulo the bounds plus one is %d.)\n\n", xor(bounds));
    }   
}
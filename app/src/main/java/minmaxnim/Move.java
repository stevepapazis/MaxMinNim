package minmaxnim;


// a class modeling moves of the game
class Move {
    private int group; // which group the cards get removed from
    private int amount; // how many cards get removed

    Move(int group, int amount) {
        setGroup(group);
        setAmount(amount);
    }

    int getGroup() { return group; }
    void setGroup(int group) { this.group = group; }
    int getAmount() { return amount; }
    void setAmount(int amount) { this.amount = amount; }
}
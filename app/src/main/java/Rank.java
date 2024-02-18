/*
changed by: Thur 14:15 workshop team 12
Members:
William Grimsey - 1270044
Even Guo - 1307884
Sophie Su - 1273360
 */


// class copied from original version in CountingUpGame.java
// along with some methods
public enum Rank {
    // Reverse order of rank importance (see rankGreater() below)
    ACE (1, 10), KING (13, 10), QUEEN (12, 10),
    JACK (11, 10), TEN (10, 10), NINE (9, 9),
    EIGHT (8, 8), SEVEN (7, 7), SIX (6, 6),
    FIVE (5, 5), FOUR (4, 4), THREE (3, 3),
    TWO (2, 2);

    private int rankCardValue = 1;
    private int scoreCardValue = 1;
    Rank(int rankCardValue, int scoreCardValue) {
        this.rankCardValue = rankCardValue;
        this.scoreCardValue = scoreCardValue;
    }

    public int getRankCardValue() {
        return rankCardValue;
    }

    public int getScoreCardValue() { return scoreCardValue; }

    public String getRankCardLog() {
        return String.format("%d", rankCardValue);
    }
}
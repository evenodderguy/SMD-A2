/*
changed by: Thur 14:15 workshop team 12
Members:
William Grimsey - 1270044
Even Guo - 1307884
Sophie Su - 1273360
 */


// class copied from original version in CountingUpGame.java
public enum Suit {
    SPADES ("S"), HEARTS ("H"), DIAMONDS ("D"), CLUBS ("C");
    private String suitShortHand = "";
    Suit(String shortHand) {
        this.suitShortHand = shortHand;
    }

    public String getSuitShortHand() {
        return suitShortHand;
    }
}
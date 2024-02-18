/*
changed by: Thur 14:15 workshop team 12
Members:
William Grimsey - 1270044
Even Guo - 1307884
Sophie Su - 1273360
 */

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player{
    public void setAutoMovement(List<String> autoMovement) {
        this.autoMovement = autoMovement;
    }

    private List<String> autoMovement;
    private int score = 0;
    private TextActor scoreActor;
    protected int playerNumber;

    private final Hand hand;

    private final Location handLocation;
    private final Location scoreLocation;

    private final CountingUpGame game;
    private IPlayingStrategy strategy = null;


    /**
     * play specific card
     * Do not check for validation
     * if checkAuto then the autoplay list will have higher priority
     * @param card card to play
     * @param checkAuto check if auto movement is enabled
     * @return card to play
     */
    public Card playSpecificCard(String card,boolean checkAuto){
        // not doing validation check on Automove
        game.setStatusText("Player " + playerNumber + " thinking...");
        GameGrid.delay(game.getThinkingTime());
        if (checkAuto && Objects.equals(card, autoMovement.get(0))){
            autoMovement.remove(0);
        }

        return getCardFromList(hand.getCardList(), card);
    }

    /**
     * return card to play according to strategy
     * @param cardsPlayed cards played in this round
     * @param isAuto is auto movement enabled
     * @return card to play (null for pass)
     */
    public Card takeTurn(ArrayList<Card> cardsPlayed, boolean isAuto){
        if (!isAuto || autoMovement.isEmpty()) return takeTurn(cardsPlayed);
        // not doing validation check on Automove
        // pop off first item
        String nextMovement = autoMovement.get(0);
//        System.out.println("player:"+playerNumber+" "+ autoMovement);
        autoMovement.remove(0);
        if (nextMovement.equals("SKIP")) {
            game.setStatusText("Player " + playerNumber + " skipping...");
            GameGrid.delay(game.getThinkingTime());
            return null;
        } else {
            game.setStatusText("Player " + playerNumber + " thinking...");
            GameGrid.delay(game.getThinkingTime());
            return getCardFromList(hand.getCardList(), nextMovement);
        }
    }

    /**
     * return card to play according to strategy
     * @param cardsPlayed cards played in this round
     * @return card to play (null for pass)
     */
    public Card takeTurn(ArrayList<Card> cardsPlayed){
        if (strategy == null){
            System.out.println(" Player strategy Not set. Always pass");
            GameGrid.delay(game.getDelayTime());
            return null;
        }
        return strategy.takeTurn(cardsPlayed,hand,playerNumber);
    }

    public Player(int playerNumber, CountingUpGame game) {
        this.playerNumber = playerNumber;
        this.handLocation = game.handLocations[playerNumber];
        this.scoreLocation = game.scoreLocations[playerNumber];
        String text = "[" + String.valueOf(score) + "]";

        this.game = game;
        scoreActor = new TextActor(text, Color.WHITE, game.bgColor, game.bigFont);
//        System.out.println("player:"+playerNumber+"game\n");
        hand = new Hand(game.getDeck());
        game.addActor(scoreActor, scoreLocation);
    }


    public void initPlayer(){
        hand.sort(Hand.SortType.SUITPRIORITY, false);
        RowLayout layout = new RowLayout(handLocation, game.getHandWidth());
        layout.setRotationAngle(90 * playerNumber);
        hand.setView(game, layout);
        hand.setTargetArea(new TargetArea(game.getTrickLocation()));
        hand.draw(); // draw the hand in GUI, not draw a card
    }

    /**
     * set strategy
     * @param strategy strategy to set
     */
    public void setStrategy(IPlayingStrategy strategy){
        this.strategy = strategy;
    }

    /**
     * insert card to hand
     * @param card card to insert
     */
    public void insertToHand(Card card){
        hand.insert(card, false);
    }

    /**
     *
     * @return the number of cards at hand
     */
    public int getNumberOfCards(){
        return hand.getNumberOfCards();
    }

    public Hand getHand() {
        return hand;
    }

    /**
     *
     * @return if player have Ace of Clubs
     */
    public boolean haveAceOfClubs(){
        List<Card> cards = hand.getCardsWithRank(Rank.ACE);
        if (cards.isEmpty()){
            return false;
        }
        for (Card card:cards){
            if (card.getSuit() == Suit.CLUBS){
                return true;
            }
        }
        return false;
    }

    /**
     * update player's score
     * @param score score to increment
     */
    public void updateScore(int score){
        this.score += score;
        updateScore();
    }

    /**
     * refresh score actor (text actor)
     */
    public void updateScore(){
        game.removeActor(scoreActor);
        String text = "P" + playerNumber + "[" + score + "]";
        scoreActor = new TextActor(text, Color.WHITE, game.getBgColor(), game.bigFont);
        game.addActor(scoreActor, scoreLocation);
    }

    public CountingUpGame getGame() {
        return game;
    }
//   protected ArrayList<Card> selectValidCard(ArrayList<Card> cardsPlayed){
//        if (cardsPlayed.isEmpty()) return hand.getCardList();
//        Card lastCardPlayed = cardsPlayed.get(cardsPlayed.size()-1);
//        ArrayList<Card> result = new ArrayList<Card>();
//        for (Card card: hand.getCardList()){
//            System.out.print(lastCardPlayed.toString());
//            System.out.println(lastCardPlayed.getRankId());
//            if((Rank.compareCard(card.getRankId(),lastCardPlayed.getRankId()) && card.getSuitId() == lastCardPlayed.getSuitId())||
//                card.getRankId() == lastCardPlayed.getRankId()){
////            if(card.getRankId() <= lastCardPlayed.getRankId()){
//                result.add(card);
//            }
//        }
//        return result;
//    }
    private Card getCardFromList(List<Card> cards, String cardName) {
        Rank cardRank = getRankFromString(cardName);
        Suit cardSuit = getSuitFromString(cardName);
        for (Card card: cards) {
            if (card.getSuit() == cardSuit
                    && card.getRank() == cardRank) {
                return card;
            }
        }
        return null;
    }
    private Rank getRankFromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        Integer rankValue = Integer.parseInt(rankString);

        for (Rank rank : Rank.values()) {
            if (rank.getRankCardValue() == rankValue) {
                return rank;
            }
        }

        return Rank.ACE;
    }

    private Suit getSuitFromString(String cardName) {
        String suitString = cardName.substring(cardName.length() - 1);

        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equals(suitString)) {
                return suit;
            }
        }
        return Suit.CLUBS;
    }

    public int getScore() {
        return score;
    }


    public boolean emptyHand(){
        return hand.isEmpty();
    }

    public int handValue(){
        int totalScorePlayed = 0;
        for (Card card: hand.getCardList()) {
            Rank rank = (Rank) card.getRank();
            totalScorePlayed += rank.getScoreCardValue();
        }
        return totalScorePlayed;
    }
}

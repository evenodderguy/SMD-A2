/*
changed by: Thur 14:15 workshop team 12
Members:
William Grimsey - 1270044
Even Guo - 1307884
Sophie Su - 1273360
 */

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

import java.util.ArrayList;
import java.util.List;

public class CleverStrategy implements IPlayingStrategy{
    private final CountingUpGame game;
    private int highCardCount = 0;
    @Override
    public Card takeTurn(List<Card> cardsPlayed, Hand playerHand, int playerNumber) {
        game. setStatusText("Player " + playerNumber + " thinking...");
        GameGrid.delay(game.getThinkingTime());
        ArrayList<Card> validCards = selectValidCard(cardsPlayed,playerHand);
        //System.out.printf(String.valueOf(validCards));
        // pass round
        if (validCards.isEmpty()){
            game.setStatusText("Player " + playerNumber + " skipping...");
            GameGrid.delay(game.getThinkingTime());
            return null;
        }

        List<Card> allCard = game.getAllCardsPlayed();


        Card lowestCard = validCards.get(0);
        for (Card card:validCards){
            if (((Rank) card.getRank()).getRankCardValue()< ((Rank) lowestCard.getRank()).getRankCardValue()){
                lowestCard = card;
            }
        }

        Card maxCard = validCards.get(0);
        for (Card card:validCards){
            if (((Rank) card.getRank()).getRankCardValue()< ((Rank) maxCard.getRank()).getRankCardValue()){
                maxCard = card;
            }
        }

        if(cardsPlayed.isEmpty()){
            return lowestCard;
        }

        boolean cond1 = haveHighestCard(allCard, playerHand);
        boolean cond2 = haveLowerCard(validCards, playerHand);
        //System.out.println("cond1: "+cond1+",  cond2:"+cond2);
        if(cond1 && cond2){
            return lowestCard;
        } else if (cond1 && !cond2) {
            return maxCard;
        }else if(!cond1 && cond2){
            return secondHighestCard(validCards,maxCard);
        }
        return null;

    }
    @Override
    public String toString(){
        return "Clever Player strategy";
    }


    public CleverStrategy(Player player) {
        game = player.getGame();
    }


    /**
     * return if player can play a lower card than the highest card on hand
     * @param validCard list of valid card that can be played
     * @param hand player's hand
     * @return true if player can play a lower card than the highest card on hand
     */
    private boolean haveLowerCard(List<Card> validCard,Hand hand){
        int max = highestCardValue(hand);
        for (Card card:validCard){
            if (((Rank) card.getRank()).getRankCardValue() < max){
                return true;
            }
        }
        return false;
    }

    /**
     * return if player have the all the highest card in the game
     * @param all all cards been played in this game
     * @param hand player's hand
     * @return
     */
    private boolean haveHighestCard(List<Card> all, Hand hand){
        int[] remaining = new int[13];
        for (int i =0; i<13;i++){
            remaining[i]=4;
        }
        for (Card card: all){
            remaining[((Rank) card.getRank()).getRankCardValue()-1] -=1;
        }
        int maxC = highestCardValue(hand);
        for (int i =13;i>maxC-2; i--){
            //
            if ((i>maxC && remaining[i-1]>0)||(i==maxC && remaining[i-1]> highCardCount)){
                return false;
            }
        }
        return true;
    }

    /**
     * return the highest card in the hand
     * @param playerHand player's hand
     * @return the card value
     */
    private int highestCardValue(Hand playerHand){
        int max=1;
        highCardCount = 0;
        for (Card c:playerHand.getCardList()){
            if(max<((Rank) c.getRank()).getRankCardValue()){
                highCardCount=1;
                max = ((Rank) c.getRank()).getRankCardValue();
            }else if(max == ((Rank) c.getRank()).getRankCardValue()){
                highCardCount++;
            }
        }
        //System.out.println("max: " + max);
        return max;
    }


    /**
     * return the second-highest card in the hand
     * @param hand player's hand
     * @param maxCard the highest card
     * @return the second-highest card
     */
    private Card secondHighestCard(List<Card> hand, Card maxCard){
        Card sec=  hand.get(0);
        int val = -1;
        //System.out.println(hand);
        int max = ((Rank) maxCard.getRank()).getRankCardValue();
        for(Card card:hand){
            if (
                    (((Rank) card.getRank()).getRankCardValue() >= val) &&
                    (((Rank) card.getRank()).getRankCardValue() < max)
            ){
                sec = card;
                val = ((Rank) card.getRank()).getRankCardValue();
            }
        }
        if(val == -1){return null;}

//        System.out.println("secondHighest:"+sec);
        return sec;
    }

}

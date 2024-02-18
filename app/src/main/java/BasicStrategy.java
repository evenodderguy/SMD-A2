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

public class BasicStrategy implements IPlayingStrategy{
    private final CountingUpGame game;


    @Override
    public Card takeTurn(List<Card> cardsPlayed, Hand playerHand, int playerNumber) {
        game. setStatusText("Player " + playerNumber + " thinking...");
        GameGrid.delay(game.getThinkingTime());
        ArrayList<Card> validCards = selectValidCard(cardsPlayed,playerHand);
        //System.out.printf(String.valueOf(validCards));
        if (validCards.isEmpty()){
            game.setStatusText("Player " + playerNumber + " skipping...");
            GameGrid.delay(game.getThinkingTime());
            return null;
        }


        Card selected = validCards.get(0);
        for (Card card:validCards){
            if (((Rank) card.getRank()).getRankCardValue()< ((Rank) selected.getRank()).getRankCardValue()){
                selected = card;
            }
        }
        return selected;
    }
    @Override
    public String toString(){
        return "Basic Player strategy";
    }


    /**
     * Constructor
     * @param player the player to use this strategy
     */
    public BasicStrategy(Player player) {
        game = player.getGame();
    }
}

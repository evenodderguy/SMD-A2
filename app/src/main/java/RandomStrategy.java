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

public class RandomStrategy implements IPlayingStrategy{
    private final CountingUpGame game;
    @Override
    public Card takeTurn(List<Card> cardsPlayed, Hand playerHand, int playerNumber) {
        game. setStatusText("Player " + playerNumber + " thinking...");
        GameGrid.delay(game.getThinkingTime());
        ArrayList<Card> validCards = selectValidCard(cardsPlayed,playerHand);
        if (validCards.isEmpty()) return null;
        Card selected = randomCard(validCards);
        if (selected == null) {
            game.setStatusText("Player " + playerNumber + " skipping...");
            GameGrid.delay(game.getThinkingTime());
        }
        return selected;
    }
    @Override
    public String toString(){
        return "Random Player strategy";
    }


    public RandomStrategy(Player player) {
        game = player.getGame();
    }

    /**
     * return random Card from ArrayList
     */
    public static Card randomCard(ArrayList<Card> list) {
        int x = CountingUpGame.random.nextInt(list.size());
        return list.get(x);
    }
}

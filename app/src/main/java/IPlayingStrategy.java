/*
changed by: Thur 14:15 workshop team 12
Members:
William Grimsey - 1270044
Even Guo - 1307884
Sophie Su - 1273360
 */

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.List;


public interface IPlayingStrategy {

    /**
     * Select valid cards from hand
     * @param cardsPlayed cards played in this round
     * @param playerHand player's hand
     * @param playerNumber player's number [0,1,2,3]
     * @return card to play
     */
    Card takeTurn(List<Card> cardsPlayed, Hand playerHand, int playerNumber);

    /**
     * Select valid cards from hand
     * @param cardsPlayed cards played in this round
     * @param hand player's hand
     * @return list of valid cards
     */
    default ArrayList<Card> selectValidCard(List<Card> cardsPlayed, Hand hand){
        if (cardsPlayed.isEmpty()) return hand.getCardList();
        Card lastCardPlayed = cardsPlayed.get(cardsPlayed.size()-1);
        ArrayList<Card> result = new ArrayList<Card>();
        for (Card card: hand.getCardList()){
            if((((Rank) card.getRank()).getRankCardValue() > ((Rank) lastCardPlayed.getRank()).getRankCardValue() &&
                    card.getSuitId() == lastCardPlayed.getSuitId())||
                    card.getRankId() == lastCardPlayed.getRankId()){
                result.add(card);
            }
        }
        return result;
    }
}

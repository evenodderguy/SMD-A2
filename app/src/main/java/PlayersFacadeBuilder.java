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
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PlayersFacadeBuilder {

    // Singleton
    private static final PlayersFacadeBuilder instance = new PlayersFacadeBuilder();

    public static PlayersFacadeBuilder getInstance(){ return instance;}

    /**
     * build players facade from property file(object
     * @param p property file
     * @param game game object
     * @param nbCardsPerPlayer number of cards per player
     * @return players facade
     */
    public PlayersFacade buildPlayerFacade(Properties p, CountingUpGame game, int nbCardsPerPlayer){
        // build players and strategies
        PlayersFacade playersFacade = new PlayersFacade(game);

        String type;
        int i= playersFacade.length();

        // read player types from Property file and add to playersFacade
        while((type = p.getProperty("players."+i)) !=null) {
            Player player;
            // build player and strategy
            player = new Player(i,game);
            IPlayingStrategy s =  PlayingStrategyFactory.getInstance().getStrategy(type,player);
            player.setStrategy(s);

            // setup Auto Move
            String playerAutoMovement = p.getProperty("players."+i+".cardsPlayed");
            player.setAutoMovement(new ArrayList<>(Arrays.asList(playerAutoMovement.split(","))));



            playersFacade.addPlayer(player);
            i= playersFacade.length();
        }

        // dealing Out
        Hand pack = game.getDeck().toHand(false);
        int[] cardsDealtPerPlayer = new int[playersFacade.length()];
        for (Player player: playersFacade.getPlayers()) {
            String initialCardsKey = "players." + player.playerNumber + ".initialcards";
            String initialCardsValue = p.getProperty(initialCardsKey);
            //System.out.println(initialCardsValue);
            if (initialCardsValue == null) {
                continue;
            }

            String[] initialCards = initialCardsValue.split(",");
            for (String initialCard: initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = getCardFromList(pack.getCardList(), initialCard);
                if (card != null) {
                    card.removeFromHand(false);
//                    players[i].hand.insert(card, false);
                    player.insertToHand(card);
                }
            }
        }

        for (Player player : playersFacade.getPlayers()) {
            int cardsToDealt = nbCardsPerPlayer - player.getNumberOfCards();
            for (int j = 0; j < cardsToDealt; j++) {
                if (pack.isEmpty()) break;
                Card dealt = randomCard(pack.getCardList());
                dealt.removeFromHand(false);
//                players[i].hand.insert(dealt, false);
                player.insertToHand(dealt);
            }
        }

        playersFacade.initAllPlayer();
        return playersFacade;
    }


    /**
     * select a randomCard from list
     * @param list Card list
     * @return Card
     */
    private Card randomCard(ArrayList<Card> list) {
        int x = CountingUpGame.random.nextInt(list.size());
        return list.get(x);
    }

    /**
     * get specific card from card list
     * @param cards card list
     * @param cardName the card
     * @return Card
     */
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
        int rankValue = Integer.parseInt(rankString);

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
}

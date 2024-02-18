/*
changed by: Thur 14:15 workshop team 12
Members:
William Grimsey - 1270044
Even Guo - 1307884
Sophie Su - 1273360
 */
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

import java.util.List;

public class HumanStrategy implements IPlayingStrategy{
    InputListener inputListener;
    private Card selected;
    private boolean passSelected = false;
    private boolean initialized = false;
    private CountingUpGame game;
    private final int playerNumber;

    public HumanStrategy(Player player) {
        Hand hand = player.getHand();
        this.game = player.getGame();
        this.playerNumber = player.playerNumber;
        inputListener = new InputListener(this);
        game.addKeyListener(inputListener);
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) {
                selected = card;
                hand.setTouchEnabled(false);
            }
        };
        hand.addCardListener(cardListener);
        initialized = true;

    }

    public void listenKeyPress(){
        passSelected = true;
    }

    @Override
    public Card takeTurn(List<Card> cardsPlayed, Hand playerHand, int playerNumber){
        if (!initialized){
            //System.out.println("Human Player Hand not initialized. Please call initHandListener");
            assert initialized;
            return null;

        }

        playerHand.setTouchEnabled(true);
        passSelected = false;
        selected = null;
        List<Card> allowedCards = selectValidCard(cardsPlayed,playerHand);
        game.setStatus("Player "+playerNumber+" double-click on card to follow or press Enter to pass");
        while (!passSelected){ //null == selected && !passSelected && false
            if (selected != null){
                if(!allowedCards.contains(selected)) {
                    // selected invalid card
                    selected = null;
                    game.setStatus("Player "+playerNumber+" selected card is invalid. Please double-click on card to follow or press Enter to pass");

                    playerHand.setTouchEnabled(true);

                }else{
                    break;
                }
            }
            GameGrid.delay(game.getDelayTime());
        }
        return selected;
    }

    @Override
    public String toString(){
        return "Human Player strategy";
    }

}

/*
changed by: Thur 14:15 workshop team 12
Members:
William Grimsey - 1270044
Even Guo - 1307884
Sophie Su - 1273360
 */

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.List;

public class PlayersFacade {

    private final CountingUpGame game;
    private final ArrayList<Player> players = new ArrayList<>();
    private int curPlayer = 0;
    private boolean playedAceClub = false;


    public PlayersFacade(CountingUpGame game){
        this.game = game;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public int length(){
        return players.size();
    }

    public List<Player> getPlayers(){
        return players;
    }


    /**
     * update all players' score
     * not changing value
     * just redisplay text actors
     */
    public void updateAllScores(){
        for(Player p:players){
            p.updateScore();
        }
    }

    /**
     * get players with max score
     * @return player list
     */
    public List<Integer> maxScorePlayers(){
        int maxScore = 0;
        for (Player p:players) if (p.getScore() > maxScore) maxScore = p.getScore();
        List<Integer> winners = new ArrayList<Integer>();
        for (Player p:players) if (p.getScore() == maxScore) winners.add(p.playerNumber);

        return winners;
    }

    /**
     * get player with ace of club
     * @return player index
     */
    public int playerIndexWithAceClub() {
        for(Player player:players){
            if (player.haveAceOfClubs()){
                return player.playerNumber;
            }
        }
        return 0;
    }

    /**
     * this is a replacement of takeTurn in the first round first turn
     * force player with AceOfClub to play
     * this will change current player index
     *
     * @return aceOfClub if existed, null otherwise
     */
    public Card forcePlayAceClub(){
        if(!playedAceClub) {
            curPlayer = playerIndexWithAceClub();
            return players.get(curPlayer).playSpecificCard("01C",true);
        }
        return null;
    }

    /**
     * hand over to next player
     */
    public void nextPlayer(){
        curPlayer++;
        if (curPlayer == players.size()) curPlayer=0;
    }

    /**
     * let current player take turn
     * @param cardsPlayed cards played in this round
     * @param isAuto is auto movement enabled
     * @return card to play (null for pass)
     */
    public Card takeTurn(ArrayList<Card> cardsPlayed,boolean isAuto){
        return players.get(curPlayer).takeTurn(cardsPlayed,isAuto);
    }

    /**
     *
     * @return current player number
     */
    public int curPlayerNum(){
        return curPlayer;
    }

    /**
     * add Score to current player
     * @param score int score to add
     */
    public void addScoreCurPlayer(int score){
        players.get(curPlayer).updateScore(score);
    }

    /**
     *
     * @return true if any player have empty hand
     */
    public boolean noEmptyHand(){
        for (Player p:players){
            if (p.emptyHand()) return false;
        }
        return true;
    }

    /**
     * end game score update
     * deduct hand value from each player
     */
    public void endGameScoreUpdate(){
        for(Player p : players){
            p.updateScore(-p.handValue());
        }
    }

    /**
     * return player score as string
     * @return player scores
     */
    public String scoresStr(){
        StringBuilder s = new StringBuilder();
        for(Player p : players){
            s.append(p.getScore()).append(",");
        }
        return s.toString();

    }

    /**
     * initialize all players been added
     */
    public void initAllPlayer(){
        for(Player p : players){
            p.initPlayer();
        }
    }

}

// CountingUpGame.java
/*
changed by: Thur 14:15 workshop team 12
Members:
William Grimsey - 1270044
Even Guo - 1307884
Sophie Su - 1273360
 */

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class CountingUpGame extends CardGame{
    final String[] trumpImage = {"bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif"};

    static public final int seed = 30008;
    static final Random random = new Random(seed);
    private Properties properties;
    private StringBuilder logResult = new StringBuilder();
    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
//    private List<List<String>> playerAutoMovements = new ArrayList<>();


    private final String version = "1.0";
    public final int nbPlayers = 4;
    public final int nbStartCards = 13;
    public final int nbRounds = 3;
    private final int handWidth = 400;
    private final int trickWidth = 40;
    private ArrayList<Card> allCardsPlayed = new ArrayList<>();
//    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
    public final Location[] handLocations = {
        new Location(350, 625),
        new Location(75, 350),
        new Location(350, 75),
        new Location(625, 350)
    };
    public final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            // new Location(650, 575)
            new Location(575, 575)
    };

//    Player[] players;
    PlayersFacade players;
//    private Actor[] scoreActors = {null, null, null, null}; // delete
    private final Location trickLocation = new Location(350, 350);
    private final Location textLocation = new Location(350, 450);

    public int getThinkingTime() {
        return thinkingTime;
    }

    public int getDelayTime() {
        return delayTime;
    }

    private int thinkingTime = 200; //2000
    private int delayTime = 60; //600
//    private Hand[] hands; // delete
    private Location hideLocation = new Location(-500, -500);

    public void setStatus(String string) {
        setStatusText(string);
    }

//    private int[] scores = new int[nbPlayers];

    private boolean isAuto = false;

    public final Font bigFont = new Font("Arial", Font.BOLD, 36);


    // moved to the Constructor in Player
//    private void initScore() {
//        for (int i = 0; i < nbPlayers; i++) {
//            // scores[i] = 0;
//            String text = "[" + String.valueOf(scores[i]) + "]";
//            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
//            addActor(scoreActors[i], scoreLocations[i]);
//        }
//    }

    /**
     * Calculate the score of the cards played in the round
     * and update the score of the player (winner of the round)
     * @param player winner of the round
     * @param cardsPlayed cards played this round
     */
    private void calculateScoreEndOfRound(int player, List<Card> cardsPlayed) {
        int totalScorePlayed = 0;
        for (Card card: cardsPlayed) {
            Rank rank = (Rank) card.getRank();
            totalScorePlayed += rank.getScoreCardValue();
        }
//        scores[player] += totalScorePlayed;
        players.addScoreCurPlayer(totalScorePlayed);
    }

    /**
     * add cards played to log
     * @param player player number that played the card
     * @param selectedCard cards played this round
     */
    private void addCardPlayedToLog(int player, Card selectedCard) {
        if (selectedCard == null) {
            logResult.append("P" + player + "-SKIP,");
        } else {
            Rank cardRank = (Rank) selectedCard.getRank();
            Suit cardSuit = (Suit) selectedCard.getSuit();
            logResult.append("P" + player + "-" + cardRank.getRankCardLog() + cardSuit.getSuitShortHand() + ",");
        }
    }

    /**
     * add (start )Round Info to log
     * @param roundNumber round number
     */
    private void addRoundInfoToLog(int roundNumber) {
        logResult.append("Round" + roundNumber + ":");
    }

    /**
     * add end of round info to log
     *
     */
    private void addEndOfRoundToLog() {
        logResult.append("Score:");
        logResult.append(players.scoresStr());
        logResult.append("\n");
    }

    /**
     * add end of game info to log
     * @param winners list of winners
     */
    private void addEndOfGameToLog(List<Integer> winners) {
        logResult.append("EndGame:");
        logResult.append(players.scoresStr());
        logResult.append("\n");
        logResult.append("Winners:" + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
    }


    /**
     * the main gameplay method
     */
    private void playGame() {
        // End trump suit
        Hand playingArea = null;
        int winner = 0;
        int roundNumber = 1;
        boolean firstTurn = true;
        boolean isContinue = true;
        int skipCount = 0;
        ArrayList<Card>cardsPlayed = new ArrayList<>();
        playingArea = new Hand(deck);

        addRoundInfoToLog(roundNumber);
        players.updateAllScores();

        Card c = players.forcePlayAceClub();
        if (c != null) {
            playingArea.setView(this, new RowLayout(trickLocation, (playingArea.getNumberOfCards() + 2) * trickWidth));
            cardsPlayed.add(c);
            c.setVerso(false);  // In case it is upside down
            // Check: Following card must follow suit if possible
            // Checking is done in Player Class
            // End Check
            players.nextPlayer();
            c.transfer(playingArea, true); // transfer to trick (includes graphic effect)
            delay(delayTime);
        }
        // main gamePlay
        while(isContinue) {
            Card selected = null;

            selected =  players.takeTurn(cardsPlayed,isAuto);
            addCardPlayedToLog(players.curPlayerNum(), selected);
            players.nextPlayer();
            // Follow with selected card
            playingArea.setView(this, new RowLayout(trickLocation, (playingArea.getNumberOfCards() + 2) * trickWidth));
            playingArea.draw();

//            System.out.println(skipCount);
            if (selected != null) {
                skipCount = 0;
                cardsPlayed.add(selected);
                allCardsPlayed.add(selected);
                selected.setVerso(false);  // In case it is upside down
                // Check: Following card must follow suit if possible
                // Checking should be done in Player Class
                // check here again in case of error
                if (!checkValidCard(cardsPlayed, selected)) {
                    selected = null;
                }
                // End Check
                selected.transfer(playingArea, true); // transfer to trick (includes graphic effect)
                delay(delayTime);
                // End Follow
            } else {
                skipCount++;
            }

            // round finish
            if (skipCount == nbPlayers - 1) {
                playingArea.setView(this, new RowLayout(hideLocation, 0));
                playingArea.draw();
                winner = players.curPlayerNum();
                skipCount = 0;
                calculateScoreEndOfRound(winner, cardsPlayed);
//                players[winner].updateScore(scores[winner]);
                addEndOfRoundToLog();
                roundNumber++;
                addRoundInfoToLog(roundNumber);
                cardsPlayed = new ArrayList<>();
                delay(delayTime);
                playingArea = new Hand(deck);
            }

            isContinue = players.noEmptyHand();
            if (!isContinue) {
                winner = players.curPlayerNum();
                calculateScoreEndOfRound(winner, cardsPlayed);
                addEndOfRoundToLog();
            }
            delay(delayTime);
        }
        players.endGameScoreUpdate();
//        for (int i = 0; i < nbPlayers; i++) {
//            calculateNegativeScoreEndOfGame(i, players[i].getHand().getCardList());
//            players[i].updateScore();
//        }
    }

    public String runApp() {
        setTitle("CountingUpGame (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
       // players = PlayerBuilder.initBuilder(properties, this).getPlayers();
        players = PlayersFacadeBuilder.getInstance().buildPlayerFacade(properties,this, nbStartCards);
        playGame();

        players.updateAllScores();
        List<Integer> winners = players.maxScorePlayers();
        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " +
                    winners.iterator().next();
        } else {
            winText = "Game Over. Drawn winners are players: " +
                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList()));
        }
        addActor(new Actor("sprites/gameover.gif"), textLocation);
        setStatusText(winText);
        refresh();
        addEndOfGameToLog(winners);

        return logResult.toString();
    }

    public CountingUpGame(Properties properties) {
        super(700, 700, 30);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto"));
        if (isAuto) {
            thinkingTime = 50;
            delayTime = 10;
        }
    }

    /**
     * check if the card is valid to play
     * @param played list of played Card in the round. empty for first play
     * @param card card to be played
     * @return true if valid
     */
    public boolean checkValidCard(List<Card> played, Card card){
        // any card is valid if no card is played
        if (!played.isEmpty()){
            return true;
        }
        // get last card
        Card last = played.get(played.size()-1);
        return (card.getSuit() == last.getSuit() && ((Rank) card.getRank()).getRankCardValue() > ((Rank) last.getRank()).getRankCardValue()) ||
                card.getRankId() == last.getRankId();
    }

    public int getHandWidth() {
        return handWidth;
    }

    public Location getTrickLocation() {
        return trickLocation;
    }

    public Deck getDeck() {
        return deck;
    }

    public ArrayList<Card> getAllCardsPlayed() {
        return allCardsPlayed;
    }
}

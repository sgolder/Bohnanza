package edu.up.cs301.bohnanza;

import android.util.Log;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Holds information for the current game state for Bohnanza
 *
 * @autor Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class BohnanzaState extends GameState {

    private static final long serialVersionUID = 416201801L;

    private int turn; //0 = player 1, 1 = player 2, etc.
    private int timesThroughDeck = 0;

    private BohnanzaPlayerState[] playerList = new BohnanzaPlayerState[4];

    private Deck mainDeck;
    private Deck discardDeck;
    private Deck tradeDeck;
    //Phase -1: initial planting
    // Phase 0: Begin turn and plant initial beans
    // Phase 1: Turn over two cards and decide to trade or plant
    // Phase 2: Trading
    private int phase;

    /**
     * Constructor for BohnanzaState.
     */
    public BohnanzaState() {
        mainDeck = new Deck();
        mainDeck.addAllCards();
        mainDeck.shuffle();
        discardDeck = new Deck();
        tradeDeck = new Deck();

        turn = 0;
        phase = -1;

        playerList[0] = new BohnanzaPlayerState("Player 1");
        playerList[1] = new BohnanzaPlayerState("Player 2");
        playerList[2] = new BohnanzaPlayerState("Player 3");
        playerList[3] = new BohnanzaPlayerState("Player 4");

        for(int i = 0; i<4; i++) {
            for(int j = 0; j<5; j++) {
                mainDeck.moveTopCardTo(playerList[i].getHand());
            }
        }
    }

    public BohnanzaState(BohnanzaState orig) {
        turn = orig.turn;
        phase = orig.phase;

        for(int i = 0; i<4; i++) {
            playerList[i] = new BohnanzaPlayerState(orig.playerList[i]);
        }

        //main, trade, and discard decks
        mainDeck = new Deck(orig.mainDeck);
        discardDeck = new Deck(orig.discardDeck);
        tradeDeck = new Deck(orig.tradeDeck);

        //hide main deck from user
        mainDeck.turnHandOver();
    }

    /**
     * Deep copy constructor of BohnanzaState
     *
     */
    public BohnanzaState(BohnanzaState orig, int playerId) {
        turn = orig.turn;
        phase = orig.phase;

        for(int i = 0; i<4; i++) {
            playerList[i] = new BohnanzaPlayerState(orig.playerList[i]);
        }

        //main, trade, and discard decks
        mainDeck = new Deck(orig.mainDeck);
        discardDeck = new Deck(orig.discardDeck);
        tradeDeck = new Deck(orig.tradeDeck);
        timesThroughDeck = orig.timesThroughDeck;

        //hide main deck from user
        mainDeck.turnHandOver();

        //hide other players cards from user
        for(int i = 0; i<4; i++) {
            if(i != playerId) {
                playerList[i].getHand().turnHandOver();
            }
        }
    }

    //getter
    public BohnanzaPlayerState[] getPlayerList() { return playerList; }
    public int getTurn(){return turn;}
    public Deck getMainDeck(){return mainDeck;}
    public Deck getTradeDeck(){return tradeDeck;}
    public Deck getDiscardDeck(){return discardDeck;}
    public int getPhase(){return phase;}
    public int getTimesThroughDeck(){return timesThroughDeck;}
    public void setTimesThroughDeck(){timesThroughDeck++;}
    public void setTurn(int newTurn){this.turn = newTurn; }
    public void setPhase(int newPhase){this.phase = newPhase;}


    @Override
    public String toString() {
        //player 1 info
        String player1Info =  "\nPlayer 1 info:\n" +
                "coins: "+playerList[0].getCoins() + "\n" +
                "Has third field: "+playerList[0].getHasThirdField() + "\n";
        for(int i = 0; i<3; i++) {
            if (playerList[0].getField(i).size() == 0) {
                player1Info = player1Info + "Field" + (i+1) + ": Empty\n";
            }
            else {
                player1Info = player1Info + "Field" + (i+1) + ": " +
                        playerList[0].getField(i).toString() + "\n";
            }
        }
        if(playerList[0].getMakeOffer() == 0) {
            player1Info = player1Info + "Player 1 has not decided if they will be trading\n";
        }
        else if (playerList[0].getMakeOffer() == 1) {

            player1Info = player1Info + "Player 1 will not be trading\n";
        }
        else if (playerList[0].getMakeOffer() == 2) {

            player1Info = player1Info + "Player 1 will be trading\n";
        }
        player1Info = player1Info + "Hand: "+playerList[0].getHand().toString()+"\n";

        //player 2 info
        String player2Info =  "\nPlayer 2 info:\n" +
                "coins: "+playerList[1].getCoins() + "\n" +
                "Has third field: "+playerList[1].getHasThirdField() + "\n";
        for(int i = 0; i<3; i++) {
            if (playerList[1].getField(i).size() == 0) {
                player2Info = player2Info + "Field" + (i+1) + ": Empty\n";
            }
            else {
                player2Info = player2Info + "Field" + (i+1) + ": " +
                        playerList[1].getField(i).toString() + "\n";
            }
        }
        if(playerList[1].getMakeOffer() == 0) {
            player2Info = player2Info + "Player 2 has not decided if they will be trading\n";
        }
        else if (playerList[1].getMakeOffer() == 1) {

            player2Info = player2Info + "Player 2 will not be trading\n";
        }
        else if (playerList[1].getMakeOffer() == 2) {

            player2Info = player2Info + "Player 2 will be trading\n";
        }
        player2Info = player2Info + "Hand: "+playerList[1].getHand().toString()+"\n";

        //player 3 info
        String player3Info =  "\nPlayer 3 info:\n" +
                "coins: "+playerList[2].getCoins() + "\n" +
                "Has third field: "+playerList[2].getHasThirdField() + "\n";
        for(int i = 0; i<3; i++) {
            if (playerList[2].getField(i).size() == 0) {
                player3Info = player3Info + "Field" + (i+1) + ": Empty\n";
            }
            else {
                player3Info = player3Info + "Field" + (i+1) + ": " +
                        playerList[2].getField(i).toString() + "\n";
            }
        }
        if(playerList[2].getMakeOffer() == 0) {
            player3Info = player3Info + "Player 3 has not decided if they will be trading\n";
        }
        else if (playerList[2].getMakeOffer() == 1) {

            player3Info = player3Info + "Player 3 will not be trading\n";
        }
        else if (playerList[2].getMakeOffer() == 2) {

            player3Info = player3Info + "Player 3 will be trading\n";
        }
        player3Info = player3Info + "Hand: "+playerList[2].getHand().toString()+"\n";

        //player 4 info
        String player4Info =  "\nPlayer 4 info:\n" +
                "coins: "+playerList[3].getCoins() + "\n" +
                "Has third field: "+playerList[3].getHasThirdField() + "\n";
        for(int i = 0; i<3; i++) {
            if (playerList[3].getField(i).size() == 0) {
                player4Info = player4Info + "Field" + (i+1) + ": Empty\n";
            }
            else {
                player4Info = player4Info + "Field" + (i+1) + ": " +
                        playerList[3].getField(i).toString() + "\n";
            }
        }
        if(playerList[3].getMakeOffer() == 0) {
            player4Info = player4Info + "Player 4 has not decided if they will be trading\n";
        }
        else if (playerList[3].getMakeOffer() == 1) {

            player4Info = player4Info + "Player 4 will not be trading\n";
        }
        else if (playerList[3].getMakeOffer() == 2) {

            player4Info = player4Info + "Player 4 will be trading\n";
        }
        player4Info = player4Info + "Hand: "+playerList[3].getHand().toString()+"\n";


        //info about decks
        String deckInfo = "\nTrade deck: "+tradeDeck.toString()+"\n" +
                "Main deck: "+ mainDeck.size() +" cards remaining\n" +
                "Discard deck: "+ discardDeck.size()+" cards\n";

        //info about state of game
        String stateInfo = "\nPhase: "+phase+"\n"+
                "Player"+(turn+1)+"'s turn\n";

        return player1Info+player2Info+player3Info+player4Info+deckInfo+stateInfo;
    }
}


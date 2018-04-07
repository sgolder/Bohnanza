package edu.up.cs301.bohnanza;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Holds information for the current game state for Bohnanza
 *
 * @autor Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class BohnanzaState extends GameState {
    private int turn; //0 = player 1, 1 = player 2, etc.

    private BohnanzaPlayerState[] playerList = new BohnanzaPlayerState[4];

    private Deck mainDeck;
    private Deck discardDeck;
    private Deck tradeDeck;

    // Phase 0: Begin turn and plant initial beans
    // Phase 1: Turn over two cards and decide to trade or plant
    // Phase 2: Trading
    private int phase;

    public BohnanzaState() {
        mainDeck = new Deck();
        mainDeck.addAllCards();
        discardDeck = new Deck();
        tradeDeck = new Deck();

        turn = 0;
        phase = 0;

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

        /*//hide main deck from user
        mainDeck.turnHandOver();

        //hide other players cards from user
        for(int i = 0; i<4; i++) {
            if(i != playerId) {
                playerList[i].getHand().turnHandOver();
            }
        }*/
    }
    /**
     * Replaces all cards with null, except for the top card of deck 2
     */
   /* public void hideDecks() {
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
    public int getPhase(){return phase;}
    public Deck getTradeDeck(){return tradeDeck;}
    public int getTurn(){return turn;}
    public Deck getMainDeck(){return mainDeck;}
    public Deck getTradeDeck(){return tradeDeck;}
    public int getPhase(){return phase;}
    public void setTurn(int newTurn){this.turn = newTurn; }
    public void setPhase(int newPhase){this.phase = newPhase;}

    /**
     * Buy new Bean field action
     *
     */
    public boolean buyThirdField(int playerId){
        if( playerList[playerId].getHasThirdField() ){
            return false;
        }
        else {
            if( playerList[playerId].getCoins() >= 3 ) {
                playerList[playerId].setHasThirdField(true);
                playerList[playerId].setCoins(playerList[playerId].getCoins() - 3 );
                return true;
            }
            else{
                return false;
            }
        }
    }

    /**
     * plant a bean in a bean field
     *
     */
    public boolean plantBean(int playerId, int fieldId,
                             Deck origin) {
        //Check if player's turn
        if( turn != playerId ){
            return false;
        }
        if(origin.size() == 0) {
            return false;
        }
        if (playerList[playerId].getField(fieldId).size() == 0) {
            origin.moveTopCardTo(playerList[playerId].getField(fieldId));
            return true;
        }
        //check if card to be planted is the same as current bean in the field
        else if (playerList[playerId].getField(fieldId).peekAtTopCard().equals
                (origin.peekAtTopCard())) {
            origin.moveTopCardTo(playerList[playerId].getField(fieldId));
            return true;
        }
        return false;
    }

    /**
     * Harvest beans in a bean field
     *
     */
    public boolean harvestField(int playerId, Deck field){
        if( field.size() == 0 ) {
            return false;
        }
        field.getCards().clear();
        return true;
    }

    /**
     * turn over two cards that are up for trading
     *
     */
    public boolean turn2Cards(int playerId){
        if( turn != playerId ){
            return false;
        }
        if(mainDeck.size() < 2) {
            return false;
        }
        //move top two cards to trade deck
        mainDeck.moveTopCardTo(tradeDeck);
        mainDeck.moveTopCardTo(tradeDeck);
        phase = 1; //phase 1 starts now
        return true;
    }

    /**
     * start trading phase
     *
     */
    public boolean startTrading(int playerId) {
        if( turn != playerId || phase != 1 ){
            return false;
        }
        phase = 2;
        return true;
    }

    /**
     * Allow player to make an offer
     *
     */
    public boolean makeOffer(int traderId, Card[] offer) {
        if(phase != 2) {
            return false;
        }
        playerList[traderId].setMakeOffer(2); //user will trade
        playerList[traderId].setOffer(offer); //make traders offer cards visible
        return true;
    }

    /**
     * Allow player to state that they will choose to not participate in trading
     *
     */
    public boolean abstainFromTrading(int playerId) {
        if(phase != 2) {
            return false;
        }
        playerList[playerId].setMakeOffer(1);
        return true;
    }

    /**
     * End turn by drawing 3 cards from main deck.
     *
     */
    public boolean draw3Cards (int playerId) {
        if(turn != playerId) {
            return false;
        }
        //trade deck empty, then change turn to +1 unless 3 then turn to 0
        if(tradeDeck.size() == 0)
        {
            mainDeck.moveTopCardTo(playerList[playerId].getHand());
            mainDeck.moveTopCardTo(playerList[playerId].getHand());
            mainDeck.moveTopCardTo(playerList[playerId].getHand());
            if(turn == 3)
            {
                turn = 0;
            }
            else
            {
                turn++;
            }
            return true;
        }
        else
        {
            return false;
        }

    }

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


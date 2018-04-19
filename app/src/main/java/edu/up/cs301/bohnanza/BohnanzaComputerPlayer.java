package edu.up.cs301.bohnanza;

import android.util.Log;

import java.util.Random;

import edu.up.cs301.actions.AbstainFromTrading;
import edu.up.cs301.actions.DrawThreeCards;
import edu.up.cs301.actions.HarvestField;
import edu.up.cs301.actions.OfferResponse;
import edu.up.cs301.actions.MakeOffer;
import edu.up.cs301.actions.PlantBean;
import edu.up.cs301.actions.StartTrading;
import edu.up.cs301.actions.TurnTwoCards;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Implements the dumb and smart AIs by sending actions to the game
 * based on the current state of the game.
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class BohnanzaComputerPlayer extends GameComputerPlayer {

    // false for dumb AI and true for smart AI
    private boolean smartAI = false;
    // most recent state of the game
    protected BohnanzaState savedState;

    /**
     * Constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public BohnanzaComputerPlayer(String name) {
        super(name);
    }

    /**
     * Constructor
     *
     * @param name the player's name (e.g., "John")
     * @param initSmartAI whether or not its the smart AI
     *
     */
    public BohnanzaComputerPlayer(String name, boolean initSmartAI) {
        super(name);
        // Initialize dumb or smart computer player
        smartAI = initSmartAI;
    }

    /**
     * Callback method, called when we receive a message, typically from
     * the game
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        // Ignore if it's not a copy of our game
        if(!(info instanceof BohnanzaState)) {
            return;
        }

        // Update our state
        savedState = (BohnanzaState) info;

        if(savedState == null) return;
        // Run the functionality of initialized AI
        if (smartAI) {
            startSmartAI();
        }
        else {
            startDumbAI();
        }
    }

    /**
     * Invoked whenever the player's timer has ticked. It is expected
     * that this will be overridden in many players.
     */
    @Override
    protected void timerTicked() {
        // Not used
    }

    /**
     * Perform the functions of a smart AI based on the state of the
     * game, including more intelligent planting, harvesting, and trading
     */
    protected void startSmartAI(){
        // Will perform the functions of a smarter AI based on the state of the game
        synchronized(this) {
            // Ignore if it's not the computer's turn
            if(savedState.getTurn() != playerNum) {
                if ( savedState.getPhase()==2) {
                    //check to see if computer needs to make an offer
                    if(savedState.getPlayerList()[playerNum].getToPlant().size() > 0) {
                        plantBean(savedState.getPlayerList()[playerNum].getToPlant(),
                                savedState.getPlayerList()[playerNum].getAllFields(), 0);
                        return;
                    }
                    if(savedState.getPlayerList()[playerNum].getOffer() == null
                            && savedState.getPlayerList()[playerNum].getMakeOffer() != 1
                            && savedState.getTradeDeck().size() > 0) {
                        //check to see if has field of same type as card up for trade
                        if (isMakingOffer(savedState.getPlayerList()[playerNum].getAllFields(),
                                savedState.getTradeDeck().peekAtBottomCard())) {
                            if (tradingOffer() != -1) {
                                game.sendAction(new MakeOffer(this, tradingOffer()));
                            }
                        } else {
                            game.sendAction(new AbstainFromTrading(this));
                        }
                    }
                }
                return;
            }

            // Get player state
            BohnanzaPlayerState myInfo = savedState.getPlayerList()[playerNum];
            getTimer().start();
            sleep(250); //allow player to see computer's moves
            if(savedState.getPhase() == -1){
                // Plant first bean
                plantBean(myInfo.getHand(), myInfo.getAllFields(), 0);
                return;
            }
            else if(savedState.getPhase() == 0){
                // Turn two cards
                if(checkFields(myInfo.getAllFields(), myInfo.getHand().peekAtBottomCard())) {
                    plantBean(myInfo.getHand(), myInfo.getAllFields(), 0);
                    return;
                }
                game.sendAction(new TurnTwoCards(this));
                return;
            }
            else if(savedState.getPhase() == 1 && savedState.getTradeDeck().getCards().isEmpty()) {
                game.sendAction(new DrawThreeCards(this));
                return;
            }
            else if(savedState.getPhase() == 1) {
                // check if you want to plant the first trading card
                if(checkFields(myInfo.getAllFields(), savedState.getTradeDeck().peekAtBottomCard())) {
                    plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
                    return;
                }
                //check if you want to plant the second trading card
                else if(checkFields(myInfo.getAllFields(), savedState.getTradeDeck().peekAtTopCard())) {
                    plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 2);
                    return;
                }
                else {
                    game.sendAction(new StartTrading(this));
                }
            }
            else if(savedState.getPhase() == 2) {
                //Plant any cards that have been traded
                boolean endTurn = true;
                for(int i = 0; i<4; i++) {
                    if(savedState.getPlayerList()[i].getToPlant().size() > 0) {
                        endTurn = false;
                    }
                }
                if(endTurn && savedState.getTradeDeck().size() == 0) {
                    game.sendAction(new DrawThreeCards(this));
                    return;
                }
                else if(savedState.getTradeDeck().size() == 0 && myInfo.getToPlant().size() > 0){
                    plantBean(savedState.getPlayerList()[playerNum].getToPlant(),
                            savedState.getPlayerList()[playerNum].getAllFields(), 0);
                    return;
                }
                else if(savedState.getTradeDeck().size() == 0){
                    return;
                }
                //assess offers
                if(checkOffers()){
                    int playerOffer = otherOffers(myInfo.getAllFields());
                    if (playerOffer == -1) {
                        //no offers meet criteria
                        //plant remaining trade deck
                        plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
                    }
                    else {
                        //accept the offer
                        game.sendAction(new OfferResponse(this, playerOffer, true));
                    }
                }
            }
        }

    }

    /**
     * Perform the functions of a dumb AI. Loops through its turn by
     * doing the bare minimum to move play to the next player.
     */
    protected void startDumbAI(){
        synchronized(this) {
            // Get player state
            BohnanzaPlayerState myInfo = savedState.getPlayerList()[playerNum];

            // Ignore if it's not the computer's turn
            if (savedState.getTurn() != playerNum) {
                // During trading
                if(savedState.getPhase() == 2 &&
                        savedState.getPlayerList()[playerNum].getMakeOffer() != 1 &&
                        savedState.getTradeDeck().size() > 0){
                    game.sendAction(new AbstainFromTrading(this));
                    return;
                }
                return;
            }
            getTimer().start();
            sleep(250); //allow player to see computer's moves
            if(savedState.getPhase() == -1){
                // Plant first bean
                plantBean(myInfo.getHand(), myInfo.getAllFields(), 0);
            }
            else if(savedState.getPhase() == 0){
                // Turn two cards
                game.sendAction(new TurnTwoCards(this));
            }
            else if(savedState.getTradeDeck().getCards().isEmpty() && savedState.getPhase() == 1){
                // End turn
                game.sendAction(new DrawThreeCards(this));
            }
            else if(savedState.getPhase() == 1) {
                //plant the first bean in the trade deck
                plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
            }
        }
    }

    /**
     * Sends plant bean method to game when the fields are ready
     *
     * @param beans the Deck that currently holds the bean to be
     *              planted
     * @param fields the computer player's fields
     * @param origin whether the player is planting from their hand(0)
     *               or one of the trading cards (1 or 2)
     */
    protected void plantBean(Deck beans, Deck[] fields, int origin) {
        //try to plant in an occupied field first
        for (int i=0; i<2; i++) {
            if (fields[i].size() > 0 && fields[i].peekAtTopCard().equals(beans.peekAtBottomCard())) {
                game.sendAction(new PlantBean(this, i, origin));
                return;
            }
            else if(origin == 2 && fields[i].size() > 0
                    && fields[i].peekAtTopCard().equals(beans.peekAtTopCard())) {
                game.sendAction(new PlantBean(this, i, origin));
                return;
            }
        }
        //try to plant in an open field
        for (int i=0; i<2; i++) {
            if (fields[i].getCards().isEmpty()) {
                game.sendAction(new PlantBean(this, i, origin));
                return;
            }
        }
        if(!smartAI) {
            dumbHarvest();
        }
        else {
            smartHarvest(fields);
        }
    }
    /**
     * Figure out where to plant a bean by checking for the same
     * type or an empty field, harvest otherwise
     *
     * @param fields the computer player's fields
     * @param cardType a pointer to the card that needs to be planted
     *
     * @return the index of the target field
     */
    protected void findTargetField (Deck[] fields, Card cardType){
        Log.i("BCompP, smart", "FindTargetField");
        // Check for field of same type, then check for empty
        //no third field, so then harvesting
        for (int i=0; i<fields.length; i++) {
            if (!(fields[i].getCards().isEmpty()) &&
                    fields[i].peekAtTopCard().equals(cardType)) {
                return;
            }
            else if (fields[i].getCards().isEmpty()) {
                return;
            }
        }
        // Must harvest a field to plant in
        dumbHarvest();
        return;
    }

    /**
     * The harvest function for the dumb AI,
     * harvests random field based on boolean
     *
     * @return the index of the newly empty field
     */
    protected void dumbHarvest(){
        Random random = new Random();
        if(random.nextBoolean()){
            game.sendAction(new HarvestField(this, 0));
        }
        else{
            game.sendAction(new HarvestField(this, 1));
        }
        /**
         External Citation
         Date: 12 April 2018
         Problem: could not remember how to get a random boolean
         Resource:https://stackoverflow.com/questions/8878015/return-true-or-false-randomly/887804
         Solution: I used the example code from this post.
         */
    }

    /**
     * The harvest function for the smart AI,
     * harvests field with highest coin count
     *
     * @param fields the computer player's fields
     *
     * @return the index of the newly empty field
     */
    protected void smartHarvest(Deck[] fields) {
        Log.i("BCompP, smart", "Harvest");
        int target = 0;
        // Iterate through fields to find field with highest value
        for(int i=0; i<2; i++){
            if(fields[i].getFieldValue(fields[i]) >
                    fields[i].getFieldValue(fields[target])){
                target = i;
            }
        }
        game.sendAction(new HarvestField(this, target));
    }

    /**
     * method to asses offers the smart AI gets during trading
     * for their turn
     *
     * @param fields the computer player's fields
     *
     * @return the index of the newly empty field
     */
    protected int otherOffers(Deck[] fields) {
        int bestOffer = -1;
        for(int j = 0; j<savedState.getPlayerList().length; j++) {
            if (j != playerNum) {
                for (int i = 0; i < 2; i++) {
                    Log.i("BCompP, offers", "start check");
                    if(savedState.getPlayerList()[j].getOffer() != null) {
                        if (savedState.getPlayerList()[j].getOffer().equals(fields[i].peekAtTopCard())) {
                            Log.i("BCompP, offers", "done check");
                            return j;
                        }
                    }
                }
            }
        }
        return bestOffer;
    }

    protected boolean checkOffers(){
        for (int i=0; i<4; i++) {
            if (i != playerNum) {
                if (savedState.getPlayerList()[i].getMakeOffer() == 0) {
                    Log.i("BCompP, smart", "check");
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean checkFields(Deck[] fields, Card origin) {
        for(int i = 0; i<2; i++) {
            if(fields[i].size() > 0 && fields[i].peekAtTopCard().equals(origin)) {
                return true;
            }
        }
        return false;
    }


    protected boolean isMakingOffer(Deck[] fields, Card cardType){
        if(cardType == null ) {
            return false;
        }
        for (int i=0; i<2; i++){
            if(cardType.equals(fields[i].peekAtTopCard())){
                return true;
            }
        }
        return false;
    }

    protected int tradingOffer() {
        Card field1 = savedState.getPlayerList()[savedState.getTurn()].getField(0).peekAtTopCard();
        Card field2 = savedState.getPlayerList()[savedState.getTurn()].getField(1).peekAtTopCard();

        if (savedState.getPlayerList()[playerNum].getHand().getCards().contains(field1)) {
            return savedState.getPlayerList()[playerNum].getHand().getCards().indexOf(field1);
        }
        else if(savedState.getPlayerList()[playerNum].getHand().getCards().contains(field2)) {
            return savedState.getPlayerList()[playerNum].getHand().getCards().indexOf(field2);
        }
        else if(savedState.getPlayerList()[playerNum].getHand().size() > 0) {
            return 0;
        }
        else {
            return -1;
        }
    }

    protected boolean hasInField(Deck[] fields, Card cardType){


        return false;
    }

    //Getters
    public int getPlayerIndex() { return playerNum; }
}

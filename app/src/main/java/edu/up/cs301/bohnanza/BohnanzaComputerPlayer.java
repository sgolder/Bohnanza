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
        if (smartAI) {startSmartAI();}
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
            if (savedState.getTurn() != playerNum) {
                if ( savedState.getPhase()==2) {
                    //check to se if has field of same type as card up for trade
                    if(isMakingOffer(savedState.getPlayerList()[playerNum].getAllFields(),
                            savedState.getTradeDeck().peekAtTopCard())){
                        game.sendAction(new MakeOffer(this, tradingOffer()));
                    }
                    else{
                        game.sendAction(new AbstainFromTrading(this));
                    }
                }
            }

            // Get player state
            BohnanzaPlayerState myInfo = savedState.getPlayerList()[playerNum];
            getTimer().start();

            if(savedState.getPhase() == -1){
                // Plant first bean
                plantBean(myInfo.getHand(), myInfo.getAllFields(), 0);
                return;
            }
            if(savedState.getPhase() == 0){
                // Turn two cards
                game.sendAction(new TurnTwoCards(this));
                return;
            }
            if(savedState.getPhase() == 1) {
                // check if you want to plant the first trading card
                plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
                //check if you want to plant the second trading card
                //start trading
                if (!savedState.getTradeDeck().getCards().isEmpty()) {
                    game.sendAction(new StartTrading(this));
                }
                //plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
                //check the trading deck to start trading
            }
            if(savedState.getPhase() == 2) {
                //Plant any cards that have been traded
                if(savedState.getTradeDeck().getCards().isEmpty()){
                    while(!myInfo.getToPlant().getCards().isEmpty()){
                       game.sendAction(new PlantBean(this, 0, 0));
                    }
                    game.sendAction(new DrawThreeCards(this));
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
                if(savedState.getPhase() == 2){
                    // Make an offer
                    if(savedState.getPlayerList()[playerNum].getHand().getCards().get(0) != null
                            && savedState.getPlayerList()[playerNum].getOffer() == null) {
                        Log.i("BCompP, dumb", "MakeOffer");
                        game.sendAction(new MakeOffer(this, 0));
                        return;
                    }
                    if(!(savedState.getPlayerList()[playerNum].getToPlant().getCards().isEmpty())) {
                        plantBean(myInfo.getHand(), myInfo.getAllFields(), 0);
                        return;
                    }
                }
                return;
            }
            getTimer().start();
            if(savedState.getPhase() == -1){
                // Plant first bean
                plantBean(myInfo.getHand(), myInfo.getAllFields(), 0);
                return;
            }
            else if(savedState.getPhase() == 0){
                // Turn two cards
                game.sendAction(new TurnTwoCards(this));
                return;
            }
            else if(savedState.getTradeDeck().getCards().isEmpty() && savedState.getPhase() == 1){
                // End turn
                game.sendAction(new DrawThreeCards(this));
                return;
            }
            else if(savedState.getPhase() ==1){
                // Plant first trading card
                plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
                // Plant second trading card
                plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
                return;
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
        int target;
        //Field that bean will be planted in
        target = findTargetField(fields, beans.peekAtTopCard());

        // Send plant bean action to the game
        game.sendAction(new PlantBean(this, target, origin));
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
    protected int findTargetField (Deck[] fields, Card cardType){
        boolean fieldFound = false;
        int targetField =0;
        // Check for field of same type, then check for empty
        //no third field, so then harvesting
        for (int i=0; i<fields.length; i++) {
            if (fields[i].peekAtTopCard() == cardType) {
                return i;
            } else if (fields[i].getCards().isEmpty()) {
                return i;
            } else if ((i == 2) && !smartAI) {
                // Harvest if didn't find eligible field
                fieldFound = true;
                targetField = dumbHarvest();
                break;
            }
            else if( (i==2) && smartAI){
                // Harvest if didn't find eligible field
                fieldFound = true;
                targetField = smartHarvest(fields);
                break;
            }
        }
        if(fieldFound){
            return targetField;
        }
        else{
            //just in case
            return -1;
        }
    }

    /**
     * The harvest function for the dumb AI,
     * harvests random field based on boolean
     *
     * @return the index of the newly empty field
     */
    protected int dumbHarvest(){
        Random random = new Random();
        if(random.nextBoolean()){
            game.sendAction(new HarvestField(this, 0));
            return 0;
        }
        else{
            game.sendAction(new HarvestField(this, 1));
            return 1;
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
    protected int smartHarvest(Deck[] fields) {
        int target = 0;
        for(int i=0; i<2; i++){
            if(fields[i].getFieldValue(fields[i]) > target){
                game.sendAction(new HarvestField(this, i));
                target = i;
            }
        }
        return target;
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
        Log.i("BCompP, offers", "done check");
        for(int j = 0; j<savedState.getPlayerList().length; j++) {
            for (int i = 0; i < 2; i++) {
                if (savedState.getPlayerList()[j].getOffer() == fields[i].peekAtTopCard()) {
                    return j;
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


    protected boolean isMakingOffer(Deck[] fields, Card cardType){
        for (int i=0; i<2; i++){
            if(cardType == fields[i].peekAtTopCard()){
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
        } else {
            return savedState.getPlayerList()[playerNum].getHand().getCards().indexOf(field2);
        }
    }

    protected boolean hasInField(Deck[] fields, Card cardType){


        return false;
    }

    //Getters
    public int getPlayerIndex() { return playerNum; }
}

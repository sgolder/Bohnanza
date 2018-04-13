package edu.up.cs301.bohnanza;

import android.util.Log;

import java.util.Random;

import edu.up.cs301.actions.DrawThreeCards;
import edu.up.cs301.actions.HarvestField;
import edu.up.cs301.actions.MakeOffer;
import edu.up.cs301.actions.PlantBean;
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
    // increments as the computer player performs each step during
    // its turn, returns to -1 when it ends it' turn
    private int curPhase = -1;

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
    }

    /**
     * Perform the functions of a dumb AI. Loops through its turn by
     * doing the bare minimum to move play to the next player.
     */
    protected void startDumbAI(){
        synchronized(this) {
            // Ignore if it's not the computer's turn
            if (savedState.getTurn() != playerNum) {
                if(savedState.getPhase() == 2){
                    Log.i("BCompP, dumb", "MakeOffer");
                    game.sendAction(new MakeOffer(this,
                            savedState.getPlayerList()[playerNum].
                                    getHand().getCards().get(1)));
                }
                return;
            }
            // Get player state
            BohnanzaPlayerState myInfo = savedState.getPlayerList()[playerNum];
            getTimer().start();

            // Plant first bean
            plantBean(myInfo.getHand(), myInfo.getAllFields(), 0);

            // Turn two cards
            game.sendAction(new TurnTwoCards(this));

            // Plant first trading card
            plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
            // Plant second trading card
            plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);

            // End turn
            game.sendAction(new DrawThreeCards(this));
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
        // Check for field of same type, then check for empty
        for (int i=0; i<fields.length; i++) {
            if (fields[i].peekAtTopCard() == cardType) {
                //Log.i("BCompP targetField", "field"+i);
                return i;
            } else if (fields[i].getCards().isEmpty()) {
                Log.i("BCompP targetField", "field" + i);
                return i;
            } else if ((i == 2) || !savedState.getPlayerList()[playerNum].getHasThirdField() ){
                // Harvest if didn't find eligible field
                return dumbHarvest();
            }
        }
        //just in case
        return dumbHarvest();
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
    }

    //Getters
    public int getPlayerIndex() { return playerNum; }
}

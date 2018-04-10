package edu.up.cs301.bohnanza;

import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;

import edu.up.cs301.actions.AbstainFromTrading;
import edu.up.cs301.actions.DrawThreeCards;
import edu.up.cs301.actions.HarvestField;
import edu.up.cs301.actions.PlantBean;
import edu.up.cs301.actions.TurnTwoCards;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by Toshiba on 4/4/2018.
 */

public class BohnanzaComputerPlayer extends GameComputerPlayer {

    //false for dumb AI and true for smart AI
    private boolean smartAI = false;
    //most recent state of the game
    protected BohnanzaState savedState;
    private int curPhase = -1;

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public BohnanzaComputerPlayer(String name) {
        super(name);
    }

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     *
     * @param initSmartAI whether or not its the smart AI
     *
     */

    public BohnanzaComputerPlayer(String name, boolean initSmartAI) {
        super(name);
        smartAI = initSmartAI;
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        if(!(info instanceof BohnanzaState)) {
            return;
        }
        savedState = (BohnanzaState) info;
        Log.i("BCompP"+playerNum, "Received new state");
        if (smartAI) {startSmartAI();}
        else {
                if( startDumbAI() ) {
                    Log.i("BCompP"+playerNum, "AI successful");
                }
        }
    }

    @Override
    protected void timerTicked() {
        //getTimer().stop();
    }

    protected void startSmartAI(){}

    protected boolean startDumbAI(){
        synchronized(this) {
            Log.i("BCompP"+playerNum, "Current turn: " + savedState.getTurn());
            if (savedState.getTurn() != playerNum) {
                return false;
            }
            //get player state
            BohnanzaPlayerState myInfo = savedState.getPlayerList()[playerNum];

            if (savedState.getTurn() == playerNum) {
                getTimer().start();
                if (curPhase == -1) {
                    //plants from hand.
                    Log.i("BCompP"+playerNum, "Plant. Phase == " + savedState.getPhase());
                    plantBean(myInfo.getHand(), myInfo.getAllFields(), 0);
                    curPhase = 0;
                    return true;
                } else if (curPhase == 0) {
                    //turn two card
                    Log.i("BCompP"+playerNum, "Turn 2. Phase == " + savedState.getPhase());
                    game.sendAction(new TurnTwoCards(this));
                    curPhase = 1;
                    return true;
                } else if (curPhase == 1) {
                    plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
                    curPhase = 2;
                    return true;
                }
                else if(curPhase == 2){
                    plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
                    curPhase = 3;
                    return true;
                }
                else if(curPhase == 3){
                    game.sendAction(new HarvestField(this, 1));
                    curPhase = 4;
                    return true;
                }
                else if(curPhase == 4){
                    plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
                    curPhase = 5;
                    return true;
                }
                else if(curPhase == 5){
                    game.sendAction(new DrawThreeCards(this));
                    curPhase = -1;
                    sleep(3000);
                    return true;
                }
            } else {
                //when not turn
                //game.sendAction(new AbstainFromTrading(this));
            }
            return false;
        }
    }

    protected void plantBean(Deck beans, Deck[] fields, int origin) {
        //check fields
        int target;
        target = findTargetField(fields, beans.peekAtTopCard());

        game.sendAction(new PlantBean(this, target, origin));
    }

    protected int findTargetField (Deck[] fields, Card cardType){
        //check for desired field, harvest if necessary

        //check for field of same type, then check for empty, else harvest
        for (int i=0; i<fields.length; i++){
            if(fields[i].peekAtTopCard() == cardType) {
                return i;
            }
            else if(fields[i].getCards().isEmpty()){
                return i;
            }
        }
        //harvest if necessary
        int harvestNum = dumbHarvest(fields);
        if(harvestNum != -1){
            return harvestNum;
        }
        else{
            return -1;
        }
    }

    protected int dumbHarvest(Deck[] fields){
        for (int i=0; i<fields.length; i++){
            if(fields[i].size()>=1){
                game.sendAction(new HarvestField(this, i));
                return i;
            }
            else
            {
                return -1;
            }
        }
        return -1;
    }
}

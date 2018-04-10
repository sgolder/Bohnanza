package edu.up.cs301.bohnanza;

import android.util.Log;

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
    private int curPhase = 0;

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
        Log.i("BCompP", "Received new state");
        if (smartAI) {startSmartAI();}
        else {
            if(savedState.getTurn() == playerNum) {
                if( startDumbAI() ) {
                    Log.i("BCompP", "AI successful");
                }
            }
        }
    }

    @Override
    protected void timerTicked() {
        //getTimer().stop();
    }

    protected void startSmartAI(){}

    protected boolean startDumbAI(){

        //get player state
        BohnanzaPlayerState myInfo = savedState.getPlayerList()[playerNum];

        if(savedState.getTurn() == playerNum) {
            getTimer().start();
            if (savedState.getPhase() == -1) {
                //plants from hand.
                Log.i("BCompP", "Plant. Phase == "+savedState.getPhase());
                plantBean(myInfo.getHand(), myInfo.getAllFields(), 0);
                //sleep(3000);
                //savedState.setPhase(curPhase);
                return true;
            }
            else if (savedState.getPhase() == 0) {
                //turn two card
                Log.i("BCompP", "Turn 2. Phase == "+savedState.getPhase());
                game.sendAction(new TurnTwoCards(this));
                sleep(3000);
                return true;
            }
            else if (savedState.getPhase() == 1) {
                plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
                game.sendAction(new HarvestField(this, 1));
                plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);
                Log.i("BCompP", "Plant trade. Phase == "+savedState.getPhase());
                Log.i("BCompP", "Draw 3. Phase == "+savedState.getPhase());
                game.sendAction(new DrawThreeCards(this));
                //Log.i("BCompP", "startDumbAI: phase 1 pt2");

                //savedState.setPhase(curPhase);
                return true;
            }
        }
        else{
            //when not turn
            game.sendAction(new AbstainFromTrading(this));
        }
        return false;
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

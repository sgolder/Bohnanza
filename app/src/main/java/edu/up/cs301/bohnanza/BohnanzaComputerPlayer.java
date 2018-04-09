package edu.up.cs301.bohnanza;

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

        savedState = (BohnanzaState)info;

        if(smartAI) {startSmartAI();}
        else{startDumbAI();}
    }

    @Override
    protected void timerTicked() {

    }

    protected void startSmartAI(){}

    protected void startDumbAI(){

        //get player state
        BohnanzaPlayerState myInfo = savedState.getPlayerList()[playerNum];

        //plants from hand. Phase
        plantBean(myInfo.getHand(), myInfo.getAllFields(), 0);

        //turn two cards and plant them
        game.sendAction(new TurnTwoCards(this));
        plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 2);
        plantBean(savedState.getTradeDeck(), myInfo.getAllFields(), 1);

        //end turn by drawing 3 cards
        game.sendAction(new DrawThreeCards(this));

        //when not turn
        game.sendAction(new AbstainFromTrading(this));

    }

    protected void plantBean(Deck beans, Deck[] fields, int origin) {
        //plant the bean
        int size = beans.size();
        //check fields
        int target;
        for (int i = 0; i < size; i++){
            target = findTargetField(fields, beans.peekAtTopCard());
            game.sendAction(new PlantBean(this, target, origin));
        }
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
            if(fields[i].size()>1){
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

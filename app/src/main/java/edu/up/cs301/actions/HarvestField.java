package edu.up.cs301.actions;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by rbard on 4/4/2018.
 */

public class HarvestField extends GameAction{

    private int field;


    public HarvestField(GamePlayer initPlayer, int targetField){
        super(initPlayer);
        field = targetField;
    }

    public int getField() { return field; }
}

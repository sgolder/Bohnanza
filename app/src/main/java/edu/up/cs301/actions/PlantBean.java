package edu.up.cs301.actions;

import android.util.Log;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by rbard on 4/4/2018.
 */

public class PlantBean extends GameAction{

    private int field;

    public PlantBean(GamePlayer initPlayer, int targetField){
        super(initPlayer);
        field = targetField;
    }

    public int getField() { return field; }
}

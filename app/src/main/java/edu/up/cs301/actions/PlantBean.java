package edu.up.cs301.actions;

import edu.up.cs301.bohnanza.Deck;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by rbard on 4/4/2018.
 */

public class PlantBean extends GameAction{

    private int field;
    // 0: hand, 1: trade[0], 2: trade[1]
    private int origin;

    public PlantBean(GamePlayer initPlayer, int targetField){
        super(initPlayer);
        field = targetField;
    }

    public PlantBean(GamePlayer initPlayer, int targetField,
                     int initorigin){
        super(initPlayer);
        field = targetField;
        origin = initorigin;
    }

    public int getField() { return field; }
    public int getOrigin() { return origin; }
}

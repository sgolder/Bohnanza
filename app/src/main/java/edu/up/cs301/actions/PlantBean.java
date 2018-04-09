package edu.up.cs301.actions;

import edu.up.cs301.bohnanza.Deck;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by rbard on 4/4/2018.
 */

public class PlantBean extends GameAction{

    private int field;
    private Deck origin;

    public PlantBean(GamePlayer initPlayer, int targetField){
        super(initPlayer);
        field = targetField;
    }

    public PlantBean(GamePlayer initPlayer, int targetField,
                     Deck origin){
        super(initPlayer);
        field = targetField;
    }

    public int getField() { return field; }
    public Deck getOrigin() { return origin; }
}

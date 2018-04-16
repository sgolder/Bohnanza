package edu.up.cs301.actions;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A HarvestField action represents a player trying to harvest one
 * of their fields in exchange for coins.
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class HarvestField extends GameAction{

    private static final long serialVersionUID = 1705102015;

    private int field;

    /**
     * Constructor for HavestField class.
     * @param initPlayer player that is harvesting
     * @param targetField index of the field to harvest
     */
    public HarvestField(GamePlayer initPlayer, int targetField){
        super(initPlayer);
        field = targetField;
    }

    public int getField() { return field; }
}

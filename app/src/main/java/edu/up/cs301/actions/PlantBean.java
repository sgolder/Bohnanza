package edu.up.cs301.actions;

import edu.up.cs301.bohnanza.Deck;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A PlantBean action represents a player planting a bean to one of
 * their fields.
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class PlantBean extends GameAction{

    private int field;
    // 0: hand, 1: trade[0], 2: trade[1]
    private int origin;

    /**
     * Constructor for the PlantBean class.
     * @param initPlayer player who is planting
     * @param targetField index of the field the player would like
     *                    to plant in
     * @param initorigin where the player is planting from
     */
    public PlantBean(GamePlayer initPlayer, int targetField,
                     int initorigin){
        super(initPlayer);
        field = targetField;
        origin = initorigin;
    }

    public int getField() { return field; }
    public int getOrigin() { return origin; }
}

package edu.up.cs301.actions;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A DrawThreeCards action represents a player ending their turn.
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class DrawThreeCards extends GameAction{

    /**
     * Constructor for DrawThreeCards class.
     */
    public DrawThreeCards (GamePlayer initPlayer){

        super(initPlayer);
    }
}

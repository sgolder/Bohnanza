package edu.up.cs301.actions;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A TurnTwoCards action represents a player deciding to turn two
 * cards for trading.
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class TurnTwoCards extends GameAction {

    /**
     * Constructor for the TurnTwoCards class.
     */
    public TurnTwoCards(GamePlayer initPlayer){
        super(initPlayer);
    }
}

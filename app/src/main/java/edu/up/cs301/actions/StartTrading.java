package edu.up.cs301.actions;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A StartTrading action represents a player deciding to allow other
 * users to make offers for the current trading cards.
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class StartTrading extends GameAction {

    /**
     * Constructor for StartTrading class.
     */
    public StartTrading(GamePlayer initPlayer){
        super(initPlayer);
    }
}

package edu.up.cs301.actions;

import java.io.Serializable;

import edu.up.cs301.game.Game;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * An AbstainFromTrading action represents a decision not to trade
 * for current trading card.
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class AbstainFromTrading extends GameAction{

    private static final long serialVersionUID = 1905102017;

    /**
     * Constructor for AbstainFromTrading class.
     *
     * @param initPlayer player making the move
     */
    public AbstainFromTrading(GamePlayer initPlayer) {
        super(initPlayer);
    }

}

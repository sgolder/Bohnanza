package edu.up.cs301.actions;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A BuyThirdField action represents a player's decision to purchase
 * the third bean field.
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class BuyThirdField extends GameAction{

    /**
     * Constructor for BuyThirdField class.
     */
    public BuyThirdField(GamePlayer initPlayer){
        super(initPlayer);
    }
}

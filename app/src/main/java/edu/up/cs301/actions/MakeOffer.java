package edu.up.cs301.actions;

import edu.up.cs301.bohnanza.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A MakeOffer action represents a player choosing to offer a bean
 * from their hand in exchange for the current trading card.
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class MakeOffer extends GameAction {

    private int offer;

    /**
     * Constructor for MakeOffer class.
     *
     * @param initPlayer player making the offer
     * @param initoffer a pointer to the card the player is offering
     *                  from their hand.
     */
    public MakeOffer(GamePlayer initPlayer, int initoffer){
        super(initPlayer);
        offer = initoffer;
    }

    public int getOffer() { return offer; }
}

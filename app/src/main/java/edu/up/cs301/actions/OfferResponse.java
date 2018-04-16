package edu.up.cs301.actions;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * An OfferResponse action represents a player choosing to accept or
 * deny another player's offer for the current trading card.
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class OfferResponse extends GameAction {


    private static final long serialVersionUID = 1505102013;

    private int traderId;
    private boolean accept;

    /**
     * Constructor for the OfferResponse class.
     *
     * @param initPlayer the player whose turn it is
     * @param inittraderId the ID/index of the player whose offer
     *                     is being accepted or denied
     * @param initaccept acceptance or denial
     */
    public OfferResponse(GamePlayer initPlayer, int inittraderId,
                         boolean initaccept){
        super(initPlayer);
        traderId = inittraderId;
        accept = initaccept;
    }

    public int getTraderId() {
        return traderId;
    }

    public boolean isAccept() {
        return accept;
    }
}

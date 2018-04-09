package edu.up.cs301.actions;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by rbard on 4/4/2018.
 */

public class OfferResponse extends GameAction {

    private int traderId;
    private boolean accept;

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

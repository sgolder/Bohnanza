package edu.up.cs301.actions;

import edu.up.cs301.bohnanza.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by rbard on 4/4/2018.
 */

public class MakeOffer extends GameAction {

    private Card offer;

    public MakeOffer(GamePlayer initPlayer, Card initoffer){
        super(initPlayer);
        offer = initoffer;
    }

    public Card getOffer() { return offer; }
}

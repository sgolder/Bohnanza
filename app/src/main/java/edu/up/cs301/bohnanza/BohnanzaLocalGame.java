package edu.up.cs301.bohnanza;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by Toshiba on 4/2/2018.
 */

public class BohnanzaLocalGame extends LocalGame {

    BohnanzaState state;

    public BohnanzaLocalGame() {
        state = new BohnanzaState();
    }

    protected void sendUpdatedStateTo(GamePlayer p) {
        // if there is no state to send, ignore
        if (state == null) {
            return;
        }

        // make a copy of the state; null out all cards except for the
        // top card in the middle deck

        //TODO: figure out ctor that doesn't take a player number
        BohnanzaState stateForPlayer = new BohnanzaState(state, 0); // copy of state

        // send the modified copy of the state to the player
        p.sendInfo(stateForPlayer);
    }

    protected boolean canMove(int playerIdx) {
        return false;
    }

    protected String checkIfGameOver() {
        return null;
    }

    protected boolean makeMove(GameAction action) {
        return false;
    }
}

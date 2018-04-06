package edu.up.cs301.bohnanza;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by Toshiba on 4/4/2018.
 */

public class BohnanzaComputerPlayer extends GameComputerPlayer {

    private boolean smartAI = false;
    private BohnanzaState savedState;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public BohnanzaComputerPlayer(String name) {
        super(name);
    }

    public BohnanzaComputerPlayer(String name, boolean initsmartAI) {
        super(name);
        smartAI = initsmartAI;
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        if(!(info instanceof BohnanzaState)) {
            return;
        }

        savedState = (BohnanzaState)info;
    }

    @Override
    protected void timerTicked() {

    }
}

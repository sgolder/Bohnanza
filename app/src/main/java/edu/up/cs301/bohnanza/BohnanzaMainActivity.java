package edu.up.cs301.bohnanza;

import java.util.ArrayList;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;

/**
 * Created by Toshiba on 4/2/2018.
 */

public class BohnanzaMainActivity extends GameMainActivity {

    public static final int PORT_NUMBER = 4753;

    @Override
    public GameConfig createDefaultConfig() {
        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        playerTypes.add(new GamePlayerType("human player (green)") {
            public GamePlayer createPlayer(String name) {
                return new BohnanzaHumanPlayer(name);
            }
        });

        GameConfig defaultConfig = new GameConfig(playerTypes, 1, 4,
                        "Bohnanza", PORT_NUMBER);

        defaultConfig.addPlayer("Human", 0);

        defaultConfig.setRemoteData("Guest", "", 1);

        return defaultConfig;
    }

    @Override
    public LocalGame createLocalGame() {
        return new BohnanzaLocalGame();
    }
}

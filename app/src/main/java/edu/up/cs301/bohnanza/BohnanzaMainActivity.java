package edu.up.cs301.bohnanza;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;

/**
 * The primary activity for the Bohnanza game
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class BohnanzaMainActivity extends GameMainActivity {

    public static final int PORT_NUMBER = 15273;

    /**
     * A Bohnanza game for 4 players. Default is one human player and
     * three dumb AIs
     */
    @Override
    public GameConfig createDefaultConfig() {
        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        //lock display to landscape orientation
        super.setRequestedOrientation
                (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        playerTypes.add(new GamePlayerType("human player") {
            public GamePlayer createPlayer(String name) {
                return new BohnanzaHumanPlayer(name);
            }
        });
        playerTypes.add(new GamePlayerType("computer player (dumb)") {
            public GamePlayer createPlayer(String name) {
                return new BohnanzaComputerPlayer(name);
            }
        });
        playerTypes.add(new GamePlayerType("computer player (smart)") {
            public GamePlayer createPlayer(String name) {
                return new BohnanzaComputerPlayer(name, true);
            }
        });

        // Create a game configuration class for Bohnanza
        GameConfig defaultConfig = new GameConfig(playerTypes, 4, 4,
                "Bohnanza", PORT_NUMBER);

        // Add the default players
        defaultConfig.addPlayer("Human", 0);
        defaultConfig.addPlayer("Computer 1", 1);
        defaultConfig.addPlayer("Computer 2", 1);
        defaultConfig.addPlayer("Computer 3", 1);

        // Set the initial information for the remote player
        defaultConfig.setRemoteData("Guest", "", 0);

        return defaultConfig;
    }

    @Override
    public LocalGame createLocalGame() {
        return new BohnanzaLocalGame();
    }

    /**
     External Citation
     Date:      18 April 2018
     Problem:   did not know how to open a url with a button
     Resource:  https://stackoverflow.com/questions/36437590/
                open-a-url-link-on-click-of-ok-button-in-android-studio
     Solution:  Used an example from this post
     */

    /**
     External Citation
     Date:      18 April 2018
     Problem:   did not know how to connect a menu item to a listener
     Resource:  https://developer.android.com/guide/topics/resources/menu-resource.html
     Solution:  Read the description of a menu resource here
     */
    public void onHelpClicked(MenuItem help) {
        String url = "https://docs.google.com/gview?embedded=true&url=riograndegames.com/getFile.php?id=535";

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}



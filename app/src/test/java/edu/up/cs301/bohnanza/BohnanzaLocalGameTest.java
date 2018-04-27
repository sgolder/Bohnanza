package edu.up.cs301.bohnanza;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by golder18 on 4/27/2018.
 */
public class BohnanzaLocalGameTest {



    @Test
    public void buyThirdField() throws Exception {
        BohnanzaLocalGame game = new BohnanzaLocalGame();
        game.state.getPlayerList()[0].setCoins(5);

        assertEquals(game.buyThirdField(0), true);
        assertEquals(game.buyThirdField(1), false);
    }

    @Test
    public void plantBean() throws Exception {
        BohnanzaLocalGame game = new BohnanzaLocalGame();
        BohnanzaState state = game.state;

        // Their turn to plant
        game.plantBean(0, 0, state.getPlayerList()[0].getHand());
        assertEquals(state.getPlayerList()[0].getField(0).
                getCards().size(), 1);
        // Not their turn
        game.plantBean(1, 0, state.getPlayerList()[0].getHand());
        assertEquals(state.getPlayerList()[1].getField(0).
                getCards().size(), 0);
        // Wrong bean
        game.plantBean(0, 0, state.getPlayerList()[0].getHand());
        assertEquals(state.getPlayerList()[0].getField(0).
                getCards().size(), 0);
    }

    @Test
    public void harvestField() throws Exception {
        BohnanzaLocalGame game = new BohnanzaLocalGame();
        //game.plantBean(0, 0, game.state.getPlayerList()[0].getHand();
        game.harvestField(0, game.state.getPlayerList()[0].getField(0));
        assertEquals(game.state.getPlayerList()[0].getField(0).
                getCards().isEmpty(), true);

    }

    @Test
    public void turn2Cards() throws Exception {

    }

    @Test
    public void startTrading() throws Exception {

    }

    @Test
    public void makeOffer() throws Exception {

    }

    @Test
    public void abstainFromTrading() throws Exception {

    }

    @Test
    public void draw3Cards() throws Exception {

    }

    @Test
    public void offerResponse() throws Exception {

    }

}
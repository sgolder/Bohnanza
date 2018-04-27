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
                getCards().size(), 1);
    }

    @Test
    public void harvestField() throws Exception {
        BohnanzaLocalGame game = new BohnanzaLocalGame();

        // Harvest a field
        game.harvestField(0, game.state.getPlayerList()[0].getField(0));
        assertEquals(game.state.getPlayerList()[0].getField(0).
                getCards().isEmpty(), true);
        // Harvest someone else's field
        game.plantBean(0, 0, game.state.getPlayerList()[0].getHand());
        game.harvestField(1, game.state.getPlayerList()[0].getField(0));
        assertEquals(1, game.state.getPlayerList()[0].getField(0).
                getCards().size());
    }

    @Test
    public void turn2Cards() throws Exception {
        BohnanzaLocalGame game = new BohnanzaLocalGame();
        game.state.setPhase(0);

        game.turn2Cards(0);
        assertEquals(1, game.state.getPhase());
        assertEquals(2, game.state.getTradeDeck().getCards().size());
    }

    @Test
    public void startTrading() throws Exception {
        BohnanzaLocalGame game = new BohnanzaLocalGame();
        game.state.setPhase(1);

        game.startTrading(0);
        assertEquals(2, game.state.getPhase());
    }

    @Test
    public void makeOffer() throws Exception {
        BohnanzaLocalGame game = new BohnanzaLocalGame();
        BohnanzaState state = game.state;
        game.turn2Cards(0);
        state.setPhase(2);

        game.makeOffer(1, 2);
        assertEquals(state.getPlayerList()[1].getHand().getCards().get(2),
                state.getPlayerList()[1].getOffer());
        game.makeOffer(1, 6); // no card at that index, remains the same
        assertEquals(state.getPlayerList()[1].getHand().getCards().get(2),
                state.getPlayerList()[1].getOffer());
    }

    @Test
    public void abstainFromTrading() throws Exception {
        BohnanzaLocalGame game = new BohnanzaLocalGame();
        game.state.setPhase(2);

        game.abstainFromTrading(1);
        assertEquals(1, game.state.getPlayerList()[1].getMakeOffer());
    }

    @Test
    public void draw3Cards() throws Exception {
        BohnanzaLocalGame game = new BohnanzaLocalGame();
        game.state.setPhase(2);
        int cardNum = game.state.getPlayerList()[0].getHand().getCards().size();

        game.draw3Cards(0);
        assertEquals(1, game.state.getTurn());
        assertEquals(cardNum+3, game.state.getPlayerList()[0].getHand().
                getCards().size());
    }

    @Test
    public void offerResponse() throws Exception {
        BohnanzaLocalGame game = new BohnanzaLocalGame();
        BohnanzaState state = game.state;
        game.turn2Cards(0);
        state.setPhase(2);
        state.getPlayerList()[1].setOffer(2);

        game.offerResponse(0, 1, true);
        assertEquals(1, state.getPlayerList()[0].getToPlant().getCards().size());
        assertEquals(1, state.getPlayerList()[1].getToPlant().getCards().size());
    }

}
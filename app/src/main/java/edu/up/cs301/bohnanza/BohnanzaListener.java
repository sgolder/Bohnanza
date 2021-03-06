package edu.up.cs301.bohnanza;

import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import edu.up.cs301.actions.AbstainFromTrading;
import edu.up.cs301.actions.DrawThreeCards;
import edu.up.cs301.actions.BuyThirdField;
import edu.up.cs301.actions.HarvestField;
import edu.up.cs301.actions.MakeOffer;
import edu.up.cs301.actions.OfferResponse;
import edu.up.cs301.actions.PlantBean;
import edu.up.cs301.actions.StartTrading;
import edu.up.cs301.actions.TurnTwoCards;
import edu.up.cs301.game.Game;

/**
 * Listener class for BohnanzaHumanPlayer. Sends the game actions
 * based on human player's touches and clicks.
 *
 * Created by AdamMercer on 4/6/18.
 */

public class BohnanzaListener implements View.OnClickListener, View.OnTouchListener {
    private PlayerView player1View;
    private PlayerView player2View;
    private PlayerView player3View;
    private PlayerView player4View;
    private HandView handView;
    private TradeView tradeView;
    private ArrayList<RectF> cardPositions = new ArrayList<>();
    private BohnanzaHumanPlayer humanPlayer;
    private Game game;
    // 0: hand, 1: trade[0], 2: trade[1]
    private int origin = 0;

    private boolean harvesting = false; // the player intends to harvest
    private boolean makeOffer = false; // the player is making an offer

    private BohnanzaState state;
    private int playerId;


    public BohnanzaListener (BohnanzaState bohnanzaState, int player, PlayerView initPlayer1,
                             PlayerView initPlayer2, PlayerView initPlayer3, PlayerView initPlayer4,
                             HandView initHand, TradeView initTrade, BohnanzaHumanPlayer initHumanPlayer,
                             Game initGame) {
        state = bohnanzaState;
        playerId = player;
        player1View = initPlayer1;
        player2View = initPlayer2;
        player3View = initPlayer3;
        player4View = initPlayer4;
        handView = initHand;
        tradeView = initTrade;
        humanPlayer = initHumanPlayer;
        game = initGame;
    }

    /**
     * determine what action to take based on the button that was pressed
     *
     * @param view
     * 		the view that was touched
     */
    @Override
    public void onClick(View view) {
        if(state.getPhase() == 2 && state.getTurn() != playerId && state.getTradeDeck().size() > 0) {
            tradeView.setActiveCard(1);
        }
        else {
            tradeView.setActiveCard(0);
        }
        String buttonLabel;
        if(view instanceof Button) {
            buttonLabel = (String)((Button) view).getText();
            //user presses Harvest Button
            if(buttonLabel.equalsIgnoreCase("Harvest")) {
                harvesting = !harvesting;
                humanPlayer.setPopups();
            }
            //user presses Make Offer button
            else if(buttonLabel.equalsIgnoreCase("Make Offer")) {
                makeOffer = !makeOffer;
                humanPlayer.setPopups();
            }
            //user presses Start Trading button
            else if(buttonLabel.equalsIgnoreCase("Start Trading")) {
                game.sendAction(new StartTrading(humanPlayer));
            }
            //user presses Draw 3 Cards button
            else if(buttonLabel.equalsIgnoreCase("Draw 3 Cards")) {
                game.sendAction(new DrawThreeCards(humanPlayer));
            }
            //user presses Pass button
            else if(buttonLabel.equalsIgnoreCase("Pass")) {
                game.sendAction(new AbstainFromTrading(humanPlayer));
            }
            //user presses Turn 2 Cards button
            else if(buttonLabel.equalsIgnoreCase("Turn 2 Cards")) {
                game.sendAction(new TurnTwoCards(humanPlayer));
            }
        }
    }

    /**
     * determine what action to take based on which surface view the user touched,
     * and where they touched on the surface view
     *
     * @param view
     * 		surface view that was touched
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() != MotionEvent.ACTION_DOWN) return false;

        int yPos = (int)motionEvent.getY();
        int xPos = (int)motionEvent.getX();
        int height = view.getHeight();
        int width = view.getWidth();
        cardPositions = handView.getCardPositions();

        //user touches on player1
        if(view.equals(player1View)) {
            playerViewTouched(0, yPos, xPos, height, width);
        }
        //user touches in player2
        else if(view.equals(player2View)) {
            playerViewTouched(1, yPos, xPos, height, width);
        }
        //user touches in player3
        else if(view.equals(player3View)) {
            playerViewTouched(2, yPos, xPos, height, width);
        }
        //user touches player4
        else if(view.equals(player4View)) {
            playerViewTouched(3, yPos, xPos, height, width);
        }
        else if(view.equals(handView)) {
            tradeView.setActiveCard(0);
            if(yPos < height/7) {
                return false;
            }
            for(int i=0; i<cardPositions.size(); i++) {
                if(cardPositions.get(i).contains(xPos, yPos)) {
                    Log.i("Card Pressed", "Card "+i);
                    if(makeOffer) {
                        Card offer = state.getPlayerList()[playerId].getHand().
                                getCards().get(i);
                        game.sendAction(new MakeOffer(humanPlayer, i));
                        makeOffer = false;
                    }
                    return true;
                }
            }
        }
        else if(view.equals(tradeView)) {
            if(tradeView.getCard1Rect().contains(xPos, yPos)) {
                origin = 1;
                if(state.getTurn() == playerId) {
                    tradeView.setActiveCard(1);
                }
                else {
                    tradeView.setActiveCard(0);
                }
                Log.i("Trade Pressed", "Card 1");
            }
            else if(tradeView.getCard2Rect().contains(xPos,yPos)) {
                origin = 2;
                if(state.getTurn() == playerId) {
                    tradeView.setActiveCard(2);
                }
                else {
                    tradeView.setActiveCard(0);
                }
                Log.i("Trade Pressed", "Card 2");
            }
        }

        if(state == null ) {
            Log.i("BohnanzaListener", "state null");
            return false;
        }
        if(game == null ) {
            Log.i("BohnanzaListener", "game null");
            return false;
        }

        if(state.getPhase() == 2 && state.getTurn() != playerId && state.getTradeDeck().size() > 0) {
            tradeView.setActiveCard(1);
        }

        return true;
    }

    /**
     * determine the action to take based on where the user touches the player view
     *
     * @param playerView the player view that was touched
     * @param yPos the y coordinate the player touched at on the player view
     * @param xPos the x coordinate the player touched at on the player view
     * @param height the height of player view
     * @param width the the width of the player view
     */
    private void playerViewTouched(int playerView, int yPos, int xPos, int height, int width) {
        RectF acceptRect = new RectF(width/10, 17*height/20-5, 3*width/10-10, 19*height/20-15);
        RectF rejectRect = new RectF(7*width/10+5, 17*height/20-5, 9*width/10-5, 19*height/20-15);
        tradeView.setActiveCard(0);
        Log.i("View Pressed", "Player 1 View");
        //user touches field 1
        if(yPos > height/20+20 && yPos < 11*height/40+25 && playerId == playerView) {
            if(harvesting) {
                game.sendAction(new HarvestField(humanPlayer, 0));
                harvesting = false;
            }
            else {
                game.sendAction(new PlantBean(humanPlayer, 0, origin));
            }
            Log.i("Field Pressed", "Field 1");
        }
        //user touches field 2
        else if(yPos > 11*height/40+25 && yPos < 21*height/40+15 && playerId == playerView) {
            if(harvesting) {
                game.sendAction(new HarvestField(humanPlayer, 1));
                harvesting = false;
            }
            else {
                game.sendAction(new PlantBean(humanPlayer, 1, origin));
            }
            Log.i("Field Pressed", "Field 2");
        }
        //user touches field 3
        else if(yPos > 21*height/40+15 && yPos < 3*height/4+25 && playerId == playerView) {
            if(!state.getPlayerList()[playerId].getHasThirdField()){
                game.sendAction(new BuyThirdField(humanPlayer));
            }
            else if(harvesting) {
                game.sendAction(new HarvestField(humanPlayer, 2));
                harvesting = false;
            }
            else {
                game.sendAction(new PlantBean(humanPlayer, 2, origin));
            }
            Log.i("Field Pressed", "Field 3");
        }
        else if(yPos > 3*height/4+25) {
            if(acceptRect.contains(xPos, yPos)) {
                game.sendAction(new OfferResponse(humanPlayer, playerView, true));
            }
            else if(rejectRect.contains(xPos, yPos)) {
                game.sendAction(new OfferResponse(humanPlayer, playerView, false));
            }
            Log.i("Field Pressed", "Player Hand");
        }
        origin = 0;
    }

    //setters
    public void setState( BohnanzaState initstate ) { state = initstate; }
    public void setGame( Game initgame ) { game = initgame; }
    public void setPlayerId(int initPlayerId) {playerId = initPlayerId;}
    public void setMakeOffer(boolean initMakeOffer) {makeOffer = initMakeOffer;}
    //getters
    public boolean getMakeOffer(){return makeOffer;}
    public boolean getHarvest(){return harvesting;}
}

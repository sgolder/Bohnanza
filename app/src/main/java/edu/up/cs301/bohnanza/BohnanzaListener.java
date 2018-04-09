package edu.up.cs301.bohnanza;

import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import edu.up.cs301.actions.DrawThreeCards;
import edu.up.cs301.actions.BuyThirdField;
import edu.up.cs301.actions.HarvestField;
import edu.up.cs301.actions.PlantBean;
import edu.up.cs301.game.Game;

/**
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

    private boolean harvesting; // the player intends to harvest

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

    @Override
    public void onClick(View view) {
        String buttonLabel;
        if(view instanceof Button) {
            buttonLabel = (String)((Button) view).getText();
            //user presses Harvest Button
            if(buttonLabel.equalsIgnoreCase("Harvest")) {
                harvesting = true;
                Log.i("Button Pressed", buttonLabel);
            }
            //user presses Make Offer button
            else if(buttonLabel.equalsIgnoreCase("Make Offer")) {
                Log.i("Button Pressed", buttonLabel);
            }
            //user presses Start Trading button
            else if(buttonLabel.equalsIgnoreCase("Start Trading")) {
                Log.i("Button Pressed", buttonLabel);
            }
            //user presses End Turn button
            else if(buttonLabel.equalsIgnoreCase("End Turn")) {
                Log.i("Button Pressed", buttonLabel);
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        if (motionEvent.getAction() != MotionEvent.ACTION_DOWN) return false;

        int yPos = (int)motionEvent.getY();
        int xPos = (int)motionEvent.getX();
        int height = view.getHeight();
        cardPositions = handView.getCardPositions();

        //user touches in player1
        if(view.equals(player1View)) {
            Log.i("View Pressed", "Player 1 View");
            //user touches field 1
            if(yPos > height/20+20 && yPos < 11*height/40+25) {
                if(harvesting) {
                    game.sendAction(new HarvestField(humanPlayer, 0));
                    harvesting = false;
                }
                else {
                    game.sendAction(new PlantBean(humanPlayer, 0));
                }
                Log.i("Field Pressed", "Field 1");
            }
            //user touches field 2
            else if(yPos > 11*height/40+25 && yPos < 21*height/40+15) {
                if(harvesting) {
                    game.sendAction(new HarvestField(humanPlayer, 1));
                    harvesting = false;
                }
                else {
                    game.sendAction(new PlantBean(humanPlayer, 1));
                }
                Log.i("Field Pressed", "Field 2");
            }
            //user touches field 3
            else if(yPos > 21*height/40+15 && yPos < 3*height/4+25) {
                if(harvesting) {
                    game.sendAction(new HarvestField(humanPlayer, 2));
                    harvesting = false;
                }
                else {
                    game.sendAction(new PlantBean(humanPlayer, 2));
                }
                Log.i("Field Pressed", "Field 3");
                if(!state.getPlayerList()[playerId].getHasThirdField()){
                    game.sendAction(new BuyThirdField(humanPlayer));
                }
            }
            else if(yPos > 3*height/4+25) {

                Log.i("Field Pressed", "Player Hand");
            }
        }
        //user touches in player2
        else if(view.equals(player2View)) {
            Log.i("View Pressed", "Player 2 View");
            //user touches field 1
            if(yPos > height/20+20 && yPos < 11*height/40+25) {
                if(harvesting) {
                    game.sendAction(new HarvestField(humanPlayer, 0));
                    harvesting = false;
                }
                else {
                    game.sendAction(new PlantBean(humanPlayer, 0));
                }
                Log.i("Field Pressed", "Field 1");
            }
            //user touches field 2
            else if(yPos > 11*height/40+25 && yPos < 21*height/40+15) {
                if(harvesting) {
                    game.sendAction(new HarvestField(humanPlayer, 1));
                    harvesting = false;
                }
                else {
                    game.sendAction(new PlantBean(humanPlayer, 1));
                }
                Log.i("Field Pressed", "Field 2");
            }
            //user touches field 3
            else if(yPos > 21*height/40+15 && yPos < 3*height/4+25) {
                if(harvesting) {
                    game.sendAction(new HarvestField(humanPlayer, 2));
                    harvesting = false;
                }
                else {
                    game.sendAction(new PlantBean(humanPlayer, 2));
                }
                Log.i("Field Pressed", "Field 3");
                if(!state.getPlayerList()[playerId].getHasThirdField()){
                    game.sendAction(new BuyThirdField(humanPlayer));
                }
            }
            else if(yPos > 3*height/4+25) {
                Log.i("Field Pressed", "Player Hand");
            }
        }
        //user touches in player3
        else if(view.equals(player3View)) {
            Log.i("View Pressed", "Player 3 View");
            //user touches field 1
            if(yPos > height/20+20 && yPos < 11*height/40+25) {
                if(harvesting) {
                    game.sendAction(new HarvestField(humanPlayer, 0));
                    harvesting = false;
                }
                else {
                    game.sendAction(new PlantBean(humanPlayer, 0));
                }
                Log.i("Field Pressed", "Field 1");
            }
            //user touches field 2
            else if(yPos > 11*height/40+25 && yPos < 21*height/40+15) {
                if(harvesting) {
                    game.sendAction(new HarvestField(humanPlayer, 1));
                    harvesting = false;
                }
                else {
                    game.sendAction(new PlantBean(humanPlayer, 1));
                }
                Log.i("Field Pressed", "Field 2");
            }
            //user touches field 3
            else if(yPos > 21*height/40+15 && yPos < 3*height/4+25) {
                if(harvesting) {
                    game.sendAction(new HarvestField(humanPlayer, 2));
                    harvesting = false;
                }
                else {
                    game.sendAction(new PlantBean(humanPlayer, 2));
                }
                if(!state.getPlayerList()[playerId].getHasThirdField()){
                    game.sendAction(new BuyThirdField(humanPlayer));
                }
                Log.i("Field Pressed", "Field 3");
            }
            else if(yPos > 3*height/4+25) {
                Log.i("Field Pressed", "Player Hand");
            }
        }
        //user touches player4
        else if(view.equals(player4View)) {
            Log.i("View Pressed", "Player 4 View");
            //user touches field 1
            if(yPos > height/20+20 && yPos < 11*height/40+25) {
                if(harvesting) {
                    game.sendAction(new HarvestField(humanPlayer, 0));
                    harvesting = false;
                }
                else {
                    game.sendAction(new PlantBean(humanPlayer, 0));
                }
                Log.i("Field Pressed", "Field 1");
            }
            //user touches field 2
            else if(yPos > 11*height/40+25 && yPos < 21*height/40+15) {
                if(harvesting) {
                    game.sendAction(new HarvestField(humanPlayer, 1));
                    harvesting = false;
                }
                else {
                    game.sendAction(new PlantBean(humanPlayer, 1));
                }
                Log.i("Field Pressed", "Field 2");
            }
            //user touches field 3
            else if(yPos > 21*height/40+15 && yPos < 3*height/4+25) {
                if(harvesting) {
                    game.sendAction(new HarvestField(humanPlayer, 2));
                    harvesting = false;
                }
                else {
                    game.sendAction(new PlantBean(humanPlayer, 2));
                }
                if(!state.getPlayerList()[playerId].getHasThirdField()){
                    game.sendAction(new BuyThirdField(humanPlayer));
                }
                Log.i("Field Pressed", "Field 3");
            }
            else if(yPos > 3*height/4+25) {
                Log.i("Field Pressed", "Player Hand");
            }
        }
        else if(view.equals(handView)) {
            if(yPos < height/7) {
                return false;
            }
            for(int i=0; i<cardPositions.size(); i++) {
                if(cardPositions.get(i).contains(xPos, yPos)) {
                    Log.i("Card Pressed", "Card "+i);
                    return true;
                }
            }
        }
        else if(view.equals(tradeView)) {
            if(tradeView.getCard1Rect().contains(xPos, yPos)) {
                Log.i("Trade Pressed", "Card 1");
            }
            else if(tradeView.getCard2Rect().contains(xPos,yPos)) {
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

        return true;
    }

    public void setState( BohnanzaState initstate ) { state = initstate; }
    public void setGame( Game initgame ) { game = initgame; }
}

package edu.up.cs301.bohnanza;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import edu.up.cs301.actions.PlantBean;
import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.infoMsg.GameInfo;


/**
 * Created by Toshiba on 4/2/2018.
 */

public class BohnanzaHumanPlayer extends GameHumanPlayer implements Animator {

    protected BohnanzaState state;
    private Activity myActivity;
    private AnimationSurface surface;
    private PlayerView player1View;
    private PlayerView player2View;
    private PlayerView player3View;
    private PlayerView player4View;
    private HandView handView;
    private TradeView tradeView;
    private LinearLayout bottomLayout;
    private BohnanzaListener myListener;
    Button harvest;
    Button button2;
    Button button3;
    Button button4;


    //////missing variables
    private int playerIndex;
    private String playerName;

    /**
     * constructor
     *
     * @param name the name of the player
     */
    public BohnanzaHumanPlayer(String name) {
        super(name);
        //TODO: sync up the playerId
        playerIndex = 0;
    }

    public void setAsGui(GameMainActivity activity) {
        myActivity = activity;

        activity.setContentView(R.layout.bohnanza_human_player);

        //surface = (AnimationSurface) myActivity.findViewById(R.id.surfaceViewPlayer1);
        //surface.setAnimator(this);

        player1View = (PlayerView) myActivity.findViewById(R.id.surfaceViewPlayer1);
        player2View = (PlayerView)myActivity.findViewById(R.id.surfaceViewPlayer2);
        player3View = (PlayerView)myActivity.findViewById(R.id.surfaceViewPlayer3);
        player4View = (PlayerView)myActivity.findViewById(R.id.surfaceViewPlayer4);
        handView = (HandView)myActivity.findViewById(R.id.surfaceViewHand);
        tradeView = (TradeView)myActivity.findViewById(R.id.surfaceViewTrade);

        bottomLayout = (LinearLayout)myActivity.findViewById(R.id.BottomLinearLayout);

        myListener = new BohnanzaListener(state, playerNum, player1View, player2View,
                player3View, player4View, handView, tradeView, this, game);

        harvest = (Button)myActivity.findViewById(R.id.buttonHarvest);
        button2 = (Button)myActivity.findViewById(R.id.button2);
        button3 = (Button)myActivity.findViewById(R.id.button3);
        button4 = (Button)myActivity.findViewById(R.id.button4);

        harvest.setOnClickListener(myListener);
        button2.setOnClickListener(myListener);
        button3.setOnClickListener(myListener);
        button4.setOnClickListener(myListener);
        player1View.setOnTouchListener(myListener);
        player2View.setOnTouchListener(myListener);
        player3View.setOnTouchListener(myListener);
        player4View.setOnTouchListener(myListener);
        handView.setOnTouchListener(myListener);
        tradeView.setOnTouchListener(myListener);

        Card.initImages(activity);
    }

    public int interval() {
        return 0;
    }

    public int backgroundColor() {
        return Color.rgb(45, 45, 45);
    }

    public boolean doPause() {
        return false;
    }

    public boolean doQuit() {
        return false;
    }

    public void tick(Canvas g) {

    }

    /////////////////(Buttons, SurfaceView) -> (x,y)
    ///////////////// connect the clicks to the locations
    public void onTouch(MotionEvent event) {
        // we use the BohnanzaListener instead
    }

    @Override
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    @Override
    public void receiveInfo(GameInfo info) {
        if(!(info instanceof BohnanzaState)) {
            return;
        }
        state = (BohnanzaState)info;
        drawGUI();
        myListener.setState( state );
        myListener.setGame( game );
    }

    public void baseLayout(Canvas canvas) {

    }
    ///////Gui!!!
    public void drawGUI() {
        Log.i("Player ID", ""+playerIndex);
        if(state == null) return;

        int player1Color = Color.rgb(37, 127, 37);
        int player2Color = Color.rgb(220, 102, 30);
        int player3Color = Color.rgb(7, 169, 207);
        int player4Color = Color.rgb(248, 93, 89);
        int[] playerColors = {player1Color, player2Color, player3Color, player4Color};

        int[] delete = {0};
        Card getCards = new Card(0, "", delete, 0);
        Bitmap[] cardImages = getCards.getCardImages();


        int[][] beanIdx = new int[4][3];
        int[][] numBeans = new int[4][3];
        for(int i = 0; i<4; i++) {
            for(int j = 0; j<3; j++) {
                numBeans[i][j] = state.getPlayerList()[i].getField(j).getCards().size();
                if(numBeans[i][j] > 0) {
                    beanIdx[i][j] = state.getPlayerList()[i].getField(j).peekAtTopCard().getBeanIdx();
                }
                else{
                    beanIdx[i][j] = 8;
                }
            }
        }

        PlayerView[] playerViews = {player1View, player2View, player3View, player4View};

        //set player fields and player hands
        for(int i = 0; i<4; i++) {
            playerViews[i].setPlayerColor(playerColors[i]);
            playerViews[i].setPlayerName("Player " +(i+1));
            playerViews[i].setField1Bean(cardImages[beanIdx[i][0]], numBeans[i][0]);
            playerViews[i].setField2Bean(cardImages[beanIdx[i][1]], numBeans[i][1]);
            playerViews[i].setField3Bean(cardImages[beanIdx[i][2]], numBeans[i][2]);
            int[] handIdx = new int[state.getPlayerList()[i].getHand().size()];
            ArrayList<Bitmap> hand = new ArrayList<>();
            for(int j = 0; j<state.getPlayerList()[i].getHand().size(); j++) {
                handIdx[j] = state.getPlayerList()[i].getHand().getCards().get(j).getBeanIdx();
                hand.add(cardImages[handIdx[j]]);
            }
            playerViews[i].setHandCards(hand);
            playerViews[i].setCoins(state.getPlayerList()[i].getCoins());
            playerViews[i].setThirdField(state.getPlayerList()[i].getHasThirdField());
        }

        ArrayList<Bitmap> hand = new ArrayList<>();
        for(int i = 0; i<state.getPlayerList()[playerNum].getHand().size(); i++) {
            hand.add(cardImages[state.getPlayerList()[playerNum].getHand().getCards().get(i).getBeanIdx()]);
        }
        handView.setHand(hand);
        if(state.getTradeDeck().getCards().size() == 0) {
            tradeView.setCard1Bean(null);
            tradeView.setCard2Bean(null);
        }
        else if(state.getTradeDeck().getCards().size() == 1) {
            tradeView.setCard1Bean(cardImages[state.getTradeDeck().getCards().get(0).getBeanIdx()]);
            tradeView.setCard2Bean(null);
        }
        else if(state.getTradeDeck().getCards().size() == 2) {
            tradeView.setCard1Bean(cardImages[state.getTradeDeck().getCards().get(0).getBeanIdx()]);
            tradeView.setCard2Bean(cardImages[state.getTradeDeck().getCards().get(1).getBeanIdx()]);
        }

        if(state.getPhase() == -1) {
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
        else if(state.getPhase() == 0 && state.getTurn() == playerNum) {
            button2.setText("Turn 2 Cards");
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
        else if(state.getPhase() == 0 && state.getTurn() != playerNum) {
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
        else if(state.getPhase() == 1 && state.getTradeDeck().getCards().size() == 0 &&
                state.getTurn() == playerNum) {
            button2.setText("Draw 3 Cards");
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
            state.setPhase(2);
        }
        else if(state.getPhase() == 1 && state.getTradeDeck().getCards().size() == 0 &&
                state.getTurn() != playerNum) {
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
            state.setPhase(2);
        }
        else if(state.getPhase() == 1 && state.getTurn() == playerNum) {
            button2.setText("Start Trading");
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
        else if(state.getPhase() == 1 && state.getTurn() != playerNum) {
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
        else if(state.getPhase() == 2 && state.getTurn() == playerNum) {
            button2.setText("Make Offer");
            button3.setText("Pass");
            button4.setText("Draw 3 Cards");
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.VISIBLE);
        }
        else if(state.getPhase() == 2 && state.getTurn() != playerNum) {
            button2.setText("Make Offer");
            button3.setText("Pass");
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }

        bottomLayout.setBackgroundColor(Color.rgb(45, 45, 45));
    }

    //Getters
    public int getPlayerIndex() { return playerIndex; }
}

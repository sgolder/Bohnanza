package edu.up.cs301.bohnanza;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

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

    /**
     * constructor
     *
     * @param name the name of the player
     */
    public BohnanzaHumanPlayer(String name) {
        super(name);
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

        BohnanzaListener myListener = new BohnanzaListener(player1View, player2View,
                player3View, player4View, handView, tradeView);

        Button harvest = (Button)myActivity.findViewById(R.id.buttonHarvest);
        Button makeOffer = (Button)myActivity.findViewById(R.id.buttonMakeOffer);
        Button endTurn = (Button)myActivity.findViewById(R.id.buttonEndTurn);
        Button startTrading = (Button)myActivity.findViewById(R.id.buttonStartTrading);


        harvest.setOnClickListener(myListener);
        makeOffer.setOnClickListener(myListener);
        endTurn.setOnClickListener(myListener);
        startTrading.setOnClickListener(myListener);
        player1View.setOnTouchListener(myListener);
        player2View.setOnTouchListener(myListener);
        player3View.setOnTouchListener(myListener);
        player4View.setOnTouchListener(myListener);
        handView.setOnTouchListener(myListener);
        tradeView.setOnTouchListener(myListener);

        Card.initImages(activity);

        drawGUI();
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
        baseLayout(g);
    }

    public void onTouch(MotionEvent event) {

    }

    @Override
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    @Override
    public void receiveInfo(GameInfo info) {

    }

    public void baseLayout(Canvas canvas) {

    }

    public void drawGUI() {
        int player1Color = Color.rgb(37, 127, 37);
        int player2Color = Color.rgb(220, 102, 30);
        int player3Color = Color.rgb(7, 169, 207);
        int player4Color = Color.rgb(248, 93, 89);

        Card getCards = new Card("");
        Bitmap[] cardImages = getCards.getCardImages();

        player1View.setBackGroundColor(player1Color);
        player1View.setPlayerName("Player 1");
        player1View.setField1Bean(cardImages[4], 6);
        player1View.setField2Bean(cardImages[0], 1);
        player1View.setCoins(13);
        ArrayList<Bitmap> hand1 = new ArrayList<>();
        for(int i = 0; i<4; i++) {
            hand1.add(cardImages[2]);
        }
        hand1.add(cardImages[6]);
        hand1.add(cardImages[5]);
        player1View.setHandCards(hand1);

        player2View.setBackGroundColor(player2Color);
        player2View.setPlayerName("Player 1");
        player2View.setField1Bean(cardImages[2], 6);
        player2View.setField2Bean(cardImages[5], 1);
        player2View.setField3Bean(cardImages[7], 9);
        player2View.setCoins(5);
        ArrayList<Bitmap> hand2 = new ArrayList<>();
        for(int i = 0; i<7; i++) {
            hand2.add(cardImages[2]);
        }
        player2View.setHandCards(hand2);

        player3View.setBackGroundColor(player3Color);
        player3View.setPlayerName("Player 3");
        player3View.setField1Bean(cardImages[10], 5);
        player3View.setField2Bean(cardImages[9], 11);
        player3View.setCoins(10);
        ArrayList<Bitmap> hand3 = new ArrayList<>();
        for(int i = 0; i<3; i++) {
            hand3.add(cardImages[2]);
        }
        hand3.add(cardImages[8]);
        player3View.setHandCards(hand3);

        player4View.setBackGroundColor(player4Color);
        player4View.setPlayerName("Player 4");
        player4View.setField1Bean(cardImages[1], 2);
        player4View.setField2Bean(cardImages[3], 4);
        player4View.setCoins(7);
        ArrayList<Bitmap> hand4 = new ArrayList<>();
        for(int i = 0; i<8; i++) {
            hand4.add(cardImages[2]);
        }
        player4View.setHandCards(hand4);

        ArrayList<Bitmap> hand = new ArrayList<>();
        hand.add(cardImages[0]);
        hand.add(cardImages[1]);
        hand.add(cardImages[2]);
        hand.add(cardImages[3]);
        hand.add(cardImages[4]);
        hand.add(cardImages[5]);
        hand.add(cardImages[6]);
        hand.add(cardImages[7]);
        hand.add(cardImages[8]);
        hand.add(cardImages[9]);
        hand.add(cardImages[10]);

        handView.setHand(hand);

        tradeView.setCard1Bean(cardImages[10]);
        tradeView.setCard2Bean(cardImages[0]);
        tradeView.setActiveCard(1);

        bottomLayout.setBackgroundColor(Color.rgb(45, 45, 45));
        //use to get coins: state.getPlayerList()[0].getCoins();

    }
}

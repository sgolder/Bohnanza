package edu.up.cs301.bohnanza;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.infoMsg.GameInfo;


/**
 * Displays the game to a human player on the GUI.
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class BohnanzaHumanPlayer extends GameHumanPlayer {
    //instance variables
    protected BohnanzaState state;
    private Activity myActivity;
    private PlayerView player1View;
    private PlayerView player2View;
    private PlayerView player3View;
    private PlayerView player4View;
    private HandView handView;
    private TradeView tradeView;
    private LinearLayout bottomLayout;
    private BohnanzaListener myListener;
    private Button harvest;
    private Button button2;
    private Button button3;
    private Button button4;
    private Toast toast;

    /**
     * constructor
     *
     * @param name the name of the player
     */
    public BohnanzaHumanPlayer(String name) {
        super(name);
    }

    /**
     * Used to initialize GUI objects
     *
     * @param activity the main activity of the game
     */
    public void setAsGui(GameMainActivity activity) {
        myActivity = activity;

        activity.setContentView(R.layout.bohnanza_human_player);

        //initialize surface views
        player1View = (PlayerView) myActivity.findViewById(R.id.surfaceViewPlayer1);
        player2View = (PlayerView)myActivity.findViewById(R.id.surfaceViewPlayer2);
        player3View = (PlayerView)myActivity.findViewById(R.id.surfaceViewPlayer3);
        player4View = (PlayerView)myActivity.findViewById(R.id.surfaceViewPlayer4);
        handView = (HandView)myActivity.findViewById(R.id.surfaceViewHand);
        tradeView = (TradeView)myActivity.findViewById(R.id.surfaceViewTrade);

        bottomLayout = (LinearLayout)myActivity.findViewById(R.id.BottomLinearLayout);

        //call listener to listen for actions the user takes
        myListener = new BohnanzaListener(state, playerNum, player1View, player2View,
                player3View, player4View, handView, tradeView, this, game, toast);

        //initialize buttons
        harvest = (Button)myActivity.findViewById(R.id.buttonHarvest);
        button2 = (Button)myActivity.findViewById(R.id.button2);
        button3 = (Button)myActivity.findViewById(R.id.button3);
        button4 = (Button)myActivity.findViewById(R.id.button4);

        //connect objects to listeners so that we receive information when a user touches them
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


        Card.initImages(activity); //initialize all card bitmaps
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
        drawGUI(); //update gui after state has been received
        myListener.setState( state );
        myListener.setGame( game );
    }

    /**
     * draws the gui that the human player will see
     *
     */
    public void drawGUI() {
        if(state == null) return; //return if we have not yet received the game state

        setPopups();

        int[] delete = {0};
        Card getCards = new Card(0, "", delete, 0);
        Bitmap[] cardImages = getCards.getCardImages(); //get bitmaps of each card

        updatePlayerViews(cardImages);//draw the fields and hand for each user

        updateHandView(cardImages); //draw the hand for the current user

        updateTradeView(cardImages); //draw the trade pile

        updateButtons(); //correctly show buttons depending on state of game

        //set white part of screen to have same background as surface views
        bottomLayout.setBackgroundColor(Color.rgb(45, 45, 45));

    }

    /**
     * displays popups as instructions for the user depending on the state of the game
     *
     */
    public void setPopups() {
        //popup that alerts the player that trading has started
        Context context = myActivity.getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        CharSequence message;
        if(toast != null) {
            toast.cancel();
        }

        if(myListener.getHarvest()){
            message = "Select the field to harvest";
            toast = Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.BOTTOM| Gravity.START, 0, 0);
            toast.show();
            return;
        }
        if(myListener.getMakeOffer()){
            message = "Select which bean you would like to offer";
            toast = Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.BOTTOM| Gravity.START, 0, 0);
            toast.show();
            return;
        }

        //Initial planting phase
        //Player has to plant one bean from their hand
        if(state.getPhase()==-1){
            message = "Please plant a bean from your hand";
            toast = Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.BOTTOM| Gravity.START, 0, 0);
            toast.show();
        }
        //Begin turn and plant initial beans
        else if(state.getPhase()==0){
            message = "Turn two cards or plant another bean";
            toast = Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.BOTTOM| Gravity.START, 0, 0);
            toast.show();
        }
        //Turn over two cards and decide to trade or plant
        else if(state.getPhase()==1){
            message = "Plant your new beans or enter trading";
            toast = Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.BOTTOM| Gravity.START, 0, 0);
            toast.show();
        }
        //Alerts the player that trading has started
        else if(state.getPhase()==2){
            message = "Trading has started";
            toast = Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.BOTTOM| Gravity.START, 0, 0);
            toast.show();
        }
    }

    /**
     * draw the bean fields and player hands for each player
     *
     * @param cardImages
     * 		bitmap of each card in Bohnanza
     */
    private void updatePlayerViews(Bitmap[] cardImages) {
        //set player colors
        int player1Color = Color.rgb(37, 127, 37);
        int player2Color = Color.rgb(220, 102, 30);
        int player3Color = Color.rgb(7, 169, 207);
        int player4Color = Color.rgb(248, 93, 89);
        int[] playerColors = {player1Color, player2Color, player3Color, player4Color};

        PlayerView[] playerViews = {player1View, player2View, player3View, player4View};

        //set bean information for each players fields
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

        //draw the information for each player: coins, name, hand, fields
        for(int i = 0; i<4; i++) {
            playerViews[i].setPlayerColor(playerColors[i]);
            playerViews[i].setPlayerName("Player " +(i+1));

            //draw fields
            playerViews[i].setField1Bean(cardImages[beanIdx[i][0]], numBeans[i][0]);
            playerViews[i].setField2Bean(cardImages[beanIdx[i][1]], numBeans[i][1]);
            playerViews[i].setField3Bean(cardImages[beanIdx[i][2]], numBeans[i][2]);

            //draw hands
            int[] handIdx = new int[state.getPlayerList()[i].getHand().size()];
            ArrayList<Bitmap> hand = new ArrayList<>();
            for(int j = 0; j<state.getPlayerList()[i].getHand().size(); j++) {
                handIdx[j] = state.getPlayerList()[i].getHand().getCards().get(j).getBeanIdx();
                hand.add(cardImages[handIdx[j]]);
            }
            playerViews[i].setHandCards(hand);

            //set information about player
            playerViews[i].setCoins(state.getPlayerList()[i].getCoins());
            playerViews[i].setThirdField(state.getPlayerList()[i].getHasThirdField());
            playerViews[i].setPhase(state.getPhase());
            playerViews[i].setOffer(state.getPlayerList()[i].getMakeOffer());
            if(state.getTurn() == i) {
                playerViews[i].setPhase(0);
            }
            else {
                playerViews[i].setPhase(state.getPhase());
            }
            if(state.getPlayerList()[i].getOffer() != null) {
                playerViews[i].setCardOffer(cardImages[state.getPlayerList()[i].getOffer().getBeanIdx()]);
            }
            playerViews[i].invalidate(); //redraw after all information is set
        }
    }

    /**
     * draw the hand of the human player
     *
     * @param cardImages
     * 		bitmap of each card in Bohnanza
     */
    private void updateHandView(Bitmap[] cardImages) {
        ArrayList<Bitmap> hand = new ArrayList<>();
        for(int i = 0; i<state.getPlayerList()[playerNum].getHand().size(); i++) {
            hand.add(cardImages[state.getPlayerList()[playerNum].getHand().getCards().get(i).getBeanIdx()]);
        }
        handView.setHand(hand);
        handView.invalidate(); //redraw after all information is set
    }

    /**
     * draw the trade pile for the current state of the game
     *
     * @param cardImages
     * 		bitmap of each card in Bohnanza
     */
    private void updateTradeView(Bitmap[] cardImages) {
        //set trading cards based on how many cards are in trading deck
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
        tradeView.setDeck(state.getTimesThroughDeck(), state.getMainDeck().size());
    }

    /**
     * Only display buttons that are needed for the current state of the game
     *
     */
    private void updateButtons() {
        /**
         External Citation
         Date:      6 March 2018
         Problem:   Did not know how to set the visibility of buttons and change the text
         Resource:  https://stackoverflow.com/questions/4127725/
         how-can-i-remove-a-button-or-make-it-invisible-in-android
         Solution:  Used an example from this post
         */

        //set the buttons the user is able to see based on stage of the game
        //planting first card, only button available is harvest
        if(state.getPhase() == -1) {
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
        //option to turn to cards
        else if(state.getPhase() == 0 && state.getTurn() == playerNum) {
            button2.setText("Turn 2 Cards");
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
        //only option is to harvest because it is not their turn
        else if(state.getPhase() == 0 && state.getTurn() != playerNum) {
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
        //player can end their turn
        else if(state.getPhase() == 1 && state.getTradeDeck().getCards().size() == 0 &&
                state.getTurn() == playerNum) {
            button2.setText("Draw 3 Cards");
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
            state.setPhase(2);
        }
        //only option is to harvest becasue it is not their turn
        else if(state.getPhase() == 1 && state.getTradeDeck().getCards().size() == 0 &&
                state.getTurn() != playerNum) {
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
            state.setPhase(2);
        }
        //player can start trading
        else if(state.getPhase() == 1 && state.getTurn() == playerNum) {
            button2.setText("Start Trading");
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
        //only option is to harvest because it is not their turn
        else if(state.getPhase() == 1 && state.getTurn() != playerNum) {
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
        //player can end their turn
        else if(state.getPhase() == 2 && state.getTurn() == playerNum) {
            button2.setText("Draw 3 Cards");
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
        //player can make an offer or choose to not trade
        else if(state.getPhase() == 2 && state.getTurn() != playerNum) {
            button2.setText("Make Offer");
            button3.setText("Pass");
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
    }

    //Getters
    public int getPlayerIndex() { return playerNum; }
}

package edu.up.cs301.bohnanza;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceView;
import java.util.ArrayList;

/**
 * Draws the information of each player on a corresponding SurfaceView
 *
 * @autor Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class PlayerView extends SurfaceView {
    //instance variables
    private int coins;
    private Rect field1;
    private Rect field2;
    private Rect field3;
    private Rect rectOffer;
    private int width;
    private int height;
    private Bitmap field1Bean;
    private Bitmap field2Bean;
    private Bitmap field3Bean;
    private int numField1Bean;
    private int numField2Bean;
    private int numField3Bean;
    private ArrayList<Bitmap> handCards = new ArrayList<>();
    private int backGroundColor;
    private String playerName = "";
    private Boolean thirdField = false;
    private int phase = 0;
    private int offer = 0;
    private Bitmap cardOffer;
    private Bitmap accept;
    private Bitmap reject;
    private Bitmap turnIndicator;
    private Rect acceptRect;
    private Rect rejectRect;
    private Rect turnRect;
    private boolean turn;
    private Bitmap toPlant1;
    private Bitmap toPlant2;

    /** constructor */
    public PlayerView(Context context) {
        super(context);
        setWillNotDraw(false);
        setupFields();
    }

    /** required constructor for SurfaceView*/
    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        setupFields();
    }

    /** required constructor for SurfaceView*/
    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        setupFields();
    }

    /**
     * set the rectangles the card for each field will be drawn in
     */
    public void setupFields() {
        field1 = new Rect(3*width/10, 1*height/20+30, 5*width/10, 5*height/20+30);
        field2 = new Rect(3*width/10, 6*height/20+20, 5*width/10, 10*height/20+20);
        field3 = new Rect(3*width/10, 11*height/20+10, 5*width/10, 15*height/20+10);
        rectOffer = new Rect(2*width/5, 16*height/20-5, 3*width/5, height-5);
    }

    //setters
    public void setCoins (int initCoins) {
        coins = initCoins;
    }
    public void setField1Bean(Bitmap bean, int num) {
        field1Bean = bean;
        numField1Bean = num;
    }
    public void setField2Bean(Bitmap bean, int num) {
        field2Bean = bean;
        numField2Bean = num;
    }
    public void setField3Bean(Bitmap bean, int num) {
        field3Bean = bean;
        numField3Bean = num;
    }
    public void setHandCards(ArrayList<Bitmap> initHand) {
        handCards = initHand;
    }
    public void setPlayerColor(int initBackGround) {
        backGroundColor = initBackGround;
    }
    public void setPlayerName (String initName) {
        playerName = initName;
    }
    public void setThirdField (Boolean initThirdField) {
        thirdField = initThirdField;
    }
    public void setPhase(int initPhase) {
        phase = initPhase;
    }
    public void setOffer(int initOffer) {
        offer = initOffer;
    }
    public void setTurn(Boolean initTurn) {
        turn = initTurn;
    }
    public void setCardOffer(Bitmap initCardOffer) {
        cardOffer = initCardOffer;
        invalidate();
    }
    public void setImages(Bitmap initAccept, Bitmap initReject, Bitmap initTurnIndicator) {
        accept = initAccept;
        reject = initReject;
        turnIndicator = initTurnIndicator;
        acceptRect = new Rect(width/10, 17*height/20-5, 3*width/10-10, 19*height/20-15);
        rejectRect = new Rect(7*width/10+5, 17*height/20-5, 9*width/10-5, 19*height/20-15);
        turnRect = new Rect(width-150, 100, width, 250);
    }
    public void setToPlant(Bitmap initToPlant1, Bitmap initToPlant2) {
        toPlant1 = initToPlant1;
        toPlant2 = initToPlant2;
    }


    /**
     * draws on the surface view
     *
     * @param canvas
     *      the canvas to draw on
     */
    @Override
    public void onDraw(Canvas canvas) {
        width = canvas.getWidth();
        height = canvas.getHeight();
        setupFields();

        //fill background with dark gray
        canvas.drawColor(Color.rgb(45, 45, 45));

        //initialize paint for the corresponding player and draw border on surface view
        Paint playerPaint = new Paint();
        playerPaint.setColor(backGroundColor);
        playerPaint.setStyle(Paint.Style.STROKE);
        playerPaint.setStrokeWidth(8);
        canvas.drawRect(0, 0, width, height, playerPaint);

        //separate fields
        playerPaint.setStrokeWidth(5.0f);
        canvas.drawLine(0, height/20+20, width, height/20+20, playerPaint);
        canvas.drawLine(0, 11*height/40+25, width, 11*height/40+25, playerPaint);
        canvas.drawLine(0, 21*height/40+15, width, 21*height/40+15, playerPaint);
        canvas.drawLine(0, 3*height/4+25, width, 3*height/4+25, playerPaint);

        //draw coin
        Paint yellowPaint = new Paint();
        yellowPaint.setColor(Color.rgb(252, 255, 102));
        canvas.drawCircle(width-40, 32, 25, yellowPaint);

        //draw turn indicator if necessary
        if(turn) {
            canvas.drawBitmap(turnIndicator, null, turnRect, null);
        }

        //draw number on coin to represent number of coins the player has
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        if(coins < 10) {
            canvas.drawText(""+coins, width-51, 47, textPaint);
        }
        else if(coins >= 10) {
            canvas.drawText("" + coins, width-63, 47, textPaint);
        }

        //draw player name
        textPaint.setColor(backGroundColor);
        textPaint.setTextSize(50);
        canvas.drawText(playerName, 10, 47, textPaint);
        textPaint.setTextSize(75);

        drawFields(canvas, textPaint); //draw the fields of the player

        drawHand(canvas, textPaint); //draw the hand of the player

        //draw border around player with player color
        playerPaint.setColor(backGroundColor);
        playerPaint.setStyle(Paint.Style.STROKE);
        playerPaint.setStrokeWidth(8);
        canvas.drawRect(0, 0, width, height, playerPaint);
    }

    /**
     * draws on the surface view
     *
     * @param canvas the canvas to draw on
     * @param textPaint the paint to paint with
     */
    private void drawFields(Canvas canvas, Paint textPaint) {
        //draw information for field 1
        if(numField1Bean != 0) {
            canvas.drawBitmap(field1Bean, null, field1, null);
            canvas.drawText(""+numField1Bean, width/2+40, 4*height/20, textPaint);
        }
        //draw information for field 2
        if(numField2Bean != 0) {
            canvas.drawBitmap(field2Bean, null, field2, null);
            canvas.drawText(""+numField2Bean, width/2+40, 9*height/20, textPaint);
        }
        //if user has not bought third bean field, give option to buy field
        if(!thirdField) {
            textPaint.setTextSize(40);
            textPaint.setColor(Color.rgb(252, 255, 102));
            canvas.drawText("BUY THIRD BEAN FIELD", width/10-10, 13*height/20+15, textPaint);
        }
        else {
            //draw information for field 3
            if(numField3Bean != 0) {
                canvas.drawBitmap(field3Bean, null, field3, null);
                canvas.drawText("" + numField3Bean, width / 2 + 40, 14 * height / 20, textPaint);
            }
        }
    }

    /**
     * draws on the surface view
     *
     * @param canvas the canvas to draw on
     * @param textPaint the paint to paint with
     */
    private void drawHand(Canvas canvas, Paint textPaint) {
        Paint acceptPaint = new Paint();
        acceptPaint.setStyle(Paint.Style.STROKE);
        acceptPaint.setStrokeWidth(5);
        acceptPaint.setColor(Color.rgb(98, 195, 112));
        Paint rejectPaint = new Paint();
        rejectPaint.setStyle(Paint.Style.STROKE);
        rejectPaint.setStrokeWidth(5);
        rejectPaint.setColor(Color.rgb(238, 96, 85));

        if(toPlant2 != null) {
            canvas.drawBitmap(toPlant2, null,
                    new Rect(4*width/10, 16*height/20-5, 6*width/10, height-5), null);
        }
        if(toPlant1 != null) {
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(45);
            canvas.drawText("PLANT", width/10, 18*height/20, textPaint);
            canvas.drawBitmap(toPlant1, null,
                    new Rect(5*width/10, 16*height/20-5, 7*width/10, height-5), null);
            return;
        }
        //draw the first 8 cards of the hand
        if(handCards.size() != 0 && phase != 2) {
            for(int i = handCards.size()-1; i>=0; i--){
                if(i<8) {
                    canvas.drawBitmap(handCards.get(i), null,
                            new Rect((8 - i) * width / 10, 16 * height / 20 - 5,
                                    (8 - i + 2) * width / 10 - 10, height - 5), null);
                }
            }
        }
        //if the user has more than 8 cards, draw the remaining cards below the first 8
        if(handCards.size() != 0 && phase != 2) {
            for(int i = handCards.size()-1; i>=0; i--){
                if(i>=8) {
                    canvas.drawBitmap(handCards.get(i), null,
                            new Rect((8-(i-8))*width/10-30, 18*height/20-5,
                                    (8-(i-8)+2)*width/10-40,22*height/20-5), null);
                }
            }
        }
        //if trading phase, remove hands to leave space for offers
        else if(phase == 2) {
            if(offer == 1) {
                textPaint.setTextSize(50);
                textPaint.setColor(Color.WHITE);
                canvas.drawText("NOT TRADING", width/5, 18*height/20+15, textPaint);
            }
            else if(offer == 2) {
                if(cardOffer != null) {
                    canvas.drawBitmap(cardOffer, null, rectOffer, null);
                    canvas.drawCircle(2*width/10, 18*height/20-10, width/10+10, acceptPaint);
                    canvas.drawCircle(8*width/10, 18*height/20-10, width/10+10, rejectPaint);
                    canvas.drawBitmap(accept, null, acceptRect, null);
                    canvas.drawBitmap(reject, null, rejectRect, null);
                }
            }
        }
    }
}

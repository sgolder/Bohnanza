package edu.up.cs301.bohnanza;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Draws the information of each player on a corresponding SurfaceView
 *
 * @autor Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class TradeView extends SurfaceView {
    //instance variables
    private Bitmap card1Bean = null;
    private Bitmap card2Bean = null;
    private int width;
    private int height;
    private Rect card1;
    private Rect card2;
    private int activeCard;
    private int deckCycle;
    private int numCards;

    /** constructor */
    public TradeView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    /** required constructor for SurfaceView*/
    public TradeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    /** required constructor for SurfaceView*/
    public TradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    //setters. Redraw after every set because TradeView setters get called
    //from various different places
    public void setCard1Bean(Bitmap initBean) {
        card1Bean = initBean;
        invalidate();
    }
    public void setCard2Bean(Bitmap initBean) {
        card2Bean = initBean;
        invalidate();
    }
    public void setActiveCard(int initActiveCard) {
        activeCard = initActiveCard;
        invalidate();
    }
    public void setDeck(int initCycle, int initNumCards) {
        deckCycle = initCycle+1;
        numCards = initNumCards;
        invalidate();
    }

    /**
     * set the rectangles the card for each field will be drawn in
     */
    public void setupCards() {
        card1 = new Rect(2*width/10, height/10, 5*width/10, 9*height/10);
        card2 = new Rect(6*width/10, height/10, 9*width/10, 9*height/10);
    }

    //getters
    public Rect getCard1Rect() {return card1;}
    public Rect getCard2Rect() {return card2;}

    /**
     * draws on the surface view
     *
     * @param canvas the canvas to draw on
     */
    @Override
    public void onDraw(Canvas canvas) {
        width = canvas.getWidth();
        height = canvas.getHeight();
        setupCards();

        //draw the background of the surface view to be a dark gray
        canvas.drawColor(Color.rgb(45, 45, 45));

        Paint textPaint = new Paint();
        textPaint.setColor(Color.rgb(249, 249, 249));
        textPaint.setTextSize(80);
        //if there are no cards in the trade pile, display the number of cards
        //left in the deck, and the cycle that the deck is on
        if(card1Bean == null && card2Bean == null) {
            canvas.drawText(""+numCards, width/2-10, height/3+95, textPaint);
            textPaint.setTextSize(40);
            canvas.drawText("Cards Remaining", width/4+25, 2*height/3+5, textPaint);
            textPaint.setTextSize(55);
            canvas.drawText("Deck "+deckCycle, width/2-50, height/3+30, textPaint);

        }
        //draw first trade card
        if(card1Bean != null) {
            canvas.drawBitmap(card1Bean, null, card1, null);
        }
        //draw second trade card
        if(card2Bean != null) {
            canvas.drawBitmap(card2Bean, null, card2, null);
        }

        Paint selectPaint = new Paint();
        selectPaint.setColor(Color.rgb(252, 255, 102));
        selectPaint.setStyle(Paint.Style.STROKE);
        selectPaint.setStrokeWidth(5.0f);
        //draw an outline around the card the user is selecting to plant or trade
        if(activeCard == 1 && card1Bean != null) {
            canvas.drawRect(2*width/10-10, height/10-10, 5*width/10+10, 9*height/10+10, selectPaint);
        }
        else if(activeCard == 2 && card2Bean != null) {
            canvas.drawRect(6*width/10-10, height/10-10, 9*width/10+10, 9*height/10+10, selectPaint);
        }
    }
}

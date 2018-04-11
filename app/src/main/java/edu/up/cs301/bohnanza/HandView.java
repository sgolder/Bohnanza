package edu.up.cs301.bohnanza;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceView;
import java.util.ArrayList;

/**
 * Draws the information of each player on a corresponding SurfaceView
 *
 * @autor Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class HandView extends SurfaceView {
    //instance variables
    private ArrayList<Bitmap> hand = new ArrayList<>();
    private ArrayList<RectF> cards = new ArrayList<>();

    /** constructor */
    public HandView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    /** required constructor for SurfaceView*/
    public HandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    /** required constructor for SurfaceView*/
    public HandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    //setter
    public void setHand(ArrayList<Bitmap> initHand) { hand = initHand; }
    //getter
    public ArrayList<RectF> getCardPositions() {return cards;}

    /**
     * draws on the surface view
     *
     * @param canvas the canvas to draw on
     */
    @Override
    public void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawColor(Color.rgb(45, 45, 45));
        cards.clear();
        //draw each card in hand to the surface view and create a corresponding
        //rectangle in an ArrayList to be accessed by Bohnanza Listener
        if(hand.size() != 0) {
            for(int i = hand.size()-1; i>=0; i--){
                canvas.drawBitmap(hand.get(i), null,
                        new Rect((10 - i) * width / 12 + 20, height / 7,
                                (10 - i + 2) * width / 12, height), null);
                cards.add(0, new RectF((10 - i) * width / 12 + 20, height / 7,
                        (10 - i + 2) * width / 12, height));
            }
        }
    }
}

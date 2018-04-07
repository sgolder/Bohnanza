package edu.up.cs301.bohnanza;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by AdamMercer on 3/29/18.
 */

public class HandView extends SurfaceView {
    private ArrayList<Bitmap> hand = new ArrayList<>();
    private ArrayList<RectF> cards = new ArrayList<>();

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

    public void setHand(ArrayList<Bitmap> initHand) {
        hand = initHand;
        invalidate();
    }
    public ArrayList<RectF> getCardPositions() {return cards;}

    @Override
    public void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawColor(Color.rgb(45, 45, 45));
        cards.clear();
        if(hand.size() != 0) {
            for(int i = hand.size()-1; i>=0; i--){
                canvas.drawBitmap(hand.get(i), null, new Rect((10-i)*width/12+20, height/7, (10-i+2)*width/12, height), null);
                cards.add(0, new RectF((10-i)*width/12+20, height/7, (10-i+2)*width/12, height));
            }
        }
    }
}

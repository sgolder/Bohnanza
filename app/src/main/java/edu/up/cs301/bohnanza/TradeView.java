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
 * Created by AdamMercer on 3/29/18.
 */

public class TradeView extends SurfaceView {
    private Bitmap card1Bean = null;
    private Bitmap card2Bean = null;
    private int width;
    private int height;
    private Rect card1;
    private Rect card2;
    private int activeCard;

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

    public void setCard1Bean(Bitmap initBean) {
        card1Bean = initBean;
    }

    public void setCard2Bean(Bitmap initBean) {
        card2Bean = initBean;
    }

    public void setActiveCard(int initActiveCard) {
        activeCard = initActiveCard;
    }

    public void setupCards() {
        card1 = new Rect(2*width/10, height/10, 5*width/10, 9*height/10);
        card2 = new Rect(6*width/10, height/10, 9*width/10, 9*height/10);
    }

    @Override
    public void onDraw(Canvas canvas) {
        width = canvas.getWidth();
        height = canvas.getHeight();
        setupCards();

        canvas.drawColor(Color.rgb(45, 45, 45));

        if(card1Bean != null) {
            canvas.drawBitmap(card1Bean, null, card1, null);
        }
        if(card2Bean != null) {
            canvas.drawBitmap(card2Bean, null, card2, null);
        }

        Paint selectPaint = new Paint();
        selectPaint.setColor(Color.rgb(252, 255, 102));
        selectPaint.setStyle(Paint.Style.STROKE);
        selectPaint.setStrokeWidth(5.0f);


        if(activeCard == 1) {
            canvas.drawRect(2*width/10-10, height/10-10, 5*width/10+10, 9*height/10+10, selectPaint);
        }
        else if(activeCard == 2) {
            canvas.drawRect(6*width/10-10, height/10-10, 9*width/10+10, 9*height/10+10, selectPaint);
        }
    }
}

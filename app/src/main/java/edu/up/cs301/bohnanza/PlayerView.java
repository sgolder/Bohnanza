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

import edu.up.cs301.animation.AnimationSurface;

/**
 * Created by AdamMercer on 3/29/18.
 */

public class PlayerView extends SurfaceView {
    private int coins;
    private Rect field1;
    private Rect field2;
    private Rect field3;
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

    public void setupFields() {
        field1 = new Rect(3*width/10, 1*height/20+30, 5*width/10, 5*height/20+30);
        field2 = new Rect(3*width/10, 6*height/20+20, 5*width/10, 10*height/20+20);
        field3 = new Rect(3*width/10, 11*height/20+10, 5*width/10, 15*height/20+10);

    }

    public void setCoins (int initCoins) {
        coins = initCoins;
        invalidate();
    }
    public void setField1Bean(Bitmap bean, int num) {
        field1Bean = bean;
        numField1Bean = num;
        invalidate();
    }
    public void setField2Bean(Bitmap bean, int num) {
        field2Bean = bean;
        numField2Bean = num;
        invalidate();
    }
    public void setField3Bean(Bitmap bean, int num) {
        field3Bean = bean;
        numField3Bean = num;
        invalidate();
    }
    public void setHandCards(ArrayList<Bitmap> initHand) {
        handCards = initHand;
        invalidate();
    }

    public void setBackGroundColor(int initBackGround) {
        backGroundColor = initBackGround;
        invalidate();
    }

    public void setPlayerName (String initName) {
        playerName = initName;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        width = canvas.getWidth();
        height = canvas.getHeight();
        setupFields();

        canvas.drawColor(Color.rgb(45, 45, 45));

        Paint playerPaint = new Paint();
        playerPaint.setColor(backGroundColor);
        playerPaint.setStyle(Paint.Style.STROKE);
        playerPaint.setStrokeWidth(8.0f);
        canvas.drawRect(0, 0, width, height, playerPaint);

        playerPaint.setStrokeWidth(5.0f);
        canvas.drawLine(0, height/20+20, width, height/20+20, playerPaint);
        canvas.drawLine(0, 11*height/40+25, width, 11*height/40+25, playerPaint);
        canvas.drawLine(0, 21*height/40+15, width, 21*height/40+15, playerPaint);
        canvas.drawLine(0, 3*height/4+25, width, 3*height/4+25, playerPaint);

        Paint yellowPaint = new Paint();
        yellowPaint.setColor(Color.YELLOW);
        canvas.drawCircle(375, 30, 25, yellowPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText(""+coins, 352, 45, textPaint);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50);
        canvas.drawText(playerName, 145, 50, textPaint);
        textPaint.setTextSize(75);

        if(numField1Bean != 0) {
            canvas.drawBitmap(field1Bean, null, field1, null);
            canvas.drawText(""+numField1Bean, width/2+40, 4*height/20, textPaint);
        }
        if(numField2Bean != 0) {
            canvas.drawBitmap(field2Bean, null, field2, null);
            canvas.drawText(""+numField2Bean, width/2+40, 9*height/20, textPaint);
        }
        if(numField3Bean != 0) {
            canvas.drawBitmap(field3Bean, null, field3, null);
            canvas.drawText(""+numField3Bean, width/2+40, 14*height/20, textPaint);
        }

        if(handCards.size() != 0) {
            for(int i = 0; i<handCards.size(); i++){
                canvas.drawBitmap(handCards.get(i), null, new Rect(10+i*width/10, 16*height/20-5, (i+2)*width/10, height-5), null);
            }
        }
    }
}

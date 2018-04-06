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

        surface = (AnimationSurface) myActivity.findViewById(R.id.animation_surface);
        surface.setAnimator(this);

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

    public void baseLayout(Canvas g) {
        Paint playerOne = new Paint();
        playerOne.setColor(Color.rgb(37, 127, 37));
        playerOne.setStyle(Paint.Style.STROKE);
        playerOne.setStrokeWidth(8.0f);

        Paint playerTwo = new Paint();
        playerTwo.setColor(Color.rgb(7, 169, 207));
        playerTwo.setStyle(Paint.Style.STROKE);
        playerTwo.setStrokeWidth(8.0f);

        Paint playerThree = new Paint();
        playerThree.setColor(Color.rgb(220, 102, 30));
        playerThree.setStyle(Paint.Style.STROKE);
        playerThree.setStrokeWidth(8.0f);

        Paint playerFour = new Paint();
        playerFour.setColor(Color.rgb(248, 93, 89));
        playerFour.setStyle(Paint.Style.STROKE);
        playerFour.setStrokeWidth(8.0f);

        //Player one
        g.drawRect(35, 100, 515, 900, playerOne);
        g.drawLine(50, 200, 500, 200, playerOne); // Name
        g.drawLine(50, 375, 500, 375, playerOne); // Field 1
        g.drawLine(50, 550, 500, 550, playerOne); // Field 2
        g.drawLine(50, 725, 500, 725, playerOne); // Field 3

        g.drawRect(535, 100, 1015, 900, playerTwo);
        g.drawLine(550, 200, 1000, 200, playerTwo); // Name
        g.drawLine(550, 375, 1000, 375, playerTwo); // Field 1
        g.drawLine(550, 550, 1000, 550, playerTwo); // Field 2
        g.drawLine(550, 725, 1000, 725, playerTwo); // Field 3

        g.drawRect(1035, 100, 1515, 900, playerThree);
        g.drawLine(1050, 200, 1500, 200, playerThree); // Name
        g.drawLine(1050, 375, 1500, 375, playerThree); // Field 1
        g.drawLine(1050, 550, 1500, 550, playerThree); // Field 2
        g.drawLine(1050, 725, 1500, 725, playerThree); // Field 3

        g.drawRect(1535, 100, 2015, 900, playerFour);
        g.drawLine(1550, 200, 2000, 200, playerFour); // Name
        g.drawLine(1550, 375, 2000, 375, playerFour); // Field 1
        g.drawLine(1550, 550, 2000, 550, playerFour); // Field 2
        g.drawLine(1550, 725, 2000, 725, playerFour); // Field 3

        int cardIdx = R.drawable.card4_cocoa;
        Bitmap card = BitmapFactory.decodeResource(
                myActivity.getResources(), cardIdx);

        // create the source rectangle
        Rect r = new Rect(0,0,card.getWidth(),card.getHeight());

        // draw the bitmap into the target rectangle
        RectF rectF = new RectF(55, 220, 150, 355);
        g.drawBitmap(card, r, rectF, null);

        playerOne.setTextSize(50.0f);
        playerOne.setStrokeWidth(2.0f);
        playerOne.setStyle(Paint.Style.FILL);
        g.drawText("Cocoa Bean", 180, 280, playerOne);
        g.drawText("x7", 180, 340, playerOne);


    }
}

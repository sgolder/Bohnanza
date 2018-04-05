package edu.up.cs301.bohnanza;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.up.cs301.game.R;

/**
 * Creates a new card which is identified by a string
 *
 * @autor Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class Card {
    private String beanName;

    public Card(String name){
        beanName = name;
    }

    public Card(Card orig){
        beanName = orig.beanName;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Card)) return false;
        Card c = (Card)other;
        return this.beanName.equals(c.beanName);
    }

    // array that contains the android resource indices for the 52 card
    // images
    private static int[] resIdx =
            {
                    R.drawable.card4_cocoa, R.drawable.card6_garden,
                    R.drawable.card8_red, R.drawable.card10_blackeyed,
                    R.drawable.card12_soy, R.drawable.card14_green,
                    R.drawable.card16_stink, R.drawable.card18_chili,
                    R.drawable.card20_blue, R.drawable.card22_wax,
                    R.drawable.card24_coffee,
            };

    // the array of card images
    private static Bitmap[] cardImages = null;

    /**
     * initializes the card images
     *
     * @param activity
     * 		the current activity
     */
    public static void initImages(Activity activity) {
        // if it's already initialized, then ignore
        if (cardImages != null) return;

        // create the outer array
        cardImages = new Bitmap[resIdx.length];

        // loop through the resource-index array, creating a
        // "parallel" array with the images themselves
        for (int i = 0; i < resIdx.length; i++) {
            // create an inner array
            cardImages[i] = BitmapFactory.decodeResource(
                    activity.getResources(), resIdx[i]);
        }
    }
    public void setBeanName(String newName) { beanName = newName; }

    public String getBeanName() { return beanName; }

    public Bitmap[] getCardImages() { return cardImages; }
}

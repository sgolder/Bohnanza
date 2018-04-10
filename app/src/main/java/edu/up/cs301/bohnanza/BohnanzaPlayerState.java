package edu.up.cs301.bohnanza;

import android.util.Log;

/**
 * Player state for a given player in Bohnanza
 *
 * @autor Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class BohnanzaPlayerState {
    private String name;
    private int coins;
    private Deck[] fields = new Deck[3];
    private Deck hand;
    private Deck toPlant;
    private boolean hasThirdField;
    private int makeOffer;
    private Card offer = null;

    public BohnanzaPlayerState(String playerName) {
        name = playerName;
        hand = new Deck();
        coins = 0;
        hasThirdField = false;
        makeOffer = 0;
        for( int i = 0; i<3; i++ ){
            fields[i] = new Deck();
        }
        toPlant = new Deck();
    }

    /**
     * Deep copy constructor of BohnanzaPlayerState
     *
     */
    public BohnanzaPlayerState(BohnanzaPlayerState orig){
        name =  orig.name;
        coins = orig.coins;
        hand = new Deck(orig.hand);
        hasThirdField = orig.hasThirdField;
        makeOffer = orig.makeOffer;
        for( int i = 0; i<3; i++) {
            fields[i] = new Deck(orig.fields[i]);
        }
    }

    //Getter methods
    public Deck getField(int field) { return fields[field]; }
    public Deck[] getAllFields() {return fields; }
    public int getCoins() {return coins;}
    public Deck getHand() {return hand;}
    public boolean getHasThirdField() {return hasThirdField;}
    public int getMakeOffer() {return makeOffer;}
    public Card getOffer() {return offer;}
    public Deck getToPlant() {return toPlant;}

    //setter methods
    public void setCoins (int newCoins) {
        coins =+ newCoins;
        Log.i("BohPlaSt, setCoins", "coins =="+coins);
    }
    public void setHasThirdField (boolean newHasThirdField) {
        hasThirdField = newHasThirdField;
    }
    public void setHand (Deck newHand) {hand = newHand;}

    //setMakeOffer 2 if user will make an offer, 1 if user will not
    // make an offer, 0 if they have not decided
    public void setMakeOffer(int newMakeOffer) {
        makeOffer = newMakeOffer;
    }
    public void setOffer(Card initoffer){
        offer = initoffer;
        /* Useful for if we make offer a Card[]
        for(int i = 0; i<offer.length; i++){
            hand.getCards().remove(i);
            hand.add(offer[i]);
        }
        */
    }

}

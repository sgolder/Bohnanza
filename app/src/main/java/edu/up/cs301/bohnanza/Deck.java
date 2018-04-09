package edu.up.cs301.bohnanza;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Creates a Deck by making a new ArrayList of cards
 *
 * @autor Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */


/**
 External Citation
 Date: 6 March 2018
 Problem: Needed various methods for deck class
 Resource:
 https://github.com/srvegdahl/SlapJack
 Solution: Used Professor Vengdahl's slapjack game framework to implement various
 methods in the deck class.
 */

public class Deck implements Serializable {

    private static final long serialVersionUID = 3216223171210121485L;

    private ArrayList<Card> cards;

    public Deck (){
        cards = new ArrayList<Card>();
    }

    public Deck(Deck orig) {
        // synchronize to ensure that original is not being modified as we
        // iterate over it
        synchronized(orig.cards) {
            // create a new arrayList for our new deck; add each card in it
            cards = new ArrayList<Card>();
            for (Card c: orig.cards) {
                cards.add(c);
            }
        }
    }

    //TODO: add method that accesses cards at specific index based on name

    /**
     * missing methods from UML:
     * public int size()
     *
     */
    /**
     * add a card to the top of a deck
     *
     * @param c
     * 		the card to add
     */
    public void add(Card c) {
        // synchronize so that the underlying ArrayList is not accessed
        // inconsistently
        synchronized(this.cards) {
            cards.add(c);
        }
    }

    /**
     * shuffles all the cards in a given deck
     *
     * @return Deck
     *      the deck that was shuffled
     */
    public Deck shuffle() {
        // go through a loop that randomly rearranges the cards
        for (int i = cards.size(); i > 1; i--) {
            int spot = (int)(i*Math.random());
            Card temp = cards.get(spot);
            cards.set(spot, cards.get(i-1));
            cards.set(i-1, temp);
        }
        // return the deck
        return this;
    }

    /**
     * Moves the top card the current deck to the top of another; does nothing if
     * the first deck is empty
     *
     * @param targetDeck
     * 		the deck to which the card should be moved
     */
    public void moveTopCardTo(Deck targetDeck) {

        // will hold the card
        Card c = null;

        // size of the first deck
        int size;

        // indivisibly check the deck for empty, and remove the card, to
        // avoid a race condition
        synchronized(this.cards) {
            size = this.size();
            if (size > 0) {
                c = cards.remove(cards.size()-1);
            }
        }

        // if the original size was non-zero, add the card to the top of the
        // target deck
        if (size > 0) {
            targetDeck.add(c);
        }
    }

    /**
     * Moves the top card the current deck to the top of another; does nothing if
     * the first deck is empty
     *
     * @param targetDeck
     * 		the deck to which the card should be moved
     */
    public void moveFirstCardTo(Deck targetDeck) {

        // will hold the card
        Card c = null;

        // size of the first deck
        int size;

        // indivisibly check the deck for empty, and remove the card, to
        // avoid a race condition
        synchronized(this.cards) {
            size = this.size();
            if (size > 0) {
                c = cards.remove(0);
            }
        }

        // if the original size was non-zero, add the card to the top of the
        // target deck
        if (size > 0) {
            targetDeck.add(c);
        }
    }

    /**
     * @return
     * 		the top card in the deck, without removing it; null
     * 		if the deck was empty
     */
    public Card peekAtTopCard() {
        synchronized (this.cards) {
            if (cards.isEmpty()) return null;
            return cards.get(cards.size()-1);
        }
    }

    public int size() {
        return cards.size();
    }

    //TODO: Make the new Card initialization include
    /**
     *  add all bohnanza cards to a the deck that method was called from
     *
     */
    public void addAllCards() {
        for(int i = 0; i < 20; i++) {
            int[] blueCoins = {4, 6, 8, 10};
            add(new Card(7, "Blue Bean", blueCoins, 20));
        }
        for(int i = 0; i < 18; i++) {
            int[] chiliCoins = {3, 6, 8, 9};
            add(new Card(6, "Chili Bean", chiliCoins, 18));
        }
        for(int i = 0; i < 16; i++) {
            int[] stinkCoins = {3, 5, 7, 8};
            add(new Card(5, "Stink Bean", stinkCoins, 16));
        }
        for(int i = 0; i < 14; i++) {
            int[] greenCoins = {3, 5, 6, 7};
            add(new Card(4, "Green Bean", greenCoins, 14));
        }
        for(int i = 0; i < 12; i++) {
            int[] soyCoins = {2, 4, 6, 7};
            add(new Card(3, "Soy Bean", soyCoins, 12));
        }
        for(int i = 0; i < 10; i++) {
            int[] blackEyedCoins = {2, 4, 5, 6};
            add(new Card(2, "Black-Eyed Bean", blackEyedCoins, 10));
        }
        for(int i = 0; i < 8; i++) {
            int[] redCoins = {2, 3, 4, 5};
            add(new Card(1, "Red Bean", redCoins, 8));
        }
        for(int i = 0; i <6; i++) {
            int[] gardenCoins = {-1, 2, 3, -1};
            add(new Card(0, "Garden Bean", gardenCoins, 6));
        }
    }

    public ArrayList<Card> getCards() { return cards; }

    public void turnHandOver() {
        int oldSize = size();
        cards.clear();
        for(int i = 0; i<oldSize; i++ ){
            int[] cardBackCoins = {-1, -1, -1, -1};
            cards.add(new Card(8, "CardBack", cardBackCoins, 0));
        }
    }

    /**
     *
     * @param initDeck
     * @return number of coins a field is worth
     *
     * index 0: 1 coin
     * index 1: 2 coins
     * index 2: 3 coins
     * index 3: 4 coins
     *
     */
    public int getFieldValue(Deck initDeck) {
        Card cardType = initDeck.peekAtTopCard();
        int[] coinCount = cardType.getCoinCount();

        if (coinCount[0] == -1) {
            //special case: garden bean
            if (initDeck.size() >= coinCount[2]) {
                return 3;
            } else if (initDeck.size() >= 2) {
                return 2;
            } else {
                return 0;
            }
        }
        else {
            for (int i = 4; i < 0; i++) {
                if (initDeck.size() >= coinCount[i]) {
                    return i + 1;

                }
            }
            return 0;
        }
    }

    @Override
    public String toString(){
        String beanList = "";
        for(int i = 0; i<size(); i++){
            //add commas between been card names
            if(i != size()-1) {
                beanList = beanList + cards.get(i).getBeanName() + ", ";
            }
            else {
                beanList = beanList + cards.get(i).getBeanName();
            }
        }
        return beanList;
    }

}

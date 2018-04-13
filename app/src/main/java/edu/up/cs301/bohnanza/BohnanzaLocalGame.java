package edu.up.cs301.bohnanza;

import android.util.Log;

import edu.up.cs301.actions.AbstainFromTrading;
import edu.up.cs301.actions.BuyThirdField;
import edu.up.cs301.actions.DrawThreeCards;
import edu.up.cs301.actions.HarvestField;
import edu.up.cs301.actions.MakeOffer;
import edu.up.cs301.actions.OfferResponse;
import edu.up.cs301.actions.PlantBean;
import edu.up.cs301.actions.StartTrading;
import edu.up.cs301.actions.TurnTwoCards;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * The LocalGame class for a Bohnanza game. Defines and enforces the
 * game rules; handles interactions between players.
 *
 * @author Adam Mercer, Reeca Bardon, Alyssa Arnaud, Sarah Golder
 */

public class BohnanzaLocalGame extends LocalGame {

    // The official game state
    BohnanzaState state;

    /**
     * Constructor for the BohnanzaLocalGame.
     */
    public BohnanzaLocalGame() {
        // create the state for the beginning of the game
        state = new BohnanzaState();
    }

    /**
     * Sends the updated state to the given player. In our case, we need to
     * make a copy of the Deck, and null out all the cards except the top card
     * in the middle deck, since that's the only one they can "see"
     *
     * @param p
     * 		the player to which the state is to be sent
     */
    protected void sendUpdatedStateTo(GamePlayer p) {
        // if there is no state to send, ignore
        if (state == null) {
            return;
        }

        // Figure out the ID of the player that was passed in
        int playerID = -1;
        if(p instanceof BohnanzaHumanPlayer) {
            BohnanzaHumanPlayer player = (BohnanzaHumanPlayer) p;
            playerID = player.getPlayerIndex();
        }
        else if(p instanceof BohnanzaComputerPlayer){
            BohnanzaComputerPlayer player = (BohnanzaComputerPlayer) p;
            playerID = player.getPlayerIndex();
        }

        // Create copy of the state using constructor that hides other
        // players' hands
        BohnanzaState stateForPlayer = new BohnanzaState(state, playerID);

        // send the modified copy of the state to the player
        p.sendInfo(stateForPlayer);
    }

    /**
     * whether a player is allowed to move
     *
     * @param playerIdx
     * 		the player-number of the player in question
     */
    protected boolean canMove(int playerIdx) {
        if( state.getPhase() == 2 || state.getTurn() == playerIdx){
            // can make a move during trading phase or during the users own turn
            return true;
        }
        return false;
    }

    /**
     * Checks whether the game is over; if so, returns a string giving the result
     *
     * @result
     * 		the end-of-game message, or null if the game is not over
     */
    protected String checkIfGameOver() {
        int maxCoins = 0;
        int winners = 0; //if winners > 1, there is a tie
        //check if we've been through the deck 1 time
        if( state.getTimesThroughDeck() == 1) {
            //find the maximum number of coins players have
            for(int i=0; i<4; i++) {
                if(state.getPlayerList()[i].getCoins() > maxCoins) {
                    maxCoins = state.getPlayerList()[i].getCoins();
                }

            }
            //check how many players have the maximum number of coins
            for(int i=0; i<4; i++) {
                if(state.getPlayerList()[i].getCoins() == maxCoins) {
                    maxCoins = state.getPlayerList()[i].getCoins();
                    winners++;
                }
            }
            //game is a draw
            if(winners > 1) {
                return "Game is a draw";
            }
            //announce winner
            if(winners == 1) {
                for(int i=0; i<4; i++) {
                    if (state.getPlayerList()[i].getCoins() == maxCoins) {
                        return "Player " + (i + 1) + " wins";
                    }
                }
            }
        }
        return null;
    }
    /**
     * makes a move on behalf of a player
     *
     * @param action
     * 		the action denoting the move to be made
     * @return
     * 		true if the move was legal; false otherwise
     */
    protected boolean makeMove(GameAction action) {
        Log.i("LocalGame, makeMove", "");
        int thisPlayerIdx = getPlayerIdx(action.getPlayer());

        // If the player is trying to buy a third field
        if(action instanceof BuyThirdField){
            if( !(buyThirdField(thisPlayerIdx)) ) {
                return false;
            }
        }
        // If the player is trying to plant a bean
        if(action instanceof PlantBean) {
            PlantBean plantBean = (PlantBean) action;
            Log.i("BLocal, makeMove", "Origin = "+plantBean.getOrigin());
            // Plant from hand
            if(plantBean.getOrigin() == 0){
                // Cannot keep planting from their hand endlessly
                // during the first phase or phase between turning
                // 2 cards and trading
                if( !(state.getPhase() == 0 || state.getPhase() == 1 ) ){
                    plantBean(thisPlayerIdx, plantBean.getField(),
                            state.getPlayerList()[thisPlayerIdx].getHand());
                }
            }
            // Plant first trading card
            else if (plantBean.getOrigin() == 1){
                // Create new deck to hold the desired trade card so
                // that the plantBean method plants intended bean
                Deck cardToTrade = new Deck();
                state.getTradeDeck().moveBottomCardTo(cardToTrade);
                // If unsuccessful, return card to the trade deck
                if(! (plantBean(thisPlayerIdx, plantBean.getField(), cardToTrade)) ) {
                    cardToTrade.moveBottomCardTo(state.getTradeDeck());
                    return false;
                }
            }
            // Plant second trading card
            else{
                // New deck for second card
                Deck cardToTrade = new Deck();
                state.getTradeDeck().moveTopCardTo(cardToTrade);
                // If unsuccessful, return card to the trade deck
                if(! (plantBean(thisPlayerIdx, plantBean.getField(), cardToTrade)) ) {
                    cardToTrade.moveBottomCardTo(state.getTradeDeck());
                    return false;
                }
            }
            sendAllUpdatedState();
            return true;
        }
        // If player wants to harvest a field
        if(action instanceof HarvestField){
            HarvestField harvestField = (HarvestField) action;
            harvestField(thisPlayerIdx, state.getPlayerList()[thisPlayerIdx].
                    getField(harvestField.getField()));
            sendAllUpdatedState();
            return true;
        }
        // Player wants to turn two trading cards
        if(action instanceof TurnTwoCards){
            turn2Cards(thisPlayerIdx);
            sendAllUpdatedState();
        }
        // Player wants to initiate trading phase
        if(action instanceof StartTrading){
            startTrading(thisPlayerIdx);
            sendAllUpdatedState();
        }
        // Player wants to make a trade offer
        if(action instanceof MakeOffer){
            MakeOffer makeOffer = (MakeOffer) action;
            makeOffer(thisPlayerIdx, makeOffer.getOffer());
            sendAllUpdatedState();
            return true;
        }
        // Player will not make an offer for highlighted trading card
        if(action instanceof AbstainFromTrading){
            abstainFromTrading(thisPlayerIdx);
            sendAllUpdatedState();
        }
        // Player wants to end turn
        if(action instanceof DrawThreeCards){
            //ending turn during phase 3, changes phase to 3
            draw3Cards(thisPlayerIdx);
            sendAllUpdatedState();
            return true;
        }
        // Player responding to another player's offer
        if(action instanceof OfferResponse){
            OfferResponse offerResponse = (OfferResponse) action;
            offerResponse(thisPlayerIdx, offerResponse.getTraderId(),
                    offerResponse.isAccept());
            sendAllUpdatedState();
            return true;
        }

        return false;
    }


    /**
     * purchase the third field
     */
    public boolean buyThirdField(int playerId) {
        if (state.getPlayerList()[playerId].getHasThirdField()) {
            // They already purchased the field
            return false;
        } else {
            if (state.getPlayerList()[playerId].getCoins() >= 3) {
                // They have enough coins to buy it and can now plant in it
                state.getPlayerList()[playerId].setHasThirdField(true);
                state.getPlayerList()[playerId].setCoins(state.getPlayerList()[playerId].getCoins() - 3);
                return true;
            } else {
                // Player doesn't have enough coins
                return false;
            }
        }
    }

    /**
     * plant a bean in a bean field
     */
    public boolean plantBean(int playerId, int fieldId,
                             Deck origin) {
        Log.i("LocalGame, plantBean", "PlayerId == "+playerId);

        //Check if player's turn
        if (state.getTurn() != playerId) {
            return false;
        }
        //Check if the origin deck has something to plant
        if (origin == null || origin.size() == 0) {
            return false;
        }
        //If the player has cards that were just traded, reset origin to toPlant
        if( !(state.getPlayerList()[playerId].getToPlant().getCards().isEmpty()) ) {
            origin = state.getPlayerList()[playerId].getToPlant();
        }
        //Plant if field is empty and/or purchased
        if (state.getPlayerList()[playerId].getField(fieldId).size() == 0) {
            if( fieldId == 2 &&
                    !(state.getPlayerList()[playerId].getHasThirdField())) {
                return false; // cannot plant if third field isn't purchased
            }
            origin.moveBottomCardTo(state.getPlayerList()[playerId].getField(fieldId));
            if( state.getPhase() == -1 ) state.setPhase(0);
            return true;
        }
        //check if card to be planted is the same as current bean in the field
        else if (state.getPlayerList()[playerId].getField(fieldId).peekAtTopCard().equals
                (origin.peekAtBottomCard())) {
            origin.moveBottomCardTo(state.getPlayerList()[playerId].getField(fieldId));
            if( state.getPhase() == -1 ) state.setPhase(0);
            return true;
        }
        return false;
    }

    /**
     * Harvest beans in a bean field
     */
    public boolean harvestField(int playerId, Deck field) {
        if (field.size() == 0) {
            return false;
        }
        else{
            state.getPlayerList()[playerId].setCoins(field.getFieldValue(field));
            for(int i=0; i<field.getFieldValue(field); i++){
                field.getCards().remove(i);
            }
            field.moveAllCardsTo(state.getDiscardDeck());
            return true;
        }
    }

    /**
     * turn over two cards that are up for trading
     */
    public boolean turn2Cards(int playerId) {
        if (state.getTurn() != playerId) {
            return false;
        }
        if(!state.getTradeDeck().getCards().isEmpty()){
            return false;
        }
        //move top two cards to trade deck
        if(state.getMainDeck().getCards().size() < 2 ){
            if(!(state.getTimesThroughDeck() < 3)){
                return false;
            }
            state.getDiscardDeck().moveAllCardsTo(state.getMainDeck());
            state.setTimesThroughDeck();
        }
        state.getMainDeck().moveTopCardTo(state.getTradeDeck());
        state.getMainDeck().moveTopCardTo(state.getTradeDeck());
        Log.i("BLocalGame, turn2", "phase ==" +state.getPhase());
        state.setPhase(1); //phase 1 starts now
        return true;
    }

    /**
     * start trading phase
     */
    public boolean startTrading(int playerId) {
        if (state.getTurn() != playerId || state.getPhase() != 1) {
            return false;
        }
        state.setPhase(2);
        return true;
    }

    /**
     * Allow player to make an offer
     */
    public boolean makeOffer(int traderId, int offer) {
        if (state.getPhase() != 2) {
            return false;
        }
        state.getPlayerList()[traderId].setMakeOffer(2); //user will trade
        state.getPlayerList()[traderId].setOffer(offer); //make traders offer cards visible
        Log.i("Local Game", "set offer");
        return true;
    }

    /**
     * Allow player to state that they will choose to not participate in trading
     */
    public boolean abstainFromTrading(int playerId) {
        if (state.getPhase() != 2) {
            return false;
        }
        state.getPlayerList()[playerId].setMakeOffer(1);
        return true;
    }

    /**
     * End turn by drawing 3 cards from main deck.
     */
    public boolean draw3Cards(int playerId) {
        if (state.getTurn() != playerId) {
            return false;
        }
        //trade deck empty, then change turn to +1 unless 3 then turn to 0
        if (state.getTradeDeck().size() == 0) {
            if(state.getMainDeck().getCards().size() < 3){
                if(!(state.getTimesThroughDeck() < 3)){
                    return false;
                }
                state.getDiscardDeck().moveAllCardsTo(state.getMainDeck());
                state.setTimesThroughDeck();
            }
            state.getMainDeck().moveTopCardTo(state.getPlayerList()[playerId].getHand());
            state.getMainDeck().moveTopCardTo(state.getPlayerList()[playerId].getHand());
            state.getMainDeck().moveTopCardTo(state.getPlayerList()[playerId].getHand());
            if (state.getTurn() == 3) {
                state.setTurn(0);
            } else {
                state.setTurn(state.getTurn() + 1);
            }
            state.setPhase( -1 );
            for(int i = 0; i < 4; i++) {
                state.getPlayerList()[i].setMakeOffer(0);
            }
            return true;
        } else {
            return false;
        }

    }

    /**
     * If a trade is accepted, move the traded cards into player's toPlant Decks
     */
    public boolean offerResponse(int playerId, int traderId, boolean accept) {
        if( state.getPhase() != 2 || !accept ||
                state.getPlayerList()[traderId].getMakeOffer() != 2) {
            return false;
        }
        BohnanzaPlayerState trader = state.getPlayerList()[traderId];
        for(int i = 0; i<trader.getHand().getCards().size(); i++) {
            if(trader.getHand().getCards().get(i) == trader.getOffer()) {
                // Give current player offered card
                Card offeredCard = trader.getHand().getCards().remove(i);
                state.getPlayerList()[playerId].getToPlant().add(offeredCard);
                // Give trader player card from trade deck
                state.getTradeDeck().moveBottomCardTo(trader.getToPlant());
            }
        }
        return false;
    }
}




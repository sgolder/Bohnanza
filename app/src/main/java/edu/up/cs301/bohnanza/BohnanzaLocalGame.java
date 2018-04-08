package edu.up.cs301.bohnanza;

import android.util.Log;

import edu.up.cs301.actions.AbstainFromTrading;
import edu.up.cs301.actions.BuyThirdField;
import edu.up.cs301.actions.DrawThreeCards;
import edu.up.cs301.actions.HarvestField;
import edu.up.cs301.actions.MakeOffer;
import edu.up.cs301.actions.PlantBean;
import edu.up.cs301.actions.StartTrading;
import edu.up.cs301.actions.TurnTwoCards;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by Toshiba on 4/2/2018.
 */

public class BohnanzaLocalGame extends LocalGame {

    BohnanzaState state;

    public BohnanzaLocalGame() {

        // create the state for the beginning of the game
        state = new BohnanzaState();

        //state.getPlayerList()[0].getHand().moveTopCardTo(
        //        state.getPlayerList()[0].getField(0));
    }

    protected void sendUpdatedStateTo(GamePlayer p) {
        // if there is no state to send, ignore
        if (state == null) {
            return;
        }

        // make a copy of the state; null out all cards except for the
        // top card in the middle deck

        BohnanzaState stateForPlayer = new BohnanzaState(state); // copy of state
        //stateForPlayer.hideDecks(); // put nulls except for visible card

        // send the modified copy of the state to the player
        p.sendInfo(stateForPlayer);
    }

    protected boolean canMove(int playerIdx) {
        //need to figure out this stuff
        return false;
    }

    protected String checkIfGameOver() {
        //check if we've been through the deck 3 times
        return null;
    }

    protected boolean makeMove(GameAction action) {
        Log.i("LocalGame, makeMove", "");
        int thisPlayerIdx = getPlayerIdx(action.getPlayer());

        if(action instanceof BuyThirdField){}
        if(action instanceof PlantBean) {
            // Initial planting in phase 0
            if( state.getPhase() == 0 ) {
                PlantBean plantBean = (PlantBean) action;
                plantBean(thisPlayerIdx, plantBean.getField(),
                        state.getPlayerList()[thisPlayerIdx].getHand());
            }
        }
        if(action instanceof HarvestField){}
        if(action instanceof TurnTwoCards){}
        if(action instanceof StartTrading){}
        if(action instanceof MakeOffer){}
        if(action instanceof AbstainFromTrading){}
        if(action instanceof DrawThreeCards){}


        return false;
    }


    /**
     * missing method:
     * public void sendAllUpdatedState()
     */
    public boolean buyThirdField(int playerId) {
        if (state.getPlayerList()[playerId].getHasThirdField()) {
            return false;
        } else {
            if (state.getPlayerList()[playerId].getCoins() >= 3) {
                state.getPlayerList()[playerId].setHasThirdField(true);
                state.getPlayerList()[playerId].setCoins(state.getPlayerList()[playerId].getCoins() - 3);
                return true;
            } else {
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
        if (origin.size() == 0) {
            return false;
        }
        if (state.getPlayerList()[playerId].getField(fieldId).size() == 0) {
            origin.moveTopCardTo(state.getPlayerList()[playerId].getField(fieldId));
            return true;
        }
        //check if card to be planted is the same as current bean in the field
        else if (state.getPlayerList()[playerId].getField(fieldId).peekAtTopCard().equals
                (origin.peekAtTopCard())) {
            origin.moveTopCardTo(state.getPlayerList()[playerId].getField(fieldId));
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
        field.getCards().clear();
        return true;
    }

    /**
     * turn over two cards that are up for trading
     */
    public boolean turn2Cards(int playerId) {
        if (state.getTurn() != playerId) {
            return false;
        }
        if (state.getMainDeck().size() < 2) {
            return false;
        }
        //move top two cards to trade deck
        state.getMainDeck().moveTopCardTo(state.getTradeDeck());
        state.getMainDeck().moveTopCardTo(state.getTradeDeck());
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
    public boolean makeOffer(int traderId, Card[] offer) {
        if (state.getPhase() != 2) {
            return false;
        }
        state.getPlayerList()[traderId].setMakeOffer(2); //user will trade
        state.getPlayerList()[traderId].setOffer(offer); //make traders offer cards visible
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
            state.getMainDeck().moveTopCardTo(state.getPlayerList()[playerId].getHand());
            state.getMainDeck().moveTopCardTo(state.getPlayerList()[playerId].getHand());
            state.getMainDeck().moveTopCardTo(state.getPlayerList()[playerId].getHand());
            if (state.getTurn() == 3) {
                state.setTurn(0);
            } else {
                state.setTurn(state.getTurn() + 1);

            }
            return true;
        } else {
            return false;
        }

    }

}




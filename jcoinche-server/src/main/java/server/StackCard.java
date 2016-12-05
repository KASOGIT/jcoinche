package server;

import common.Card;
import common.ClientInfo;

import java.util.ArrayList;

/**
 * Created by kaso on 25/11/16.
 */
public class StackCard {
    /**
     * PRIVATE ATTRIBUTES
     */
    private ArrayList<Card> _stack = new ArrayList<>();
    private ClientInfo _winnerTurn = null;

    /**
     * PUBLIC METHODS
     */

    public void pushCardStack(Card toPush) {
        if (_stack.isEmpty()) {
            _stack.add(toPush);
        }
        else {

        }
    }
}

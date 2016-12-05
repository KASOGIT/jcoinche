package common;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by kaso on 23/11/16.
 */
public class Deck {

    /**
     * PRIVATE STATIC ATTRIBUTES
     */

    private static final int NB_CARD_FAMILY = 8;
    private static final int NB_FAMILY = 4;
    private static final int[] equiv = {7, 8, 9, 10, 11, 12, 13, 1};

    /**
     * PRIVATE ATTRIBUTES
     */

    private LinkedList<Card> _deck = new LinkedList<>();

    /**
     * PUBLIC METHODS
     */

    public Deck() {
        generateDeck();
    }

    public LinkedList<Card> getDeck() {
        shuffleDeck();
        return (_deck);
    }

    /**
     * PRIVATE METHODS
     */

    public void generateDeck() {
        System.out.println("SERVER: DECK GENERATE AND SHUFFLE");
        for (int trump = 0; trump < NB_FAMILY; ++trump) {
            for (int id = 0; id < NB_CARD_FAMILY; ++id) {
                _deck.add(new Card(equiv[id], trump + 1));
            }
        }
    }

    private void shuffleDeck() {
        Collections.shuffle(_deck);
    }

    public int[] getEquiv() {
        return equiv;
    }
}

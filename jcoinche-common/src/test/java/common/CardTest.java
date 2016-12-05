package common;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kaso on 27/11/16.
 */
public class CardTest {

    private Card    testCard1 = new Card(7, 1);
    private Card    testCard2 = new Card(-1, -1);
    private Card    testCard3 = new Card(-1, 2);
    private Card    testCard4 = new Card(2, -1);

    @org.junit.Test
    public void getInfoCard() throws Exception {
        assertEquals(Card.InfoCard.SEVEN, testCard1.getInfoCard());
        assertEquals(Card.InfoCard.NONE, testCard2.getInfoCard());
        assertEquals(Card.InfoCard.NONE, testCard3.getInfoCard());
        assertEquals(Card.InfoCard.NONE, testCard4.getInfoCard());

    }

    @org.junit.Test
    public void getTrump() throws Exception {
        assertEquals(Card.Trump.HEARTS, testCard1.getTrump());
        assertEquals(Card.Trump.NONE, testCard2.getTrump());
        assertEquals(Card.Trump.NONE, testCard3.getTrump());
        assertEquals(Card.Trump.NONE, testCard4.getTrump());
    }

}
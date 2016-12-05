package common;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kj on 27/11/16.
 */
public class AnnouceTest {

    Annouce     test = new Annouce();
    Annouce     test2 = new Annouce();
    Annouce     test3 = new Annouce();
    Annouce     test4 = new Annouce();

    @Test
    public void canAnnouce() throws Exception {
        assertEquals(true, test.canAnnouce());
        test.setPass(true);
        assertEquals(false, test.canAnnouce());
    }

    @Test
    public void setAnnouce() throws Exception {

    }

    @Test
    public void getAnnouce() throws Exception {
        test.setAnnouce(2, 90);
        test2.setAnnouce(-1, 80);
        test3.setAnnouce(3, 20);
        test4.setAnnouce(-1, -1);
        assertEquals(90, test.getAnnouce());
        assertEquals(80, test2.getAnnouce());
        assertEquals(0, test3.getAnnouce());
        assertEquals(0, test4.getAnnouce());
    }

    @Test
    public void getTrump() throws Exception {
        test.setAnnouce(2, 90);
        test2.setAnnouce(-1, 80);
        test3.setAnnouce(3, 20);
        test4.setAnnouce(-1, -1);
        assertEquals(Card.Trump.DIAMOND, test.getTrump());
        assertEquals(Card.Trump.NONE, test2.getTrump());
        assertEquals(Card.Trump.SPADES, test3.getTrump());
        assertEquals(Card.Trump.NONE, test4.getTrump());
    }

    @Test
    public void getMsgAnnouce() throws Exception {
        test.setAnnouce(2, 90);
        test3.setAnnouce(3, 20);
        assertEquals("90 at: DIAMOND", test.getMsgAnnouce());
        assertEquals("0 at: SPADES", test3.getMsgAnnouce());

    }

    @Test
    public void setPass() throws Exception {

    }

}
package common;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kj on 27/11/16.
 */
public class DeckTest {

    Deck    test = new Deck();

    @Test
    public void getDeck() throws Exception {
        test.generateDeck();
        assertEquals(7, test.getEquiv()[0]);
        assertNotNull(test.getDeck());
    }

}
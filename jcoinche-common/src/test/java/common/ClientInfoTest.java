package common;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kj on 27/11/16.
 */
public class ClientInfoTest {

    ClientInfo test = new ClientInfo(42, null);

    @Test
    public void resetHand() throws Exception {
        test.resetHand();
        assertEquals(0, test.getCards().size());
    }

    @Test
    public void sendObject() throws Exception {

    }

    @Test
    public void tellToAnnouce() throws Exception {

    }

    @Test
    public void tellToPlay() throws Exception {

    }

    @Test
    public void sendMsg() throws Exception {

    }

    @Test
    public void canAnnouce() throws Exception {

    }

    @Test
    public void getAnnouce() throws Exception {

    }

    @Test
    public void pass() throws Exception {

    }

    @Test
    public void resetPass() throws Exception {

    }

    @Test
    public void setAnnouce() throws Exception {

    }

    @Test
    public void getMsgAnnouce() throws Exception {

    }

    @Test
    public void getMsgPass() throws Exception {

    }

    @Test
    public void addCard() throws Exception {
        test.addCard(new Card(-1, -1));
        assertEquals(1, test.getCards().size());
    }

    @Test
    public void setTeam() throws Exception {
        test.setTeam(42);
        assertEquals(42, test.getIdTeam());
    }

    @Test
    public void getMsgStart() throws Exception {

    }

    @Test
    public void getMsgWinTurn() throws Exception {

    }

    @Test
    public void deleteCard() throws Exception {

    }

    @Test
    public void deleteCard1() throws Exception {

    }

    @Test
    public void canPlayThisCard() throws Exception {

    }

    @Test
    public void confirmPlay() throws Exception {

    }

    @Test
    public void tellErrorPlay() throws Exception {

    }

    @Test
    public void printListCards() throws Exception {

    }

    @Test
    public void getIdClient() throws Exception {
        assertEquals(42, test.getIdClient());
    }

    @Test
    public void incrScore() throws Exception {

    }

    @Test
    public void getScore() throws Exception {

    }

    @Test
    public void getCards() throws Exception {

    }

    @Test
    public void getIdTeam() throws Exception {

    }

    @Test
    public void getNbCards() throws Exception {

    }

    @Test
    public void closeChannel() throws Exception {

    }

    @Test
    public void tellToEnd() throws Exception {

    }

}
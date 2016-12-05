package common;

/**
 * Created by kaso on 23/11/16.
 */
public class Annouce {

    /**
     * PRIVATE ATTRIBUTES
     */

    private boolean _pass = false;
    private int     _announce = 0;
    private Card.Trump  _trump = Card.Trump.NONE;

    /**
     * PUBLIC METHODS
     */

    public boolean canAnnouce() {
        return (!_pass);
    }

    public void setAnnouce(int trump, int announce) {
        if (trump > 0 && trump <= 4)
            _trump = Card.Trump.values()[trump];
        else
            _trump = Card.Trump.NONE;
        if (announce >= 80)
            _announce = announce;
        else
            _announce = 0;
    }

    public int  getAnnouce() {
        return (_announce);
    }

    public Card.Trump getTrump() {
        return (_trump);
    }

    public String getMsgAnnouce() {
        return (_announce + " at: " + _trump.name);
    }

    public void setPass(boolean pass) {
        _pass = pass;
    }
}

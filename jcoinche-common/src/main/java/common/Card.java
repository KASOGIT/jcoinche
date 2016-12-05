package common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kj on 21/11/16.
 */
public class Card {

    public enum InfoCard {
        SEVEN(7, "7", 0, 0),
        EIGHT(8, "8", 0, 0),
        NINE(9, "9", 0, 14),
        TEN(10, "10", 10, 10),
        JACK(11, "JACK", 2, 20),
        QUEEN(12, "QUEEN", 3, 3),
        KING(13, "KING", 4, 4),
        ACE(1, "ACE", 11, 11),
        NONE(0, null, 0, 0);

        public int id;
        public String name;
        public int valueNormal;
        public int valueTrump;
        InfoCard(int id, String name, int valueNormal, int valueTrump) {
            this.id = id;
            this.name = name;
            this.valueNormal = valueNormal;
            this.valueTrump = valueTrump;
        }
    }

    public enum Trump {
        NONE(0, null),
        HEARTS(1, "HEARTS"),
        DIAMOND(2, "DIAMOND"),
        SPADES(3, "SPADES"),
        CLUB(4, "CLUB");

        public int id;
        public String name;
        Trump(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private static Map<Integer, InfoCard> mapCard = new HashMap<>();

    static {
            InfoCard[] infoCards = InfoCard.values();
            mapCard.put(7, infoCards[0]);
            mapCard.put(8, infoCards[1]);
            mapCard.put(9, infoCards[2]);
            mapCard.put(10, infoCards[3]);
            mapCard.put(11, infoCards[4]);
            mapCard.put(12, infoCards[5]);
            mapCard.put(13, infoCards[6]);
            mapCard.put(1, infoCards[7]);
            mapCard.put(0, infoCards[8]);
    }

    private InfoCard  _idCard;
    private Trump     _trump;

    public Card(int id, int trump) {
        try {
            this._idCard = mapCard.get(id);
            this._trump = Trump.values()[trump];
        }
        catch (Exception e) {
            this._idCard = mapCard.get(0);
            this._trump = Trump.values()[0];
        }
        if (this._idCard == null) {
            this._idCard = mapCard.get(0);
            this._trump = Trump.NONE;
        }
    }

    public InfoCard getInfoCard() { return _idCard; }
    public Trump getTrump() { return _trump; }

}

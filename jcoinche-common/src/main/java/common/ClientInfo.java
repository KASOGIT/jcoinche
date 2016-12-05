package common;

/**
 * Created by kj on 21/11/16.
 */

import io.netty.channel.Channel;
import protomsg.CoincheMsg;

import java.util.LinkedList;

public class ClientInfo {

    /**
     * PUBLIC STATIC ATTRIBUTES
     */

    public static int TYPE_CLIENT = 0;
    public static int TYPE_SERVER = 1;

    /**
     * PRIVATE ATTRIBUTES
     */

    private int     _idClient = -1;
    private int     _idTeam = -1;
    private LinkedList<Card>    _cards = new LinkedList<>();
    private int     _score = 0;
    private Channel _channel = null;
    private Annouce _annouce = new Annouce();

    /**
     * PUBLIC METHODS
     */

    public ClientInfo(int id) {
        _idClient = id;
    }

    public ClientInfo(int id, Channel chan) {
        this(id);
        _channel = chan;
    }

    public void resetHand() {
        _cards.clear();
    }

    public void sendObject(CoincheMsg.ServerMsg msg) {
        _channel.writeAndFlush(msg);
    }

    public void sendObject(CoincheMsg.ClientMsg msg) {
        _channel.writeAndFlush(msg);
    }

    public void tellToAnnouce() {
        CoincheMsg.ServerMsg msg = CoincheMsg.ServerMsg.newBuilder()
                .setTypeRequest(CoincheMsg.ServerMsg.TypeRequestServer.ANNOUCE)
                .build();
        sendObject(msg);
    }

    public void tellToPlay() {
        CoincheMsg.ServerMsg msg = CoincheMsg.ServerMsg.newBuilder()
                .setTypeRequest(CoincheMsg.ServerMsg.TypeRequestServer.PLAY)
                .build();
        sendObject(msg);
    }

    public void sendMsg(String msg, int type) {
        if (_channel != null && _channel.isActive()) {
            if (type == TYPE_SERVER) {
                CoincheMsg.ServerMsg toSend = CoincheMsg.ServerMsg.newBuilder()
                        .setTypeRequest(CoincheMsg.ServerMsg.TypeRequestServer.INFO)
                        .setInfo(CoincheMsg.ServerMsg.Info.newBuilder().setInfoGame(msg)).build();
                sendObject(toSend);
            }
            else if (type == TYPE_CLIENT) {
                CoincheMsg.ClientMsg toSend = CoincheMsg.ClientMsg.newBuilder()
                        .setTypeRequest(CoincheMsg.ClientMsg.TypeRequestClient.INFO)
                        .setInfo(CoincheMsg.ClientMsg.Info.newBuilder().setInfo(msg)).build();
                sendObject(toSend);
            }
        }
    }

    public boolean  canAnnouce() {
        return (_annouce.canAnnouce());
    }

    public Annouce getAnnouce() {
        return (_annouce);
    }

    public void pass() {
        _annouce.setPass(true);
    }

    public void resetPass() {_annouce.setPass(false);}

    public void setAnnouce(int trump, int value) {
        _annouce.setAnnouce(trump, value);
    }

    public String getMsgAnnouce() {
        return ("Player: " + _idClient + " team: " + _idTeam + " ANNOUCE: " + _annouce.getMsgAnnouce());
    }

    public String getMsgPass() {
        return ("Player: " + _idClient + " team: " + _idTeam + " PASS his turn");
    }

    public void addCard (Card new_card) {
        _cards.add(new_card);
    }
    public void setTeam(int team) {
        _idTeam = team;
    }

    public String getMsgStart() {
        return ("Player: " + _idClient + " team: " + _idTeam + " will start for: " + _annouce.getMsgAnnouce());
    }

    public String getMsgWinTurn() {
        return ("Player: " + _idClient + " team: " + _idTeam + " win this turn!");
    }

    public void deleteCard(int idx) { _cards.remove(idx); }

    public void deleteCard(Card toDel) {
        for (Card card : _cards) {
            if (card.getInfoCard().id == toDel.getInfoCard().id && card.getTrump().id == toDel.getTrump().id) {
                System.out.println("card deleted");
                _cards.remove(card);
                break;
            }
        }
    }

    public boolean canPlayThisCard(Card played, Card first, Annouce announce) {
        System.out.println("PLAYED TRUMP: " + played.getTrump().id + " FIRST TRUMP: " + first.getTrump().id + " announce trump: " + announce.getTrump().id);
        boolean hasFamily = false;
        boolean hasTrump = false;
        for (Card card : _cards) {
            if (card.getTrump().id == played.getTrump().id && card.getInfoCard().id == played.getInfoCard().id) {
                continue;
            }
            if (card.getTrump().id == first.getTrump().id) {
                hasFamily = true;
                break;
            }
        }
        for (Card card : _cards) {
            if (card.getTrump().id == played.getTrump().id && card.getInfoCard().id == played.getInfoCard().id) {
                continue;
            }
            if (card.getTrump().id == announce.getTrump().id) {
                hasTrump = true;
                break;
            }
        }
        System.out.println("family: " + hasFamily);
        if (first.getTrump().id == announce.getTrump().id && played.getTrump().id == announce.getTrump().id) {
            System.out.println("here 1");
            return (true);
        }
        else if (played.getTrump().id == announce.getTrump().id && hasFamily) {
            System.out.println("here 2");
            return (false);
        }
        else if (played.getTrump().id != first.getTrump().id && hasFamily) {
            System.out.println("here 3");
            return (false);
        }
        else if (played.getTrump().id != first.getTrump().id && hasTrump) {
            System.out.println("here 4");
            return (false);
        }
        System.out.println("here 5");
        return (true);
    }

    public void confirmPlay(Card card) {
        CoincheMsg.ServerMsg toSend = CoincheMsg.ServerMsg.newBuilder()
                .setTypeRequest(CoincheMsg.ServerMsg.TypeRequestServer.PLAY_CONFIRM)
                .setPlayConfirm(CoincheMsg.ServerMsg.PlayConfirm.newBuilder().setCard(CoincheMsg.Card.newBuilder()
                        .setValue(card.getInfoCard().id)
                        .setType(card.getTrump().id)))
                .build();
        sendObject(toSend);
    }

    public void tellErrorPlay() {
        CoincheMsg.ServerMsg toSend = CoincheMsg.ServerMsg.newBuilder()
                .setTypeRequest(CoincheMsg.ServerMsg.TypeRequestServer.ERROR_PLAY)
                .build();
        sendObject(toSend);
    }

    public void printListCards() {
        int     i = 0;
        for (Card card : _cards) {
            System.out.println("Card number " + i + " : " + card.getInfoCard().name + " " + card.getTrump().name());
            i++;
        }
    }
    public int  getIdClient() { return _idClient; }

    public void incrScore(int toIncr) {
        _score += toIncr;
    }
    public int  getScore() { return _score; }
    public LinkedList<Card> getCards() { return _cards; }

    public int getIdTeam() {
        return (_idTeam);
    }
    public int getNbCards() { return (_cards.size()); }

    public void closeChannel() {
        _channel.disconnect();
    }

    public void tellToEnd() {
        CoincheMsg.ServerMsg toSend = CoincheMsg.ServerMsg.newBuilder()
                .setTypeRequest(CoincheMsg.ServerMsg.TypeRequestServer.FINISH)
                .build();
        sendObject(toSend);
    }

}

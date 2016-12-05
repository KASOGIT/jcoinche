package server;

import common.Annouce;
import common.Card;
import common.ClientInfo;
import common.Deck;
import protomsg.CoincheMsg;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by kaso on 21/11/16.
 */

public class Game {

    /**
     * STATIC PRIVATE ATTRIBUTES
     */

    private static final int MAX_PLAYER = 4;
    private static final int CARD_BY_HAND = 8;
    private static final String GAME_START_MSG = "Game is complete, wait for your turn";
    private static final String WELCOME_MSG = "Welcome on the jcoinche server ! waiting for others player";
    private static final int CARD_BY_TURN = 4;
    private static final int MAX_TURN = 8;

    /**
     * PRIVATE ATTRIBUTES
     */

    private int nbPlayer = 0;
    private ArrayList<ClientInfo> players = new ArrayList<>();
    private Deck _deck = new Deck();
    private Annouce _announce = new Annouce();
    private int idTeamAnnouce = -1;
    private ArrayList<Card> _stack = new ArrayList<>();
    private ClientInfo _winnerAnnonce = null;
    private ClientInfo _winnerTurn = null;
    private int _turn = 0;
    private int _countNext = 0;

    /**
     * PRIVATE METHODS
     */

    private boolean annoucesDone() {
        for (ClientInfo player : players) {
            if (player.canAnnouce()) {
                return (false);
            }
        }
        return (false);
    }

    private int getScoreTeam(int idTeam) {
        int score = 0;
        for (ClientInfo player : players) {
            if (player.getIdTeam() == idTeam) {
                score += player.getScore();
            }
        }
        return (score);
    }

    public void closeClientConnection() {
        for (ClientInfo client : players) {
            client.tellToEnd();
            client.closeChannel();
        }
    }

    /**
     * PUBLIC METHODS
     */

    public boolean pushCardStack(ClientInfo current, common.Card played) {
        if (_winnerTurn == null) {
            _winnerTurn = current;
        }
        else {
            Card first = _stack.get(0);
            if (!current.canPlayThisCard(played, first, _announce)) {
                current.tellErrorPlay();
                return (false);
            }
            boolean isBest = true;
            if (played.getTrump().id == _announce.getTrump().id) {
                for (Card card : _stack) {
                    if (card.getTrump().id == _announce.getTrump().id && card.getInfoCard().valueTrump > played.getInfoCard().valueTrump) {
                        isBest = false;
                        break;
                    }
                    else if (card.getTrump().id == _announce.getTrump().id && card.getInfoCard().valueTrump == played.getInfoCard().valueTrump && card.getInfoCard().id > played.getInfoCard().id) {
                        isBest = false;
                        break;
                    }
                }
                System.out.println("here best atout");
            }
            else if (played.getTrump().id == first.getTrump().id) {
                for (Card card : _stack) {
                    if (card.getTrump().id == first.getTrump().id && card.getInfoCard().valueNormal > played.getInfoCard().valueNormal) {
                        isBest = false;
                        break;
                    }
                    else if (card.getTrump().id == first.getTrump().id && card.getInfoCard().valueNormal == played.getInfoCard().valueNormal && card.getInfoCard().id > played.getInfoCard().id) {
                        isBest = false;
                        break;
                    }
                }
                System.out.println("here best couleur jouee");
            }
            else {
                System.out.println("here else pisse");
                isBest = false;
            }
            if (isBest) {
                _winnerTurn = current;
            }
        }
        _stack.add(played);
        current.deleteCard(played);
        current.confirmPlay(played);
        sendInfoToPlayers("Player: " + current.getIdClient() + " team: " + current.getIdTeam() + " played : " + played.getInfoCard().name + " of: " + played.getTrump().name);
        if (_stack.size() == CARD_BY_TURN) {
            for (Card card : _stack) {
                if (_announce.getTrump() == card.getTrump()) {
                    _winnerTurn.incrScore(card.getInfoCard().valueTrump);
                }
                else {
                    _winnerTurn.incrScore(card.getInfoCard().valueNormal);
                }
            }
            _turn++;
            sendInfoToPlayers(_winnerTurn.getMsgWinTurn());
            _stack.clear();
            if (_turn == MAX_TURN) {
                int finalScore = getScoreTeam(idTeamAnnouce);
                if (finalScore >= _announce.getAnnouce()) {
                    sendInfoToPlayers("Team: " + idTeamAnnouce + " call: " + _announce.getAnnouce() + " and win with a score of: " + finalScore + " ");
                }
                else {
                    sendInfoToPlayers("Team: " + idTeamAnnouce + " call: " + _announce.getAnnouce() + " and loose with a score of: " + finalScore + " ");
                }
                closeClientConnection();
                return (true);
            }
            else {
                _winnerTurn.tellToPlay();
                _winnerTurn = null;
            }
        }
        else {
            getNextPlayerToPlay(current.getIdClient()).tellToPlay();
        }
        return (false);
    }

    public void addPlayer(ClientInfo player) {
        System.out.println("SERVER: add player to a game: " + player.toString());
        this.players.add(player);
        player.sendMsg(WELCOME_MSG, ClientInfo.TYPE_SERVER);
        nbPlayer++;
    }

    public boolean isFull() {
        return nbPlayer == MAX_PLAYER;
    }

    public void startGame() {
        System.out.println("SERVER: start game");
        LinkedList<Card> deck = _deck.getDeck();
        int where = 0;
        for (int p = 0; p < players.size(); ++p)  {
            CoincheMsg.ServerMsg.Start.Builder hand = CoincheMsg.ServerMsg.Start.newBuilder();
            hand.setTeam(p % 2 == 0);
            players.get(p).setTeam(p % 2);
            hand.setIdPlayer(players.get(p).getIdClient());
            for (int i = where; i < where + CARD_BY_HAND; ++i) {
                hand.addCards(CoincheMsg.Card.newBuilder()
                        .setValue(deck.get(i).getInfoCard().id)
                        .setType(deck.get(i).getTrump().id));
                players.get(p).addCard(deck.get(i));
            }
            where += CARD_BY_HAND;
            CoincheMsg.ServerMsg msg = CoincheMsg.ServerMsg.newBuilder()
                    .setTypeRequest(CoincheMsg.ServerMsg.TypeRequestServer.START)
                    .setStart(hand)
                    .setInfo(CoincheMsg.ServerMsg.Info.newBuilder().setInfoGame(GAME_START_MSG)).build();
            players.get(p).sendObject(msg);
            System.out.println("SERVER: send hand for player: " + players.get(p).getIdClient());
        }
        players.get(0).tellToAnnouce();
    }

    public boolean hasPlayerById(int id) {
        for (ClientInfo player : players) {
            if (player.getIdClient() == id) {
                return (true);
            }
        }
        return (false);
    }

    public void sendInfoToPlayers(String info) {
        for (ClientInfo player : players) {
            player.sendMsg(info, ClientInfo.TYPE_SERVER);
        }
    }

    private void resetHandPlayer() {
        for (ClientInfo player : players) {
            player.resetHand();
        }
    }

    private void resetPassPlayer() {
        for (ClientInfo player : players) {
            player.resetPass();
        }
    }

    private ClientInfo getNextPlayerToAnnouce(int idCurrent) {
        System.out.println("getnextplayer");
        int idNext = idCurrent + 1 > 3 ? 0 : idCurrent + 1;
        if (players.get(idNext).canAnnouce()) {
            return (players.get(idNext));
        }
        else {
            _countNext++;
            if (_countNext == 4) {
                _countNext = 0;
                resetHandPlayer();
                resetPassPlayer();
                startGame();
                return (null);
            }
            return (getNextPlayerToAnnouce(idNext));
        }
    }

    private ClientInfo getNextPlayerToPlay(int idCurrent) {
        int idNext = idCurrent + 1 > 3 ? 0 : idCurrent + 1;
        return (players.get(idNext));
    }

    private ClientInfo isPlayerWinAnnouce() {
        ClientInfo winner = null;
        for (ClientInfo player : players) {
            if (player.canAnnouce() && player.getAnnouce().getAnnouce() != 0 && winner == null) {
                winner = player;
            }
            else if (player.canAnnouce() && winner != null) {
                return (null);
            }
        }
        return (winner);
    }

    public void checkStartGame(ClientInfo player) {
        ClientInfo winner = isPlayerWinAnnouce();
        if (winner != null) {
            System.out.println("winner annonce");
            _winnerAnnonce = winner;
            _announce = winner.getAnnouce();
            idTeamAnnouce = winner.getIdTeam();
            sendInfoToPlayers(winner.getMsgStart());
            winner.tellToPlay();
        }
        else {
            ClientInfo nextPlayer = getNextPlayerToAnnouce(player.getIdClient());
            if (nextPlayer != null) {
                nextPlayer.tellToAnnouce();
            }
        }
    }
}

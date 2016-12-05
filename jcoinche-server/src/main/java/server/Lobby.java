package server;

import common.ClientInfo;
import io.netty.channel.Channel;

import java.util.List;

/**
 * Created by kaso on 23/11/16.
 */

public class Lobby {

    /**
     * STATIC PRIVATE ATTRIBUTES
     */

    private static int nbClient = 0;
    private static final String ERROR_DISCONNECT = "ERROR: A PLAYER LEAVE THE GAME, END OF GAME...";

    /**
     * PRIVATE ATTRIBUTES
     */

    private List<Game> games;

    /**
     * PUBLIC METHODS
     */

    public Lobby(List<Game> games) {
        this.games = games;
    }

    public ClientInfo addPlayerToGame(Channel chan) {
        ClientInfo player = null;
        if (games.isEmpty()) {
            games.add(new Game());
            games.get(0).addPlayer(player = new ClientInfo(nbClient, chan));
            nbClient++;
        }
        else {
            for (Game game : games) {
                if (!game.isFull()) {
                    game.addPlayer(player = new ClientInfo(nbClient, chan));
                    nbClient++;
                    if (game.isFull()) {
                        game.startGame();
                    }
                }
            }
            if (player == null) {
                games.add(new Game());
                games.get(games.size()).addPlayer(player = new ClientInfo(nbClient, chan));
                nbClient++;
            }
        }
        System.out.println("Lobby player: " + player.toString());
        return (player);
    }

    public void deleteGame(Game game) {
        games.remove(game);
    }

    public Game getGameOfPlayer(ClientInfo player) {
        for (Game game : games) {
            if (game.hasPlayerById(player.getIdClient())) {
                return (game);
            }
        }
        return (null);
    }

    public void closeGameOfPlayer(ClientInfo player) {
        Game game = getGameOfPlayer(player);
        if (game != null) {
            game.sendInfoToPlayers(ERROR_DISCONNECT);
            game.closeClientConnection();
        }
    }
}

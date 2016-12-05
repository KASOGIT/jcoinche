package server;

/**
 * Created by kaso on 21/11/16.
 */

import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
import common.ClientInfo;
import common.WorkerTab;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import protomsg.CoincheMsg;

import java.util.LinkedList;

public class JCoincheServerHandler extends SimpleChannelInboundHandler<CoincheMsg.ClientMsg> {

    /**
     * PRIVATE ATTRIBUTES
     */

    private WorkerTab tabWorker;
    private ClientInfo  player;
    private Lobby lobby;
    private CoincheMsg.ClientMsg    lastMsg = null;

    /**
     * PUBLIC METHODS
     */

    JCoincheServerHandler(Lobby lobby, WorkerTab tabWorker) {
        this.lobby = lobby;
        this.tabWorker = tabWorker;
        this.initTabWorker();
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, CoincheMsg.ClientMsg clientMsg) throws Exception {
        System.out.println("SERVER: receive data: " + clientMsg.toString());
        switch (clientMsg.getTypeRequestValue()) {
            case (CoincheMsg.ClientMsg.TypeRequestClient.ANNOUCE_VALUE):
                handleAnnouce(clientMsg);
                break;
            case (CoincheMsg.ClientMsg.TypeRequestClient.PLAY_VALUE):
                handlePlay(clientMsg);
                break;
            case (CoincheMsg.ClientMsg.TypeRequestClient.INFO_VALUE):
                handleInfo(clientMsg);
                break;
            default:
                System.out.println("error cmd");
                break;
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.player = lobby.addPlayerToGame(ctx.channel());
        System.out.println("player handler: " + player.toString());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        lobby.closeGameOfPlayer(player);
    }

    /**
     * PRIVATE METHODS
     */

    private void initTabWorker() {
    }

    private void handlePlay(CoincheMsg.ClientMsg lastMsg) {
        System.out.println("handle play");
        Game game = lobby.getGameOfPlayer(player);
        boolean end = game.pushCardStack(player, new common.Card(lastMsg.getPlay().getCard().getValue(), lastMsg.getPlay().getCard().getType()));
        if (end) {
            lobby.deleteGame(game);
        }
    }

    private void handleInfo(CoincheMsg.ClientMsg lastMsg) {
        System.out.println(lastMsg.getInfo().getInfo());
    }

    private void handleAnnouce(CoincheMsg.ClientMsg lastMsg) {
        System.out.println("here handle announce ");
        if (player.canAnnouce()) {
            Game game = lobby.getGameOfPlayer(player);
            if (lastMsg.getAnnounce().getPass() == 1) {
                player.pass();
                game.sendInfoToPlayers(player.getMsgPass());
            }
            else {
                player.setAnnouce(lastMsg.getAnnounce().getTrump(), lastMsg.getAnnounce().getValue());
                game.sendInfoToPlayers(player.getMsgAnnouce());
            }
            game.checkStartGame(player);
        }
    }
}

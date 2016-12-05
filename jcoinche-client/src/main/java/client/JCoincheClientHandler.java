package client;

/**
 * Created by kaso on 21/11/16.
 */

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import common.ClientInfo;
import common.WorkerTab;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protomsg.CoincheMsg;

public class JCoincheClientHandler extends SimpleChannelInboundHandler<CoincheMsg.ServerMsg> {

    private Channel channel;
    private CoincheMsg.ServerMsg resp = null;
    private BlockingQueue<CoincheMsg.ServerMsg> resps = new LinkedBlockingQueue<CoincheMsg.ServerMsg>();
    private WorkerTab   tabWorker = new WorkerTab();
    private ClientInfo  info;
    private Parcer      parcer = new Parcer();
    private int         lastIdxPlayed = -1;

    JCoincheClientHandler() {
        this.initTabWorker();
    }

    public void handleRequest() {
        // Now wait for response from server
        boolean interrupted = false;
        for (;;) {
            try {
                resp = resps.take();
            } catch (InterruptedException ignore) {
                Thread.currentThread().interrupt();
            }
            if (resp.getTypeRequest() == CoincheMsg.ServerMsg.TypeRequestServer.FINISH)
                break;
            this.tabWorker.runWorker(resp.getTypeRequest().getNumber());
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CoincheMsg.ServerMsg msg)
            throws Exception {
        resps.add(msg);
    }


    public void initTabWorker() {
        this.tabWorker.addWorker(CoincheMsg.ServerMsg.TypeRequestServer.START_VALUE, this::startGame);
        this.tabWorker.addWorker(CoincheMsg.ServerMsg.TypeRequestServer.ANNOUCE_VALUE, this::playGame);
        this.tabWorker.addWorker(CoincheMsg.ServerMsg.TypeRequestServer.INFO_VALUE, this::handleInfo);
        this.tabWorker.addWorker(CoincheMsg.ServerMsg.TypeRequestServer.PLAY_VALUE, this::playTurn);
        this.tabWorker.addWorker(CoincheMsg.ServerMsg.TypeRequestServer.ERROR_PLAY_VALUE, this::replay);
        this.tabWorker.addWorker(CoincheMsg.ServerMsg.TypeRequestServer.PLAY_CONFIRM_VALUE, this::deleteCard);

    }

    public void handleInfo() {
        System.out.println(resp.getInfo().getInfoGame());
    }


    public void startGame() {
        System.out.println("Game Started");
        info = new ClientInfo(resp.getStart().getIdPlayer(), channel);
        for (int i = 0; i < 8; i++)
        info.addCard(new common.Card(resp.getStart().getCards(i).getValue(), resp.getStart().getCards(i).getType()));
    }

    private void playGame() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("It's your turn to make your announce ! (Example : ANNOUNCE [Value > 0] [Trump]");
        info.printListCards();
        info.sendObject(parcer.readAnnounce());
    }

    public void playTurn() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("It's your turn to play a card ! (PLAY [Index Card]");
        playCard();
    }

    public void replay() {
        System.out.println("You play a wrong card !");
        playCard();
    }
    public void playCard() {
        info.printListCards();
        int idx = parcer.readPlay();
        if (idx >= 0 && idx < info.getNbCards()) {
            CoincheMsg.ClientMsg msgSend = CoincheMsg.ClientMsg.newBuilder()
                    .setTypeRequest(CoincheMsg.ClientMsg.TypeRequestClient.PLAY)
                    .setPlay(CoincheMsg.ClientMsg.Play.newBuilder()
                            .setCard(CoincheMsg.Card.newBuilder()
                                    .setValue(info.getCards().get(idx).getInfoCard().id)
                                    .setType(info.getCards().get(idx).getTrump().id))
                    ).build();
            info.sendObject(msgSend);
            lastIdxPlayed = idx;
        }
        else
            replay();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void deleteCard() {
        info.deleteCard(lastIdxPlayed);
    }

    public Channel getChannel() {
        return (channel);
    }

    public ClientInfo getClientInfo() {
        return (info);
    }
}

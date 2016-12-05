package server;

/**
 * Created by kaso on 21/11/16.
 */

import common.WorkerTab;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import protomsg.CoincheMsg;

import java.util.LinkedList;

public class JCoincheServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private Lobby lobby;
    private WorkerTab tabWorker;

    JCoincheServerChannelInitializer(Lobby lobby, WorkerTab tabWorker) {
        this.lobby = lobby;
        this.tabWorker = tabWorker;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline p = ch.pipeline();

        p.addLast(new ProtobufVarint32FrameDecoder());
        p.addLast(new ProtobufDecoder(CoincheMsg.ClientMsg.getDefaultInstance())); // set object

        p.addLast(new ProtobufVarint32LengthFieldPrepender());
        p.addLast(new ProtobufEncoder());

        p.addLast(new JCoincheServerHandler(this.lobby, this.tabWorker));
        System.out.println("init channel");
    }
}
